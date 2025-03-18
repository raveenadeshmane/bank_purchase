# Purchase Approval System

A Spring Boot-based microservice for evaluating customer purchase requests and determining approval decisions.

## Features

- **Purchase Approval Checks**: Evaluate customer eligibility and financial capacity
- **Dynamic Payment Adjustment**: Automatically adjust payment periods for optimal approval
- **Mock Customer Profiles**: Predefined financial profiles for testing
  - `12345678901`: Ineligible
  - `12345678912`: Profile 1 (Factor 50)
  - `12345678923`: Profile 2 (Factor 100)
  - `12345678934`: Profile 3 (Factor 500)
- **Validation Constraints**: 
  - Amount: €200-€5000
  - Payment Period: 6-24 months
- **API Documentation**: Integrated Swagger UI/OpenAPI
- **Docker Support**: Containerized deployment
- **Monitoring**: Prometheus metrics endpoint

## Technologies

- **Java 21**
- **Spring Boot 3.2**
- Spring Web
- Spring Validation
- SpringDoc OpenAPI
- Docker
- Maven
- Spring Security
- Lombok
- JUnit 5/Mockito

## Installation

### Prerequisites
- Java 21
- Maven 3.9+
- Docker (optional)

## Start Docker

1. Switch to directory

   ``
   docker/dev
   ``

2. Build docker images

   ``
   docker-compose build
   ``

3. Start application

   ``
   docker-compose up -d
   ``

### Local Setup
```bash
git clone https://github.com/yourrepo/purchase-approval.git
cd purchase-approval
mvn clean install
java -jar target/purchase-approval-1.0.0.jar

