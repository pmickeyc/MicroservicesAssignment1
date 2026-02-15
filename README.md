# Sales API

Spring Boot REST API for `Customer -> SalesOrder` management.

## Stack
- Java 25
- Spring Boot 4.0.2
- Spring Web + Spring Data JPA + Validation
- H2 (in-memory)

## Run
```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.2'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd spring-boot:run
```

## Test
```powershell
.\mvnw.cmd test
```

## API
Base path: `/api/sales/v1`

Customers:
- `GET /customers`
- `GET /customers/{customerId}`
- `POST /customers`
- `PUT /customers/{customerId}`
- `DELETE /customers/{customerId}`

Orders (nested under customer):
- `GET /customers/{customerId}/orders`
- `GET /customers/{customerId}/orders/{orderId}`
- `POST /customers/{customerId}/orders`
- `PUT /customers/{customerId}/orders/{orderId}`
- `DELETE /customers/{customerId}/orders/{orderId}`

Order list filters:
- `from-date`, `to-date` (`yyyy-MM-dd`)
- `status`
- `min-amount`, `max-amount`
- `page`, `size`, `sort`

Customer profile fields (optional on create/update):
- `phoneNumber`
- `billingAddress`
- `shippingAddress`

## H2
- Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:salesdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- User: `sa`
- Password: empty
- Seed data: `src/main/resources/data.sql`

## Response conventions
- Pagination: `content`, `page`, `size`, `totalElements`, `totalPages`, `last`
- Error payload: `timestamp`, `status`, `error`, `message`, `path`, `fieldErrors`
