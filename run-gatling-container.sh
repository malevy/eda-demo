#!/bin/bash
docker run -it \
    --rm \
    --mount type=bind,src=$(pwd)/gatling/conf,dst=/opt/gatling/conf \
    --mount type=bind,src=$(pwd)/gatling/results,dst=/opt/gatling/results \
    --mount type=bind,src=$(pwd)/gatling/user-files,dst=/opt/gatling/user-files \
    --env HOSTNAME=$(HOSTNAME) \
    --env HOSTPORT=8080 \
    denvazh/gatling:latest
