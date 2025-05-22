####################################################################################################

### BUILDING & TESTING STAGE ###
FROM node:current-alpine3.20 AS build-stage

RUN apt-get update && \
    apt-get install -y \
    openssl \
    wget

WORKDIR /usr/src/focust-react

COPY package*.json ./
RUN npm install 

COPY . .
RUN npm run build

####################################################################################################

### RUNNING THE BINARY ###
FROM busybox:latest AS run-stage

COPY --from=build-stage ./.certs /etc/ssl/certs
RUN rm -rf ./.certs

WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

EXPOSE 5443 443
ENTRYPOINT ["busybox", "httpd", "-f", "-v", "-p", "5443"]