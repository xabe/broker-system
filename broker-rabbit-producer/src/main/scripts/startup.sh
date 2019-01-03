#! /bin/bash

# small template for my bash shell scripts.

#set -o errexit  # the script ends if a command fails
#set -o pipefail # the script ends if a command fails in a pipe
#set -o nounset  # the script ends if it uses an undeclared variable
# set -o xtrace # if you want to debug

# Defaults values
logLevel=4 #4-debug;3-info;2-success;1-warning;0-error

appFolder="#{app.parent.dir}"
deployFolder="${appFolder}/deploy"
libFolder="${appFolder}/lib"
configFolder="${appFolder}/config/"
logFolder="${appFolder}/logs/"
daemonizeDir="$(which daemonize)"
javaDir="$(which java)"
app="#{app.name}"
port=8085
pid="${appFolder}/pid"
gcLog="${logFolder}GC.log"
serverLog="${logFolder}/Trace.${app}.`hostname`.log"
messageStarted="Started"
appMainClass="#{app.main.class}"

javaOpts="-Xmx512M \
 -Xms512M \
 -Xss512K \
 -XX:MetaspaceSize=150M \
 -XX:MaxMetaspaceSize=150M \
 -XX:CompressedClassSpaceSize=150M \
 -XX:+UseParallelGC \
 -Djava.security.egd=file:/dev/./urandom \
 -Dsun.rmi.dgc.client.gcInterval=3600000 \
 -Dsun.rmi.dgc.server.gcInterval=3600000 \
 -XX:+HeapDumpOnOutOfMemoryError \
 -XX:HeapDumpPath=${appFolder}/dump.hprof \
 -Xrunjdwp:server=y,transport=dt_socket,address=2005,suspend=n \
 -Djava.net.preferIPv4Stack=true \
 -Xdebug \
 -Xlog:gc* \
 -verbose:gc \
 -Xlog:gc:${gcLog} \
 -Dorg.jboss.logging.provider=slf4j \
 -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
 -DAsyncLogger.RingBufferSize=128 \
 -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=DEBUG \
 -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \
 -Dlog4j.configurationFile=file://${configFolder}/log4j2.xml \
 -Dapp.log.path=${logFolder} \
 -Dapp.log.name=${app}"

jar=$(ls -r1 ${deployFolder}/broker-* | head -n1)
#[[ ! -f "${jar}" ]] && error "Error not found merak in ${deployFolder}"; exit 1;
classPath="${libFolder}/*:${configFolder}:${deployFolder}/*"

javaCommand="${javaDir} ${javaOpts} -cp ${classPath} ${appMainClass}"

# Usage function
usage() {
  echo -n "Usage:
          start||debug||restart||stop||status||info"
}

# Log functions
error() {
  printf "\033[0;31m%s\033[0m\n" "$1"
}
warning() {
  if [[ ${logLevel} -gt 0 ]]; then
    printf "\033[1;33m%s\033[0m\n" "$1"
  fi
}
success() {
  if [[ ${logLevel} -gt 1 ]]; then
    printf "\033[0;32m%s\033[0m\n" "$1"
  fi
}
info() {
  if [[ ${logLevel} -gt 2 ]]; then
    printf "\033[0;34m%s\033[0m\n" "$1"
  fi
}
debug() {
  if [[ ${logLevel} -gt 3 ]]; then
    printf "\033[1;30m%s\033[0m\n" "$1"
  fi
}

check() {
  local function_num_params=$#
  if [[ ${function_num_params} -lt 1 ]]; then
    error "At least one parameter must be introduced. action"
    exit 1
fi
}

# Params function
params() {
	global_action=$1
}

function is_running {
  #[[ ! -e /proc/${global_pid} ]] && return 1 mac not work
  [[ -z "${global_pid}" ]] && return 1
  return 0
}

function service_pid {
  global_pid=$(ps -ef | grep java | grep "\-Dapp.log.name=${app}" | egrep -v "grep|bash" | awk '{print $2}')
  is_running || return 1
  return 0
}

function remove_pid {
  rm -f ${pid}
}

function started {
  grep -q "${messageStarted}" "${serverLog}" > /dev/null 2>&1
  if [[ $? -eq 0 && running ]]; then return 0; fi
  return 1
}

function rename_file_date {
  if [[ -f $1 ]]; then mv $1 $1.$(date +\%Y"-"\%m"-"\%d"-"\%H"-"\%M); fi
}

function start_service {
  service_pid
  if [[ $? -eq 0 ]]; then failure ${app} already started; echo; return 0; fi

  remove_pid
  rename_file_date ${gcLog}
  rename_file_date ${serverLog}
  info "Starting ${app}"
  $(${daemonizeDir} -p ${pid} -l ${pid} ${javaCommand})
  service_pid
  if [[ $? -ne 0 ]]; then error "${app} start"; echo; return 1; fi
  local wait_sec=60

  while [[ ${wait_sec} -ne 0 ]]; do
    sleep 2
    echo -n "."
    if started ; then
      wait_sec=0
      break
    fi
    if ! is_running ; then
      echo
      error "Error to start ${app}"
      return 1
    fi
    wait_sec=$((wait_sec -2))
  done

  if [[ ! started ]]; then
    echo
    error "Error to start ${app}"
    remove_pid
    echo
    return 1
  else
    echo
    success "OK"
  fi
  echo
}

function stop_service {
  info "Stopping  ${app}: "
  service_pid
  if [[ $? -eq 1 ]]; then error $"${app} not running" ; echo; return 0; fi

  kill ${global_pid}
  local wait_sec=15
  while [[ ${wait_sec} -ne 0 ]]; do
    sleep 2
    echo -n "."
    service_pid
    if  ! is_running ; then
      wait_sec=0
      break
    fi
    wait_sec=$((wait_sec - 2))
  done

  if is_running ; then
  echo -n "Send kill -9 for stoped app ${app}" 1>&2
    kill -9 ${global_pid}
    sleep 1
  fi

  success "OK"
  remove_pid
  echo
}

execute() {
    case ${global_action} in
start)
  start_service
  ;;
stop)
  stop_service
  ;;
restart)
  stop_service
  start_service
  ;;
status)
  status_app
  ;;
*)
  usage
  exit 1
esac
exit $?
}

# Main function
main() {
  check "$@"
  params "$@"
  execute
}

main "$@"