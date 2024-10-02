# Focust - Build from Source using Docker and Docker Compose
This markdown document is used as a guide for anyone who wants to build Focust using [*Docker Compose*](https://github.com/docker/compose).

## Spring Application

### SSL Certificate (Optional)
First, we need to a SSL certificate with `focust-spring` and `focust-spring.p12` being the *alias* and *keystore* respectively. You can do this by creating a **Self-Signed SSL Certificate**. If you want more information on how to create a Self-Signed SSL Certificate, I recommend the page ["How to Enable HTTPS in Spring Boot Application?"](https://www.geeksforgeeks.org/how-to-enable-https-in-spring-boot-application/) by *GeeksForGeeks*.

I recommend placing the generated `.p12` file into a hidden directory `.secrets` in the main project directory.

> [!IMPORTANT]
> changing the alias and keystore of the SSL Certificate should be reflected in the `./spring/src/main/resources/application.properties` file with the properties `server.ssl.key-store-alias` and `server.ssl.key-store`, respectively. Note that the `server.ssl.key-store` property needs to accept a value of `classpath:/keystore/[KEYSTORE]`, where `[KEYSTORE]` is the keystore value.

### Building Docker Container
Now, before we go onto building the container, we need to create specific environment variables that will contain the secrets needed, which are just passwords for the *MySQL Database* and *Spring Security*

```sh
export MYSQL_ROOT_PASSWORD="..."
export SPRING_SECURITY_PASSWORD="..."
```

After setting that, we can go ahead and run the `docker build` command, making sure that we include those secrets.

```sh
docker build -t allandeboe/focust-spring --secret id=MYSQL_ROOT_PASSWORD --secret id=SPRING_SECURITY_PASSWORD .
```

