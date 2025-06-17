# EDA - Event Driven Architecture demo

Three services that are composed together to simulate a
really bad ticketing website. There are two modes that the
demo can be run in:

1. a typical request/response system
1. an EDA configuration that uses RabbitMQ.

#### build and run the demo

```bash
mvn clean package
docker compose build --no-cache
docker compose up --wait
```

#### sample requests

submit an order using the request/response sync configuration
```http request
POST http://localhost:8080/orders/sync
content-type: application/json
accept: application/json
{
  "showId":"456765",
  "status":"pending",
  "seats":["1A","1B","1C"]
}
```

retrieve the status of an order 
```http request
GET http://localhost:8080/orders/00001
accept: application/json
```

submit an order using the event-driven configuration
```http request
POST http://localhost:8080/orders
content-type: application/json
accept: application/json

{
  "showId":"456765",
  "status":"pending",
  "seats":["1A","1B","1C"]
}
```

The above request will result in a 201 with the location header
set to the URL of the order.

#### cleanup
An easy way to remove all of the EDA images
```bash
docker rmi $(docker images --filter=reference="eda*" -q)
```

 