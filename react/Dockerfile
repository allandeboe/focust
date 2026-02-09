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
RUN apk update && \
     apk add --no-cache \
     ca-certificates \
     mini_httpd

COPY ./.certs /usr/local/share/ca-certificates
COPY ./mini_httpd/mini_httpd.conf /etc/mini_httpd/mini_httpd.conf
RUN update-ca-certificates

# should be the same user in `./mini_httpd/mini_httpd.conf`
RUN addgroup -S appgroup && \
     adduser -S react-app \
     -G appgroup \
     -s=/bin/sh
RUN test -d /usr/src/focust-react || mkdir --parents /usr/src/focust-react
RUN chown react-app /usr/src/focust-react
USER react-app

# Copy rendered front-end to this stage
WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

# Only valid ports for HTTPS is 443 and 8443, and port
# 8443 is taken up by the back-end server
EXPOSE 443
HEALTHCHECK --interval=60s CMD wget -qO- https://localhost:443 || exit 1

# Due to how Vite works, we cannot set up HTTPS/SSL with the configs.
# However, mini_httpd does provide a way of setting up HTTPS
# see ./mini_httpd/mini_http.conf
ENTRYPOINT ["mini_httpd", "-C", "/etc/mini_httpd/mini_httpd.conf", "-D"]