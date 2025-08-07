# Hades Backend - Kairos Marketing Platform

## Quick Start

### Prerequisites
- JDK 17+
- PostgreSQL 15+
- Redis 7+ (optional, can use Docker)
- RabbitMQ 3.12+ (optional, can use Docker)

### Setup
1. Install PostgreSQL and create databases:
   ```sql
   CREATE DATABASE kairos_db;
   CREATE DATABASE kairos_dev_db;
   CREATE USER kairos_user WITH PASSWORD 'kairos_pass';
   GRANT ALL PRIVILEGES ON DATABASE kairos_db TO kairos_user;
   GRANT ALL PRIVILEGES ON DATABASE kairos_dev_db TO kairos_user;
   ```

2. Start Redis and RabbitMQ (using Docker):
   ```bash
   docker run -d --name kairos-redis -p 6379:6379 redis:7-alpine
   docker run -d --name kairos-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management-alpine
   ```

3. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

### Health Checks
- Application: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

### Current Status
Block 1: Foundation and Project Setup - COMPLETE

### Next Steps
- Block 2: Multi-tenancy foundation
- Block 3: JWT authentication system
