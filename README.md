# DbMongo
## Back end for a client/server architecture in a project university for Apollon project

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![License](https://img.shields.io/badge/license-tomcat-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Server app for Apollon prject. It agglomerates geographic using mongodb queries and create a set of Rest Api to obtain specific data.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 5](https://maven.apache.org)
- [MongoDB 4](https://www.mongodb.com) 

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `it.unisalento.db.crud.DbMongo.configurations.DbMongoApplication` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html) like so:

```shell
gradle bootRun
```

[Sharding.zip](https://github.com/anto6715/DbMongo/blob/master/Sharding.zip) contains config file to deploy a distribuited Mongodb databases and some instruction used to create a distributed collection for tests. It is recommended refer to the official [guide](https://docs.mongodb.com/manual/sharding/)

## Authors

* **Antonio Mariani** - *Initial work* 
* **Giuseppe Morleo** - *Contributors* 
