# Ejemplo de uso de broker con Java

En este ejemplo vamos usar los broker mas **utilizados** que son:

 - [x] RabbitMq [producer](broker-rabbit-producer/README.md) [consumer](broker-rabbit-consumer/README.md)
 - [x] Kafka **(TODO)**


El stack tecnologico para hacer este ejemplos es el siguiente:

 - [x] Grizzly como servidor NIO
 - [x] Jersey como JAX-RS 
 - [x] Jackson
 - [x] Spring
 - [x] Spring-Amqp
 - [x] Spring-kafka
 
 ## Requisitos
 
 * Maven 3 o superior
 * Java 11 o superior
 * Docker 18.09.0
 * Docker Compose 1.23.2
 
 ## Inicio rápido
  
  ```
  git clone git@github.com:xabe/broker-system.git
  ```
  
 ### Como compilar
 
 
```
mvn clean install -Pdocker
```

### Como arrancar

```

docker-compose up

```


### Como crear un evento

```

curl -X POST -h "content-type: application/json" http://localhost:8008/producer/message

```

### Como consultar los evento

```

curl -X GET -h "content-type: application/json" http://localhost:8009/consumer/message


```