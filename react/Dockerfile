####################################################################################################

### BUILDING & TESTING STAGE ###
FROM node:current-alpine3.20 AS build-stage

WORKDIR /usr/src/focust-react

COPY package*.json ./
RUN npm install 

COPY . .
RUN npm run build

####################################################################################################

### RUNNING THE BINARY ###
FROM busybox:latest AS run-stage

#RUN apt-get update && \
#    apt-get install -y \
#    openssl \
#    wget

COPY --from=build-stage /usr/src/focust-react/.certs/focust-react-client.key /etc/ssl/certs/focust-spring-client.key
COPY --from=build-stage /usr/src/focust-react/.certs/focust-react-client.crt /usr/local/share/ca-certificates/focust-spring-client.crt
#RUN update-ca-certificates 

WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

EXPOSE 5443
ENTRYPOINT ["busybox", "httpd", "-f", "-v", "-p", "5443"]