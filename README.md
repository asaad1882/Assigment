# student-management

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.daleel.student.ms.App` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Technology used 
- Spring boot is used to build the rest API, it is used in validation, MongoDB handling.
- Lombok is a java library tool that is used to minimize/remove the boilerplate code and save the precious time of developers during development by just using some annotations. In addition to it, it also increases the readability of the source code and saves space. 
- Bucket4j is a Java rate-limiting library based on the token-bucket algorithm. Bucket4j is a thread-safe library that can be used in either a standalone JVM application, or a clustered environment. It also supports in-memory or distributed caching via the JCache (JSR107) specification
- Actuator for Monitoring our app, gathering metrics, understanding traffic, or the state of our database become trivial with this dependency.
- Using Spring Caching to cache client rest request and database query and evict every one hour.
- JaCoCo mainly provides three important metrics: Lines coverage reflects the amount of code that has been exercised based on the number of Java byte code instructions called by the tests.
- for testing using Junit,Mockito 

## Build project
Using the below commands :
- docker-compose up -d  mongo_db
- mvn clean install


## Run App using the below:
- curl -X GET "http://localhost:8090/api/students" -H "accept: application/json" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D"

- curl -X POST "http://localhost:8090/api/students" -H "accept: application/json" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D" -H "Content-Type: application/json" -d "{\"id\":\"string\",\"firstname\":\"string\",\"lastname\":\"string\",\"departmentName\":\"string\"}"
- curl -X GET "http://localhost:8090/api/students/0/1" -H "accept: */*" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D"
 
## Documentation:
http://localhost:8090/swagger-ui/index.html
## Application health
http://localhost:8090/actuator
## Todo
- Using Spring security with different token instead of static
- Integrate JaCoCo with SonarQube 






