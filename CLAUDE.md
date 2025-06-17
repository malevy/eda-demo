# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Building the Project
```bash
# Build all services
mvn clean package

# Build specific service
cd eda-order-service && mvn clean package
cd eda-payment-service && mvn clean package  
cd eda-reservation-service && mvn clean package
```

### Running Tests
```bash
# Run all tests
mvn test

# Run tests for specific service
cd eda-order-service && mvn test
```

### Docker Operations
```bash
# Build containers (requires setting environment variables first)
export active_profile=rabbitmq  # or azure-topic
export azure_topic_connectstring="..."  # if using azure-topic
docker-compose build --no-cache

# Start the system
docker-compose up

# Remove EDA images
docker rmi $(docker images --filter=reference="eda*" -q)
```

### Load Testing
```bash
# Run Gatling load tests (Windows)
winpty bash ./run-gatling-container.sh
```

## Architecture Overview

### Event-Driven Microservices Architecture
- **eda-order-service**: Order management and workflow orchestration (port 8080)
- **eda-payment-service**: Payment processing 
- **eda-reservation-service**: Seat reservation management

### Messaging Infrastructure
- **Message Broker**: RabbitMQ (default) or Azure Service Bus Topics
- **Topic**: `ticketing-events` (single topic with filtered consumers)
- **Consumer Groups**: 
  - `order-processor` (order service)
  - `payment-processor` (payment service) 
  - `reservations-processor` (reservation service)

### Message Patterns
- **Envelope Pattern**: All messages wrapped in `Envelope<T>` with id, timestamp, messageType, payload
- **Message Types**: Versioned message types (e.g., `ticketing.order.created.v1`)
- **Headers**: `message-type` header used for conditional message routing via SpEL expressions
- **Filtering**: Services use `@StreamListener` with condition headers for selective consumption

### Dual Operation Modes
1. **Synchronous**: Traditional request/response via REST (`/orders/sync`)
2. **Asynchronous**: Event-driven workflow via messaging (`/orders`)

### Caching and Resilience
- **Hazelcast**: Distributed caching for order and reservation services
- **Chaos Monkey**: Fault injection for resilience testing
- **Spring Profiles**: Environment-specific configurations (`rabbitmq`, `azure-topic`, `chaos-monkey`)

### Configuration Profiles
- `rabbitmq`: Uses RabbitMQ message broker
- `azure-topic`: Uses Azure Service Bus Topics  
- `chaos-monkey`: Enables fault injection testing

### Environment Variables for Docker
- `active_profile`: Set to `rabbitmq` or `azure-topic`
- `azure_topic_connectstring`: Azure Service Bus connection string (if using azure-topic)
- `chaos_monkey_enabled`: Enable chaos engineering (set to `true`)