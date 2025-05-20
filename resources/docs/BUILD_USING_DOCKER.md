# Focust - Build from Source using Docker
This markdown document is used as a guide for anyone who wants to build Focust using Docker

## MySQL Database
First, for the Spring application to run properly, there has to be a MySQL Database set up, and the easiest way through a creating a MySQL Docker Container.

Run the following commands, where you replace `[PASSWORD]` with the actual password for the MySQL database (we will need to use this password again later.)

```sh
docker create volume mysql-data
docker run -d --name focust-mysql \
    -e MYSQL_DATABASE=focust_db \
    -e MYSQL_ROOT_PASSWORD=[PASSWORD] \
    --network spring-mysql \
    --volume=/root/docker/focust-mysql/conf.d:/etc/mysql/conf.d \
    --volume=mysql-data:/var/lib/mysql \
    --restart=always \
    -p 3307:3306 \
    mysql:latest
```

## Spring Application

### SSL Certificate
First, we need to a SSL certificate with `focust-spring` and `focust-spring.p12` being the *alias* and *keystore* respectively. You can do this by creating a **Self-Signed SSL Certificate**. If you want more information on how to create a Self-Signed SSL Certificate, I recommend the page ["How to Enable HTTPS in Spring Boot Application?"](https://www.geeksforgeeks.org/how-to-enable-https-in-spring-boot-application/) by *GeeksForGeeks*.

I recommend placing the generated `.p12` file into the `./spring/src/main/resources/.keystore` directory. We will also need the password for the keystore later.

Next, we also want to generate a `.crt` and `.key` files (named `focust-spring-client.crt` and `focust-spring-client.key`, respectively) from the `focust-spring.p12` file, also putting them into the `./spring/src/main/resources/.keystore` directory. We can do so rather easily using two [**OpenSSL**](https://en.wikipedia.org/wiki/OpenSSL) commands per file, with `[PASSWORD]` being the password for the `focust-spring.p12` file:

Commands for the `focust-spring-client.crt` file (in the `./spring/src/main/resources/.keystore` directory)
```sh
openssl pkcs12 -in focust-spring.p12 -passin 'pass:[PASSWORD]' -out focust-spring.crt.pem -passout 'pass:[PASSWORD]' -clcerts -nokeys
openssl x509 -in focust-spring.crt.pem -passin 'pass:[PASSWORD]' -out focust-spring-client.crt
```

Commands for the `focust-spring-client.key` file (in the `./spring/src/main/resources/.keystore` directory)
```sh
openssl pkcs12 -in focust-spring.p12 -passin 'pass:[PASSWORD]' -out focust-spring.key.pem -passout 'pass:[PASSWORD]' -nocerts -nodes
openssl pkey -in focust-spring.key.pem -passin 'pass:[PASSWORD]' -out focust-spring-client.key
```

### RSA Public & Private Keys
Since this web application uses **RSA256** to sign and verify JWT Tokens, it is important that you create `.der` files for both the Public Key (`public_key.der`) and the Private Key (`private_key.der`) and put them both into the `./spring/src/main/resources/.keystore` - the same directory when the SSL Certificate lives.

> [!TIP]
> If you are not familiar with creating RSA Public and Private key files for Java applications,
> the article ["RSA Public Key Cryptography"](https://blog.jonm.dev/posts/rsa-public-key-cryptography-in-java/) by *Johnathan T. Moore* will be a good reference.

### Build Docker Image
Now, before we go onto building the container, we need to create specific environment variables that will contain the secrets needed, which are just passwords for the *MySQL Database*, *Spring Security*, and the *SSL Keystore*. create the files `/.secrets/mysql-root`, `/.secrets/spring-security`, and `/.secrets/ssl-keystore` on the main directory to just contain the passwords of each respectively.

After setting that, we can go ahead and run the `docker build` command after going to the `./spring` directory, making sure that we include those secrets.

```sh
docker build \
    --secret "id=MYSQL_ROOT_PASSWORD,src=../.secrets/mysql-root" \
    --secret "id=SPRING_SECURITY_PASSWORD,src=../.secrets/spring-security" \
    --secret "id=SSL_KEYSTORE_PASSWORD,src=../.secrets/ssl-keystore" \
    . -t allandeboe/focust-spring:0.0.5
```

### Run Docker Container
Before going further, make sure that there exists Docker networks called `spring-mysql` and `react-spring`. If not, create it using `docker network create spring-mysql` and `docker network create react-spring`, respectively.

Finally, to run the docker container, we need to make sure we run the following commands:

**For Linux**
```sh
docker run -d --name focust-spring \
    --network spring-mysql \
    --network react-spring \
    --restart=always \
    --volume=/var/run/docker.sock:/var/run/docker.sock \
    --volume=$PWD:$PWD \
    -w $PWD \
    -p 8443:8443 \
    allandeboe/focust-spring:0.0.5
```

**For Windows** (with [*Docker Desktop*](https://www.docker.com/products/docker-desktop/))
```bat
docker run -d --name focust-spring^
    --network spring-mysql^
    --network react-spring^
    --restart=always^
    --volume=/var/run/docker.sock:/var/run/docker.sock^
    -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal^
    -p 8443:8443^
    allandeboe/focust-spring:0.0.5
```

> [!TIP]
> For more information regarding having a docker container load up test containers, I recommend
> reading [*this article*](https://java.testcontainers.org/supported_docker_environment/continuous_integration/dind_patterns/) on the Testcontainers website.

## React Application

### Build Docker Image
To build the docker image, you can run the following command under the `./react` directory:

```sh
docker build \
    . -t allandeboe/focust-react:0.0.1
```

### Run Docker Container
Before going further, make sure that there exists a network called `react-spring`. If not, create it using `docker network create react-spring`. 

Finally, to run the docker container, we need to make sure we run the following commands:

```sh
docker run -d --name focust-react \
    --network react-spring \
    --restart=always \
    --volume=/var/run/docker.sock:/var/run/docker.sock \
    -p 5080:5080 \
    allandeboe/focust-react:0.0.1
```