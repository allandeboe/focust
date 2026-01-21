####################################################################################################

### BUILDING & TESTING STAGE ###
FROM node:current-alpine3.20 AS build-stage

WORKDIR /usr/src/focust-react
COPY package*.json ./

COPY ./.certs /etc/ssl/certs
RUN apk update && apk add ca-certificates

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

COPY --from=build-stage /etc/ssl/certs /etc/ssl/certs

WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

EXPOSE 5443
ENTRYPOINT ["busybox", "httpd", "-f", "-v", "-p", "5443"]