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
        FRONT_END_BACK_END_NETWORK_NAME = 'react-spring'
    }

    stages {

        stage("Create Networks & Volumes") {
            agent any
            steps {
                sh 'docker volume inspect ${DATABASE_VOLUME_NAME} || docker volume create ${DATABASE_VOLUME_NAME}'

                sh 'docker network inspect ${BACK_END_DATABASE_NETWORK_NAME} || docker network create ${BACK_END_DATABASE_NETWORK_NAME}'
                sh 'docker network inspect ${FRONT_END_BACK_END_NETWORK_NAME} || docker network create ${FRONT_END_BACK_END_NETWORK_NAME}'
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
                        sh 'test -d .keystore || mkdir .keystore'
                        dir ('./.keystore') {
                            sh 'test -f ./focust-spring.p12 && rm ./focust-spring.p12 || exit 0'
                            sh 'cp $SSL_CERTIFICATE_PATH ./focust-spring.p12'

                            sh 'test -f ./focust-spring-client.crt && rm ./focust-spring-client.crt || exit 0'
                            sh 'cp $FOCUST_SPRING_CLIENT_CRT ./focust-spring-client.crt'
                            sh 'test -f ./focust-spring-client.key && rm ./focust-spring-client.key || exit 0'
                            sh 'cp $FOCUST_SPRING_CLIENT_KEY ./focust-spring-client.key'

                            sh 'test -f ./public_key.der && rm ./public_key.der || exit 0'
                            sh 'cp $JWT_RSA_PUBLIC_KEY ./public_key.der'
                            sh 'test -f ./private_key.der && rm ./private_key.der || exit 0'
                            sh 'cp $JWT_RSA_PRIVATE_KEY ./private_key.der'
                        }
                    }
                    sh 'test -d .secrets || mkdir .secrets'
                    dir ('./.secrets') {
                        sh 'test -f "mysql-root" && rm mysql-root || exit 0'
                        sh 'echo "$MYSQL_DATABASE_CREDENTIALS_PSW" >> mysql-root'

                        sh 'test -f "spring-security" && rm spring-security || exit 0'
                        sh 'echo "$SPRING_SECURITY_CREDENTIALS" >> spring-security'
                        
                        sh 'test -f "spring-ssl-keystore" && rm spring-ssl-keystore || exit 0'
                        sh 'echo "$SSL_CERTIFICATE_PSW" >> spring-ssl-keystore'
                    }
                }
                dir('./spring') {
                    sh '''
                        docker build \
                        --secret "id=MYSQL_ROOT_PASSWORD,src=../.secrets/mysql-root" \
                        --secret "id=SPRING_SECURITY_PASSWORD,src=../.secrets/spring-security" \
                        --secret "id=SSL_KEYSTORE_PASSWORD,src=../.secrets/spring-ssl-keystore" \
                        . -t allandeboe/focust-spring:0.0.5
                    '''
                    sh '''
                        docker run -d --name focust-spring \
                        --network ${BACK_END_DATABASE_NETWORK_NAME} \
                        --network ${FRONT_END_BACK_END_NETWORK_NAME} \
                        --restart=always \
                        --volume=/var/run/docker.sock:/var/run/docker.sock \
                        -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
                        -p 8443:8443 \
                        allandeboe/focust-spring:0.0.5
                    '''
                }
            }   
        }

        stage("Run Front-end Server Container") {
            agent any
            environment {
                FOCUST_REACT_CLIENT_CRT = credentials('focust-react-client-crt')
                FOCUST_REACT_CLIENT_KEY = credentials('focust-react-client-key')
            }
            steps {
                dir('./react') {
                    sh 'test -d .certs || mkdir .certs'
                    dir('./.certs') {
                        sh 'test -f ./focust-react.crt.pem && rm ./focust-react.crt.pem || exit 0'
                        sh 'cp $FOCUST_REACT_CLIENT_CRT ./focust-react.crt.pem'
                        sh 'test -f ./focust-react.key.pem && rm ./focust-react.key.pem || exit 0'
                        sh 'cp $FOCUST_REACT_CLIENT_KEY ./focust-react.key.pem'
                    }
                    sh '''
                        docker build \
                        . -t allandeboe/focust-react:0.0.1
                    '''
                    sh '''
                        docker run -d --name focust-react \
                        --network ${FRONT_END_BACK_END_NETWORK_NAME} \
                        --restart=always \
                        --volume=/var/run/docker.sock:/var/run/docker.sock \
                        -p 443:443 -p 5443:5443 \
                        allandeboe/focust-react:0.0.1
                    '''
                }
            }
        }

    }

}