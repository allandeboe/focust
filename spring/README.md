# Focust - Spring Application
This Spring application is used as the RESTful, back-end server for Focust.

## About
I intended this application to be used in a way that is analogous to how [*Jenkins*](https://www.jenkins.io/) is used in the sense that Focust acts as a stand-alone application running on some allocated company servers and can be accessed by the team working at said company. This influences a lot of design decisions regarding the application.

## Dependencies
The Spring application uses *Java JDK 23*.

| Dependency | Version | Description |
|--- |---|--- |
| **Spring Web** | `3.3.4`[^1] | Used to create the RESTful applications through Spring. |
| **Spring HATEOAS** | `3.3.4`[^1] | Ensures that the REST API follows the [HATEOAS principle](https://en.wikipedia.org/wiki/HATEOAS). |
| **Spring Boot Actuator** | `3.3.4`[^1] | Allows monitoring of the health of the back-end server. |
| **Spring Security** | `3.3.4`[^1] | Used for security-related configuration and functionality. |
| **Spring Data JPA** | `3.3.4`[^1] | Allows use of *Jakarta* (JPA) and [*Hibernate*](https://hibernate.org/) |
| **Lombok** | `1.18.34`[^1] | Reduces common boilerplate code. |
| **Testcontainers** | `1.19.8`[^1] | Used to create a lightweight MySQL database for testing purposes. |
| **MySQL JDBC Driver** | `8.3.0`[^1] | Allows the server to interact with the MySQL database. |
| **JGit** | `7.0.0.202409031743-r` | Allows the application to interact with [git](https://git-scm.com/). |
| **JUnit** | `4.13.2`[^2] | Used to create (unit) tests in Java. Includes [JUnit Jupiter]() |
| **Mockito** | `5.11.0`[^2] | Used to easily create mocks for testing. |

[^1]: `3.3.4` is the version of Spring Boot used; generated as a result of [Spring Initializr](../resources/images/spring-initializr.png).
[^2]: These came with the `spring-boot-starter-test` Maven dependency.