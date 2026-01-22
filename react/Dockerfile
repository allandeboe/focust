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

# Only valid ports for HTTPS is 443 and 8443, and port
# 8443 is taken up by the back-end server
EXPOSE 443
ENTRYPOINT ["busybox", "httpd", "-f", "-v", "-p", "443"]