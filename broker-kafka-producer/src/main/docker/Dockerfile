FROM openjdk:12-jdk-alpine

RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /home/app
COPY --chown=app:app producer-kafka.tar.gz app.tar.gz
RUN touch app.tar.gz
RUN tar -xzf app.tar.gz
RUN rm -rf app.tar*
RUN chmod 777 /home/app/producer-kafka/logs

ENV KAFKA_HOSTS="kafka:9092"
ENV HOME_APP="/home/app/producer-kafka"
ENV JAVA_OPTS=" -Xmx512M \
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
                -Dkafka.bootstrap-servers=${KAFKA_HOSTS} \
                -XX:HeapDumpPath=${HOME_APP}/dump.hprof \
                -Xrunjdwp:server=y,transport=dt_socket,address=2005,suspend=n \
                -Djava.net.preferIPv4Stack=true \
                -Xdebug \
                -Xlog:gc* \
                -verbose:gc \
                -Xlog:gc:${HOME_APP}/logs/GC.log \
                -Dorg.jboss.logging.provider=slf4j \
                -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
                -DAsyncLogger.RingBufferSize=128 \
                -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=DEBUG \
                -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \
                -Dlog4j.configurationFile=file://${HOME_APP}/config/log4j2.xml \
                -Dapp.log.path=${HOME_APP}/logs \
                -Dapp.log.name=producer-rabbit"

EXPOSE 8010

ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS -cp  $HOME_APP/lib/*:$HOME_APP/config/:$HOME_APP/deploy/* com.xabe.broker.kafka.App" ]