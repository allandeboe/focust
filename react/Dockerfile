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
FROM alpine:3.23 AS run-stage

# Copy certificates into this stage
RUN apk update && apk add --no-cache ca-certificates
COPY ./.certs /usr/local/share/ca-certificates
RUN update-ca-certificates

# Copy rendered front-end to this stage
WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

# Only valid ports for HTTPS is 443 and 8443, and port
# 8443 is taken up by the back-end server
EXPOSE 443
HEALTHCHECK CMD wget -qO- https://localhost:443 || exit 1
ENTRYPOINT ["httpd", "-f", "-v", "-p", "443"]