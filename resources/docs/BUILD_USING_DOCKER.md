# Focust - Build from Source using Docker and Docker Compose
This markdown document is used as a guide for anyone who wants to build Focust using [*Docker Compose*](https://github.com/docker/compose).

## Spring Application

### Setting up MySQL Database
First, for the Spring application to run properly, there has to be a MySQL Database set up, and the easiest way through a creating a MySQL Docker Container.

Run the following commands, where you replace `[PASSWORD]` with the actual password for the MySQL database (we will need to use this password again later.)

```sh
docker create volume mysql-data
docker run -d --name focust-mysql \
    -e MYSQL_DATABASE=focust_db \
    -e MYSQL_ROOT_PASSWORD=[PASSWORD] \
    --network spring-mysql \
    --restart=always \
    -p 3307:3306 \
    -v mysql-data \
    mysql:latest
```

### SSL Certificate
First, we need to a SSL certificate with `focust-spring` and `focust-spring.p12` being the *alias* and *keystore* respectively. You can do this by creating a **Self-Signed SSL Certificate**. If you want more information on how to create a Self-Signed SSL Certificate, I recommend the page ["How to Enable HTTPS in Spring Boot Application?"](https://www.geeksforgeeks.org/how-to-enable-https-in-spring-boot-application/) by *GeeksForGeeks*.

I recommend placing the generated `.p12` file into a hidden directory `.secrets` in the main project directory.

> [!IMPORTANT]
> changing the alias and keystore of the SSL Certificate should be reflected in the `./spring/src/main/resources/application.properties` file with the properties `server.ssl.key-store-alias` and `server.ssl.key-store`, respectively. Note that the `server.ssl.key-store` property needs to accept a value of `classpath:/keystore/[KEYSTORE]`, where `[KEYSTORE]` is the keystore value.

### Build Docker Image
Now, before we go onto building the container, we need to create specific environment variables that will contain the secrets needed, which are just passwords for the *MySQL Database* and *Spring Security*. create the files `/.secrets/mysql-root` and `/.secrets/spring-security` on the main directory to just contain the passwords of each respectively.

After setting that, we can go ahead and run the `docker build` command after going to the `./spring` directory, making sure that we include those secrets.

```sh
docker build \
    --secret "id=MYSQL_ROOT_PASSWORD,src=../.secrets/mysql-root" \
    --secret "id=SPRING_SECURITY_PASSWORD,src=../.secrets/spring-security" \
    . -t allandeboe/focust-spring
```

### Run Docker Container
Before going further, make sure that there exists a network called `spring-mysql`. If not, create it using `docker network create spring-mysql`. 

Finally, to run the docker container, we need to make sure we run the following command, not forgetting to include 

```sh
docker run -d --name focust-spring \
    --network spring-mysql \
    --restart=always \
    --volume /var/run/docker.sock:/var/run/docker.sock \
    -p 8080:8443 \
    allandeboe/focust-spring
```