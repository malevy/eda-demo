#!/bin/bash
docker run -it \
    --rm \
    --mount type=bind,src=$(pwd)/gatling/conf,dst=/opt/gatling/conf \
    --mount type=bind,src=$(pwd)/gatling/results,dst=/opt/gatling/results \
    --mount type=bind,src=$(pwd)/gatling/user-files,dst=/opt/gatling/user-files \
    --env HOSTNAME=docker.for.win.localhost \
    --env HOSTPORT=8080 \
    denvazh/gatling:latest

# on Mac, set the HOSTNAME to docker.for.mac.localhost