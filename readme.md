# EDA - Event Driven Architecture demo

Three services that are composed together to simulate a 
really bad ticketing website. There are two modes that the
demo can be run in:

1. a typical request/response system
2. an EDA configuration that uses RabbitMQ as a message
bus.

#### build and run the demo
> Note: there are opportunities for improvement here via automation

> Requires Java 10

    mvn clean package
    docker-compose build --no-cache
    docker-compose up


#### sample requests

submit an order using the request/response sync configuration

    POST http://localhost:8080/orders/sync
    content-type: application/json
    accept: application/json
    {
      "showId":"456765",
      "status":"pending",
      "seats":["1A","1B","1C"]
    }

retrieve the status of an order 

    GET http://localhost:8080/orders/00001
    accept: application/json

submit an order using the event-driven configuration

    POST http://localhost:8080/orders
    content-type: application/json
    accept: application/json
    
    {
      "showId":"456765",
      "status":"pending",
      "seats":["1A","1B","1C"]
    }

The above request will result in a 201 with the location header
set to the URL of the order.

#### Simulating load
The included script *run-gatling-container.sh* will build and 
and launch a container with an instance of Gatling. There are two 
scripts. One will exercise the request/response path and the other 
will challenge the eventing path

#### Embracing chaos
Netflix's Chaos Monkey has been integrated thanks to 
[Chaos Monkey for Spring Book](https://guides.github.com/pdfs/markdown-cheatsheet-online.pdf)
by CodeCentric. It can be activated by setting the environment variable *chaos_monkey_enabled* 
to *true* prior to spinning up the containers.

    export chaos_monkey_enabled=true



 