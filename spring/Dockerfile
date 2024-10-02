####################################################################################################

### BUILDING & TESTING STAGE ###
FROM maven:3.9.9-eclipse-temurin-23 AS build-stage

COPY . /usr/src/focust-spring
#COPY ../.secrets/keystore/focust-spring.p12 /usr/src/focust-spring/src/main/resources/keystore

WORKDIR /usr/src/focust-spring/src/main/resources
RUN echo "" > application.properties && \
    echo "### CREDENTIALS ###"
RUN --mount=type=secret,id=MYSQL_ROOT_PASSWORD,env=MYSQL_ROOT_PASSWORD \
    echo "spring.datasource.password = ${MYSQL_ROOT_PASSWORD}" > application.properties
RUN --mount=type=secret,id=SPRING_SECURITY_PASSWORD,env=SPRING_SECURITY_PASSWORD \
    echo "spring.security.user.password = ${SPRING_SECURITY_PASSWORD}" > application.properties
#RUN --mount=type=secret,id=ssl-keystore-password,env=SSL_KEYSTORE_PASSWORD \
#    --mount=type=secret,id=ssl-key-password,env=SSL_KEY_PASSWORD \
#    echo "server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}" > application.properties && \
#    echo "server.ssl.key-password=${SSL_KEY_PASSWORD}" > application.properties

WORKDIR /usr/src/focust-spring
RUN mvn verify

####################################################################################################

### RUNNING STAGE ###
FROM eclipse-temurin:23-jdk
ENV JAR_FILE=api-server-0.0.1-SNAPSHOT.jar

USER root
RUN adduser -D focust-spring && \
    mkdir /focust-spring && \
    chown -R focust-spring /focust-spring
COPY --from=build-stage /usr/src/focust-spring/target/JAR_FILE /focust-spring/JAR_FILE

USER focust-spring
ENTRYPOINT ["java", "-jar", "/api-server-0.0.1-SNAPSHOT.jar"]