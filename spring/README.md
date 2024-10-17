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
| **Spring Data JPA** | `3.3.4`[^1] | Allows use of JPA and [*Hibernate*](https://hibernate.org/) |
| [**Lombok**](https://projectlombok.org/) | `1.18.34`[^1] | Reduces common boilerplate code. |
| [**Testcontainers**](https://testcontainers.com/) | `1.19.8`[^1] | Used to create a lightweight MySQL database for testing purposes. |
| **MySQL JDBC Driver** | `8.3.0`[^1] | Allows the server to interact with the MySQL database. |
| [**JGit**](https://github.com/eclipse-jgit/jgit) | `7.0.0.202409031743-r` | Allows the application to interact with [git](https://git-scm.com/). |
| [**JUnit 5**](https://junit.org/junit5/) | `4.13.2`[^2] | Used to create (unit) tests in Java. Includes [JUnit Jupiter]() |
| [**Mockito**](https://site.mockito.org/) | `5.11.0`[^2] | Used to easily create mocks for testing. |
| [**Auth0 Java-JWT**](https://github.com/auth0/java-jwt) | `4.4.0` | Used to create, sign, and verify JWT tokens. |
| [**REST-Assured**](https://rest-assured.io/) | `5.5.0` | Used to interact with server endpoints when testing. |

[^1]: `3.3.4` is the version of Spring Boot used; generated as a result of [Spring Initializr](../resources/images/spring-initializr.png).
[^2]: These came with the `spring-boot-starter-test` Maven dependency.

## Security
Focust is an issue tracker web application, and, to assign people with tasks from user stories, users are needed for the application to work as intended. Thus, user security is paramount, hence a lot of steps are taken to ensure the security of this web application:

* have the database store [bcrypt](https://en.wikipedia.org/wiki/Bcrypt) hashes of user passwords, which automatically comes with [salts](https://en.wikipedia.org/wiki/Salt_(cryptography)), ensuring that users with the same password are not all going to be hacked when a hacker uses a rainbow table, as well as a *strength* value which is used to slow down the generation of the hash, slowing the hacker down even more.

* have the application use the [HTTPS](https://en.wikipedia.org/wiki/HTTPS) protocol to ensure that messages are encrypted when sending messages to and from the back-end server, making it difficult to extract user passwords using packet sniffers (like [Wireshark](https://www.wireshark.org/)) when the user logs in or creates a new account. 

* Use data transfer objects (DTOs) to prevent leaking sensitive information to the outside when someone makes a request and causes a data breach. After all, we don't want to repeat what [happened in Missouri](https://arstechnica.com/tech-policy/2021/10/viewing-website-html-code-is-not-illegal-or-hacking-prof-tells-missouri-gov/), do we?

* JWT Access and Refresh Tokens are signed and verified using the [RSA256](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) algorithm, as using an assymetric key algorithm is more secure than a symmetric one by the mere fact that the same secret to decrypt 

## Overview of REST
This section contains a comprehensive overview of the REST commands one can make to the back-end server to be able to interact with it with the need for the front-end server. For all commands, the requests to and responses from the server are all in JSON.

### Authentication & Users
When it comes to authentication, there are primarily only two endpoints that are needed; one to register a new user and another to log in. Both should return JWT Tokens.

| HTTP Method | Endpoint | Description
| --- |--- |--- |
| `POST` | `/auth/register` | Registers a new user, given `email` and `password`. Responds with `jwtToken` containing the JWT Access Token. |
| `POST` | `/auth/login` | Used to sign in a user, given `email` and `password`. Responds with `jwtToken` containing the JWT Access Token. |
| `GET` | `/users` | Used to get the list of all users, 15 at a time, with each entry showing the *id* and *email* of a given user. you can specify a page number by setting the `pageNumber` value in the JSON Request. By Default, the first 15 users are returned. |
| `GET` | `/users/{id}` | Used to get the user with the user id of `{id}`. Returns the id and email of the user.