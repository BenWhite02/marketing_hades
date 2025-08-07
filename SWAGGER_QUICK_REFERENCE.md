# Hades API - Quick Swagger Reference

## Getting Started

1. Start Application:
   cd C:\03Marketing\Hades
   .\gradlew bootRun

2. Access Swagger UI:
   - Main UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

## Authentication Flow

Step 1: Register User
POST /api/v1/auth/register

Step 2: Login
POST /api/v1/auth/login

Step 3: Get Available Apps
GET /api/v1/auth/apps

Step 4: Select App Context
POST /api/v1/auth/apps/{app-id}/select

Step 5: Use Business APIs
GET /api/v1/customers

## API Groups

- Authentication: /api-docs/authentication
- App Management: /api-docs/app-management
- Monitoring: /api-docs/monitoring

## Troubleshooting

If Swagger UI not loading:
- Check application is running on port 8080
- Verify SpringDoc dependencies in build.gradle.kts
- Check for configuration errors in application.yml

For authentication issues:
- Use Authorize button in Swagger UI
- Copy full JWT token including Bearer prefix
- Ensure token has not expired

