# student-management
Requirements
For building and running the application you need:

* JDK 1.8
* Maven 3
## Running the application locally
There are several ways to run a Spring Boot application on your local machine. 
* One way is to execute the main method in the com.daleel.student.ms.App class from your IDE.

* Alternatively you can use the Spring Boot Maven plugin like so:

   * mvn spring-boot:run
## Technology used
* Spring boot is used to build the rest API, it is used in validation, MongoDB handling.
* Actuator for Monitoring our app, gathering metrics, understanding traffic, or the state of our database become trivial with this dependency.
* JaCoCo mainly provides three important metrics: Lines coverage reflects the amount of code that has been exercised based on the number of Java byte code instructions called by the tests.
* for testing using Junit,Mockito
* JBucket is used for rate limit
## Build project Using the below commands :##

docker-compose up -d mongo_db
mvn clean install
### Run App using the below:
#### Create Student
curl -X POST "http://localhost:8090/api/students" -H "accept: */*" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D" -H "Content-Type: application/json" -d "{\"id\":\"string\",\"firstname\":\"string\",\"lastname\":\"string\",\"departmentName\":\"string\"}"

#### List students:
curl -X GET "http://localhost:8090/api/students" -H "accept: */*" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D"

#### List Student paginated
curl -X GET "http://localhost:8090/api/students/0/5" -H "accept: */*" -H "X-API-KEY: 184DA27F6D8E9181EB44DA79A983D"

### Documentation:
http://localhost:8090/swagger-ui/index.html

### Application health
http://localhost:8090/actuator

### Todo
* Using Spring security with different token instead of static
* Integrate JaCoCo with SonarQube
