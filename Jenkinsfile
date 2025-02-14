/**
 * Jenkinsfile - used for Focust's CI/CD pipeline
 *
 * @author  Allan DeBoe (allan.m.deboe@gmail.com)
 * @date    October 10/21/2024
 */
pipeline {

    agent none

    environment {
        DATABASE_VOLUME_NAME = 'mysql-data'
        BACK_END_DATABASE_NETWORK_NAME = 'spring-mysql'
    }

    stages {

        stage("Create Networks & Volumes") {
            agent any
            steps {
                sh 'docker volume inspect ${DATABASE_VOLUME_NAME} || docker volume create ${DATABASE_VOLUME_NAME}'
                sh 'docker network inspect ${BACK_END_DATABASE_NETWORK_NAME} || docker network create ${BACK_END_DATABASE_NETWORK_NAME}'
            }
        }

        stage("Run MySQL Database Container") {
            agent any
            environment {
                MYSQL_DATABASE_CREDENTIALS = credentials('focust-mysql-database')
            }
            steps {
                sh '''
                    docker run -d --name focust-mysql \
                    -e MYSQL_DATABASE=focust_db \
                    -e MYSQL_ROOT_PASSWORD=$MYSQL_DATABASE_CREDENTIALS_PSW \
                    --network ${BACK_END_DATABASE_NETWORK_NAME} \
                    --volume=/root/docker/focust-mysql/conf.d:/etc/mysql/conf.d \
                    --volume=${DATABASE_VOLUME_NAME}:/var/lib/mysql \
                    --restart=always \
                    -p 3307:3306 \
                    mysql:latest
                '''
            }
        }

        stage("Run Back-end Server Container") {
            agent any
            environment {
                MYSQL_DATABASE_CREDENTIALS = credentials('focust-mysql-database')
                SPRING_SECURITY_CREDENTIALS = credentials('focust-mysql-database')

                FOCUST_SPRING_CLIENT_CRT = credentials('focust-spring-client-crt')
                FOCUST_SPRING_CLIENT_KEY = credentials('focust-spring-client-key')

                JWT_RSA_PUBLIC_KEY = credentials('focust-jwt-rsa-public-key')
                JWT_RSA_PRIVATE_KEY = credentials('focust-jwt-rsa-private-key')
            }
            steps {
                withCredentials(bindings: [certificate(aliasVariable: '', \
                                       credentialsId: 'focust-spring-ssl-certificate', \
                                       keystoreVariable: 'SSL_CERTIFICATE_PATH', \
                                       passwordVariable: 'SSL_CERTIFICATE_PSW')]) {
                    dir ('./spring/src/main/resources') {
                        sh 'mkdir .keystore'
                        dir ('./.keystore') {
                            sh 'cp $SSL_CERTIFICATE_PATH ./focust-spring.p12'
                            sh 'cp $FOCUST_SPRING_CLIENT_CRT ./focust-spring-client.crt'
                            sh 'cp $FOCUST_SPRING_CLIENT_KEY ./focust-spring-client.key'
                            sh 'cp $JWT_RSA_PUBLIC_KEY ./public_key.der'
                            sh 'cp $JWT_RSA_PRIVATE_KEY ./private_key.der'
                        }
                    }
                    sh 'mkdir .secrets'
                    dir ('./.secrets') {
                        sh 'echo "$MYSQL_DATABASE_CREDENTIALS_PSW" >> mysql-root'
                        sh 'echo "$SPRING_SECURITY_CREDENTIALS" >> spring-security'
                        sh 'echo "$SSL_CERTIFICATE_PSW" >> ssl-keystore'
                    }
                }
                dir('./spring') {
                    sh '''
                        docker build \
                        --secret "id=MYSQL_ROOT_PASSWORD,src=../.secrets/mysql-root" \
                        --secret "id=SPRING_SECURITY_PASSWORD,src=../.secrets/spring-security" \
                        --secret "id=SSL_KEYSTORE_PASSWORD,src=../.secrets/ssl-keystore" \
                        . -t allandeboe/focust-spring:0.0.4
                    '''
                    sh '''
                        docker run -d --name focust-spring \
                        --network ${BACK_END_DATABASE_NETWORK_NAME} \
                        --restart=always \
                        --volume=/var/run/docker.sock:/var/run/docker.sock \
                        -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
                        -p 8443:8443 \
                        allandeboe/focust-spring:0.0.4
                    '''
                }
            }   
        }

    }

}