# EDA - Event Driven Architecture demo

Three services that are composed together to simulate a 
really bad ticketing website. There are two modes that the
demo can be run in:

1. a typical request/response system
1. an EDA configuration that uses either RabbitMQ or Azure ServiceBus Topics.


#### configuring Azure ServiceBus Topic

> Note: there are opportunities for improvement here via automation

Using this demo solution with Azure requires that you have configured the appropriate resources.
1. you must have an Azure Subscription
1. create a Resource Group to contain the ServiceBus Namespace
1. create a Service Bus Namespace
    1. the Premium tier or higher is required 
1. create a Topic within the namespace
1. create a Shared Access Policy (SAP) with the Send and Listen claims, at a minimum.
    1. you will need to supply one of the generated connection strings as shown below.
1. create three subscriptions against the topic. these subscriptions are named after the
service that will make use of them.
    1. order-processor
    1. reservation-processor
    1. payment-processor

#### build and run the demo

> the environment variable 'active_profile' needs to be set to either
>       'rabbitmq' to use RabbitMQ or
>       'azure-topic' for Azure ServiceBus Topic

> the environment variable 'azure_topic_connectstring' should be set to one of the
> connection strings that were generated when you created the SAP above. I am using a single
> connection string for all the services for simplicity. a better practice would be for each 
> service to have it's own access policy

    mvn clean package
    export active_profile=azure-topic
    export azure_topic_connectstring=...
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

on Windows

    winpty bash ./run-gatling-container.sh

#### cleanup
deleting the Azure Resource Group is a quick and easy way to release all of the resources that 
were created within Azure.

an easy way to remove all of the EDA images

     docker rmi $(docker images --filter=reference="eda*" -q)


 