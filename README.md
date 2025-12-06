# Distribio - E-commerce Network Marketing System

A Spring Boot 3.x backend application implementing a multi-level referral commission system with user activation/deactivation based on sales activity.

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.7**
- **PostgreSQL** (Database)
- **Maven** (Build System)
- **WAR Packaging** (for deployment)

## Features

- User registration with optional referral parent
- Multi-level commission distribution (up to 10 levels)
- Automatic user inactivation after 5 weeks of no sales
- Automatic reactivation when sales occur via inactive user's link
- Referral relationship management with history tracking
- UPI payment flow with transaction ID verification
- Scheduled job for automatic inactivity checks

## Business Rules

### Commission Structure
- Level 0 (Seller): 5%
- Level 1: 10%
- Level 2: 9%
- Level 3: 8%
- Level 4: 7%
- Level 5: 6%
- Level 6: 5%
- Level 7: 4%
- Level 8: 3%
- Level 9: 2%
- Level 10: 1%

### Inactivation Rules
- Users become inactive if they have no sale (direct or via link) in the last **5 weeks**
- When a user becomes inactive:
  - All their direct children are shifted to the inactive user's effective parent
  - History entries are created for tracking
- Inactive users do not receive commissions

### Reactivation Rules
- When a sale occurs via an inactive user's link, that user is automatically reactivated
- All referral shift history entries are reverted
- Previous effective parent relationships are restored

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or Docker for running PostgreSQL)

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE distribio;
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/distribio
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. The application will automatically create tables on first run (using `spring.jpa.hibernate.ddl-auto=update`)

Alternatively, you can manually run the schema from `src/main/resources/schema.sql`.

## Building the Application

### Standard JAR Build
```bash
mvn clean package
```

This will create a JAR file in `target/distribio-0.0.1-SNAPSHOT.jar`

### WAR Build (for deployment)
```bash
mvn clean package
```

This will create a WAR file in `target/distribio-0.0.1-SNAPSHOT.war`

### Including Frontend (Angular) in WAR

1. Build your Angular application:
```bash
cd /path/to/angular-app
ng build --prod
```

2. Copy the Angular dist folder contents to the Spring Boot static resources:
```bash
cp -r dist/angular-app/* src/main/resources/static/
```

3. Rebuild the WAR:
```bash
mvn clean package
```

The WAR file will now include your Angular frontend and can be deployed to a servlet container.

## Running the Application

### Development Mode
```bash
mvn spring-boot:run
```

### Production Mode (JAR)
```bash
java -jar target/distribio-0.0.1-SNAPSHOT.jar
```

### Production Mode (WAR)
```bash
java -jar target/distribio-0.0.1-SNAPSHOT.war
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

Base URL: `http://localhost:8080/api`

### User Management

- **POST** `/api/users` - Register new user
  ```json
  {
    "username": "user1",
    "parentId": 1  // optional
  }
  ```

- **GET** `/api/users` - List all users
- **GET** `/api/users/{id}` - Get user details
- **POST** `/api/users/admin/deactivate/{userId}` - Force deactivate user
- **POST** `/api/users/admin/activate/{userId}` - Force activate user

### Orders

- **POST** `/api/orders` - Create order
  ```json
  {
    "productId": 1,
    "buyerUserId": 2,
    "sellerUserId": 1,
    "paymetProofUrl": "TXN123456",
    "amount": 1000.00
  }
  ```

- **GET** `/api/orders` - List all orders

### Sales

- **POST** `/api/sales` - Create sale
  ```json
  {
    "sellerId": 1,
    "buyerId": 2,
    "totalAmount": 1000.00
  }
  ```

- **GET** `/api/sales` - List all sales

### Admin

- **GET** `/api/admin/ledger` - List commission ledger entries
- **GET** `/api/admin/shift-history` - List referral shift history

## Scheduled Jobs

The application includes a scheduled job that runs **every hour** to check for users who should be marked as inactive (no sales in the last 5 weeks).

## Testing

Run unit tests:
```bash
mvn test
```

The test suite includes:
- Commission calculation tests
- User inactivation/reactivation tests
- Order processing tests

## Project Structure

```
src/
├── main/
│   ├── java/com/rak/distribio/
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Spring Data repositories
│   │   ├── service/        # Business logic services
│   │   └── DistribioApplication.java
│   └── resources/
│       ├── application.properties
│       ├── schema.sql      # Database schema
│       ├── data.sql        # Seed data
│       └── static/         # Frontend assets (Angular dist)
└── test/
    └── java/com/rak/distribio/
        └── service/        # Unit tests
```

## Configuration

Key configuration in `application.properties`:

- Database connection settings
- JPA/Hibernate settings
- Logging levels
- Scheduling (enabled via `@EnableScheduling`)

## Transaction Management

All critical operations use `@Transactional` to ensure data consistency:
- User inactivation/reactivation
- Commission distribution
- Order processing
- Referral relationship updates

Pessimistic locking (`PESSIMISTIC_WRITE`) is used when updating user relationships to prevent race conditions.

## Logging

The application uses SLF4J logging. Key events are logged:
- User registration
- User inactivation/reactivation
- Commission calculations
- Order creation
- Scheduled job execution

## Future Enhancements

- [ ] Authentication and authorization (currently marked as TODO)
- [ ] Support for multiple products (currently single product)
- [ ] Affiliate link generation and tracking
- [ ] Payment gateway integration
- [ ] Email notifications
- [ ] Admin dashboard

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure database `distribio` exists

### Port Already in Use
- Change server port in `application.properties`: `server.port=8081`

### Build Failures
- Ensure Java 17 is installed: `java -version`
- Clean and rebuild: `mvn clean install`

## License

This project is proprietary software.

## Support

For issues or questions, please contact the development team.

