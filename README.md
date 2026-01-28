# MediShare - Medicine Donation Bridge

A full-stack application connecting medicine donors with those in need, built as a student-level project with enterprise-grade features.

## 🚀 Project Overview

MediShare is a medicine donation platform that enables:
- Donors to list unused medicines
- Receivers to request needed medicines
- NGOs to bulk receive donations
- Admins to verify listings
- Real-time impact tracking

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Database**: PostgreSQL (production) / H2 (development)
- **Security**: JWT-based authentication
- **ORM**: JPA/Hibernate
- **API Documentation**: Swagger/OpenAPI

## 📋 Features Implemented

- ✅ User authentication and authorization
- ✅ Medicine listing and search
- ✅ Request system for receivers
- ✅ Role-based access control
- ✅ Impact metrics tracking
- ✅ RESTful API design
- ✅ Data validation and security

## 🚀 Getting Started

### Prerequisites
- Java 17
- Maven 3.6+

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd medi-share
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
java -jar target/medi-share-0.0.1-SNAPSHOT.jar
```

> **Note**: If port 8080 is already in use, you can either stop the existing service or configure a different port by setting the `server.port` property.

### Configuration

The application uses H2 in-memory database by default for development. For production, update `application.properties` to use PostgreSQL:

```properties
# Database Configuration (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/medishare
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 🔐 API Endpoints

### Authentication
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/register` - Register

### Medicines
- `GET /api/v1/medicines` - Search medicines
- `POST /api/v1/medicines` - List a medicine (requires authentication)
- `GET /api/v1/medicines/available` - Get all available medicines
- `GET /api/v1/medicines/{medicineId}` - Get medicine by ID
- `POST /api/v1/medicines/{medicineId}/request` - Request a medicine
- `GET /api/v1/medicines/impact` - Get impact metrics

### Users
- `GET /api/v1/users/profile` - Get current user profile (requires authentication)

## 📊 Database Schema

The application uses the following main tables:
- `users` - Stores user information
- `medicines` - Medicine listings
- `donation_requests` - Medicine requests
- `verifications` - Verification records
- `donations_completed` - Completed donations

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/medshare/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Business Logic
│   │   ├── repository/     # Data Access Layer
│   │   ├── model/          # Entity Classes
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configuration Classes
│   │   └── exception/      # Custom Exceptions
│   └── resources/
│       └── application.properties  # Configuration Properties
frontend/
├── public/               # Public assets
├── src/                  # React source files
├── package.json          # Frontend dependencies
└── README.md             # Frontend documentation
```

## 🚀 Deployment

For local development:
- Uses H2 in-memory database
- Runs on port 8080 by default

For production:
- Switch to PostgreSQL
- Configure appropriate security settings
- Set up SSL for production environments

## 🌐 Frontend

The application includes a React-based frontend with:

- User authentication (login/register)
- Medicine browsing interface
- Medicine donation form
- Role-based access control
- Responsive design

To run the frontend:

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will run on `http://localhost:3000` and communicate with the backend API.

## 📄 License

This project is licensed under the MIT License.

---

Built as a capstone project demonstrating full-stack development skills with Spring Boot, security, and database integration.