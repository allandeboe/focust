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

WORKDIR /usr/src/focust-react
COPY --from=build-stage /usr/src/focust-react/dist .

EXPOSE 5080
ENTRYPOINT ["busybox", "httpd", "-f", "-v", "-p", "5080"]