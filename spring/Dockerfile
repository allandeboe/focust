####################################################################################################

### BUILDING & TESTING STAGE ###
FROM maven:3.9.9-eclipse-temurin-23 AS build-stage

COPY . /usr/src/focust-spring

WORKDIR /usr/src/focust-spring/src/main/resources
RUN echo "" >> application.properties && \
    echo "### CREDENTIALS ###" >> application.properties
RUN --mount=type=secret,id=MYSQL_ROOT_PASSWORD,target=/root/mysql_root \
    echo "spring.datasource.password = $(cat /root/mysql_root)" >> application.properties
RUN --mount=type=secret,id=SPRING_SECURITY_PASSWORD,target=/root/spring-security \
    echo "spring.security.user.password = $(cat /root/spring-security)" >> application.properties
RUN --mount=type=secret,id=SSL_KEYSTORE_PASSWORD,target=/root/ssl-keystore \
    echo "server.ssl.key-store-password = $(cat /root/ssl-keystore)" >> application.properties

WORKDIR /usr/src/focust-spring

####################################################################################################

# Building, Testing, & Running the Spring Application is done
# when running the container as the Spring Application uses
# Testcontainers when testing, and it can't access the docker
# daemon while the image is being built.
ENTRYPOINT ["mvn", "spring-boot:test-run"]