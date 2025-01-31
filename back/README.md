# Yoga App !

This projet is the back part of a global project.   
To see the global information about this projet, go [there](../README.md) !

## How to install and run the project

This project need a MySQL database.  
To install the project, you add environmental variables in your configuration :
- DATABASE_USERNAME as your database username
- DATABASE_PASSWORD as your database password

`mvn clean install` allows you to install the project.  
`mvn spring-boot:run` allows you to launch the back-end. The main method is [SpringBootSecurityJwtApplication](./src/main/java/com/openclassrooms/starterjwt/SpringBootSecurityJwtApplication.java).

Running the project generates the necessary tables in the database and allows to use the necessary endpoints of the application.

The server run on the port 8080.

## How to run the test

[Mocked data](./src/main/resources/data-test.sql) is used for the integration tests.

`mvn clean test` allows you to launch and generate the jacoco code coverage.   
Report is available in [target/site/jacoco/index.html](./target/site/jacoco/index.html) after project installation. Lauch the html page to display the visual report.