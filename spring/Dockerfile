####################################################################################################

### BUILDING & TESTING STAGE ###
FROM maven:3.9.9-eclipse-temurin-23 AS build-stage

RUN apt-get update && \
    apt-get install -y \
    openssl \
    wget

COPY . /usr/src/focust-spring

WORKDIR /usr/src/focust-spring/src/main/resources
RUN echo "" >> application.properties && \
    echo "### CREDENTIALS ###" >> application.properties
RUN --mount=type=secret,id=MYSQL_ROOT_PASSWORD,target=/root/mysql_root \
    echo "spring.datasource.password = $(cat /root/mysql_root)" >> application.properties
RUN --mount=type=secret,id=SPRING_SECURITY_PASSWORD,target=/root/spring-security \
    echo "spring.security.user.password = $(cat /root/spring-security)" >> application.properties
RUN --mount=type=secret,id=SSL_KEYSTORE_PASSWORD,target=/root/ssl-keystore \
    echo "spring.ssl.bundle.jks.focust-spring.keystore.password = $(cat /root/ssl-keystore)" >> application.properties && \
    keytool -importcert -noprompt -trustcacerts -alias focust-spring \
    -file ./.keystore/focust-spring-client.crt \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass $(cat /root/ssl-keystore)

COPY ./src/main/resources/.keystore/focust-spring-client.crt /usr/local/share/ca-certificates/focust-spring-client.crt
RUN update-ca-certificates 

WORKDIR /usr/src/focust-spring

RUN mkdir ./src/test/resources
RUN cp -a ./src/main/resources/. ./src/test/resources

####################################################################################################

# Building, Testing, & Running the Spring Application is done
# when running the container as the Spring Application uses
# Testcontainers when testing, and it can't access the docker
# daemon while the image is being built.
RUN chmod -x ./run.sh

HEALTHCHECK CMD curl --fail --silent https://localhost:8443/actuator/health | grep UP || exit 1
ENTRYPOINT ["/bin/bash", "./run.sh"]
CMD ["-d"]