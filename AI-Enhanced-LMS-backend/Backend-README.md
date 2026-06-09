# AI-Enhanced Learning Management System - Backend

This is the backend service for the **AI-Enhanced Learning Management System**, built using **Java Spring Boot**, **MySQL**, **Spring Security**, **JWT Authentication**, and planned integrations with **AWS S3**, **SerpAPI**, and Docker.

The goal of this backend is to support a complete LMS platform with role-based access for **Admin**, **Teacher**, and **Student** users, along with course management, module management, enrollments, assignments, submissions, grading, analytics, AI-powered recommendations, quiz generation, notifications, and file uploads.

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT Authentication
* MySQL
* Maven
* Lombok
* Jakarta Validation
* WebClient / WebFlux for external API integration
* SerpAPI for AI-based learning recommendations
* AWS S3 for file/resource uploads
* Docker planned for containerized deployment

---

## Current Features Implemented

### Authentication

* User registration
* User login
* JWT token generation
* BCrypt password encryption
* Role-based user model

Supported roles:

```text
ADMIN
TEACHER
STUDENT
```

### Course Management

* Create course
* Get all courses
* Get course by ID
* Update course
* Deactivate course

### Course Module Management

* Create course modules
* Get modules by course
* Get module by ID
* Update module
* Delete module

### Enrollment Management

* Enroll student into a course
* Get enrollments by student
* Get enrollments by course
* Deactivate enrollment
* Auto-create progress record after enrollment

### Assignment Management

* Create assignments
* Get assignments by course
* Get assignment by ID
* Update assignments
* Delete assignments

### Submission Management

* Students can submit assignments
* Supports text submission
* Supports file metadata fields for future AWS S3 integration
* Get submissions by assignment
* Get submissions by student
* Get submission by ID
* Update submission
* Delete submission

### Grading

* Teacher can grade a student submission
* Prevents duplicate grading for the same submission
* Allows grade update
* Calculates grade percentage
* Updates student progress average score
* Supports feedback from teacher

### Analytics

* Course-level summary
* Class average
* Highest score
* Lowest score
* Total assignments
* Total submissions
* Total graded submissions
* Grade distribution data for histogram charts
* Student grade trend data with up/down arrows

### AI Personalized Learning Recommendation

* Planned integration using SerpAPI
* Generates up to 5 high-quality learning recommendations
* Filters out low-trust sources such as Reddit, Quora, forums, and random discussion boards
* Prioritizes official documentation, university resources, and trusted learning platforms
* Implemented SerpAPI-powered personalized learning recommendations with source credibility filtering and ranked scoring to avoid unreliable forum-based resources.

---

## Project Structure

```text
src/main/java/com/prajwalmh/AI_Enhanced/LMS/backend
│
├── controller
│   ├── AuthController.java
│   ├── CourseController.java
│   ├── CourseModuleController.java
│   ├── EnrollmentController.java
│   ├── AssignmentController.java
│   ├── SubmissionController.java
│   ├── GradeController.java
│   └── AnalyticsController.java
│
├── dto
│   ├── request
│   └── response
│
├── entity
│   ├── User.java
│   ├── Role.java
│   ├── Course.java
│   ├── CourseModule.java
│   ├── Enrollment.java
│   ├── Resource.java
│   ├── Assignment.java
│   ├── Submission.java
│   ├── SubmissionStatus.java
│   ├── Grade.java
│   ├── Progress.java
│   ├── DiscussionPost.java
│   ├── DiscussionReply.java
│   ├── Notification.java
│   ├── NotificationType.java
│   ├── AiRecommendation.java
│   ├── Quiz.java
│   ├── QuizQuestion.java
│   └── QuizAttempt.java
│
├── repository
│
├── service
│
├── security
│   └── JwtService.java
│
├── config
│   └── SecurityConfig.java
│
├── exception
│
└── AIEnhancedLmsBackendApplication.java
```

---

## Database

The backend uses **MySQL** as the primary database.

Database name:

```sql
ai_lms_db
```

Example configuration in `application.properties`:

```properties
spring.application.name=AI-Enhanced-LMS-backend

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/ai_lms_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## External API Configuration

For SerpAPI-based AI recommendations:

```properties
serpapi.api.key=YOUR_SERPAPI_KEY
serpapi.base.url=https://serpapi.com/search.json
```

The API key should not be committed directly to GitHub in production. Use environment variables or a secure secret manager for deployment.

---

## Main API Endpoints

### Auth APIs

```http
POST /api/auth/register
POST /api/auth/login
```

### Course APIs

```http
POST   /api/courses
GET    /api/courses
GET    /api/courses/{id}
PUT    /api/courses/{id}
DELETE /api/courses/{id}
```

### Course Module APIs

```http
POST   /api/courses/{courseId}/modules
GET    /api/courses/{courseId}/modules
GET    /api/modules/{moduleId}
PUT    /api/modules/{moduleId}
DELETE /api/modules/{moduleId}
```

### Enrollment APIs

```http
POST   /api/enrollments
GET    /api/enrollments/student/{studentId}
GET    /api/enrollments/course/{courseId}
DELETE /api/enrollments/{enrollmentId}
```

### Assignment APIs

```http
POST   /api/courses/{courseId}/assignments
GET    /api/courses/{courseId}/assignments
GET    /api/assignments/{assignmentId}
PUT    /api/assignments/{assignmentId}
DELETE /api/assignments/{assignmentId}
```

### Submission APIs

```http
POST   /api/assignments/{assignmentId}/submissions
GET    /api/assignments/{assignmentId}/submissions
GET    /api/students/{studentId}/submissions
GET    /api/submissions/{submissionId}
PUT    /api/submissions/{submissionId}
DELETE /api/submissions/{submissionId}
```

### Grade APIs

```http
POST /api/submissions/{submissionId}/grade
GET  /api/students/{studentId}/grades
GET  /api/courses/{courseId}/grades
GET  /api/grades/{gradeId}
PUT  /api/grades/{gradeId}
```

### Analytics APIs

```http
GET /api/analytics/courses/{courseId}/summary
GET /api/analytics/courses/{courseId}/grade-distribution
GET /api/analytics/students/{studentId}/courses/{courseId}/trend
```

### AI Recommendation APIs

```http
POST /api/ai/recommendations/generate
GET  /api/ai/recommendations/student/{studentId}
GET  /api/ai/recommendations/student/{studentId}/course/{courseId}
```

---

## Sample Requests

### Register User

```http
POST http://localhost:8080/api/auth/register
```

```json
{
  "fullName": "John Teacher",
  "email": "teacher@test.com",
  "password": "password123",
  "role": "TEACHER"
}
```

### Login User

```http
POST http://localhost:8080/api/auth/login
```

```json
{
  "email": "teacher@test.com",
  "password": "password123"
}
```

### Create Course

```http
POST http://localhost:8080/api/courses
```

```json
{
  "title": "Java Spring Boot Full Stack Development",
  "description": "Complete backend course covering REST APIs, JPA, JWT security, MySQL, testing, and deployment.",
  "category": "Software Engineering",
  "level": "Intermediate",
  "teacherId": 2
}
```

### Create Assignment

```http
POST http://localhost:8080/api/courses/1/assignments
```

```json
{
  "title": "Spring Boot REST API Assignment",
  "description": "Build REST APIs for authentication, course creation, module management, and assignment workflows.",
  "maxMarks": 100,
  "dueDate": "2026-06-20T23:59:00",
  "published": true,
  "createdById": 2
}
```

### Submit Assignment

```http
POST http://localhost:8080/api/assignments/1/submissions
```

```json
{
  "answerText": "I have completed the REST API assignment.",
  "fileName": "spring-boot-assignment.pdf",
  "fileType": "application/pdf",
  "fileSize": 245760,
  "fileUrl": "https://example.com/spring-boot-assignment.pdf",
  "s3Key": "submissions/student1/spring-boot-assignment.pdf",
  "studentId": 3
}
```

### Grade Submission

```http
POST http://localhost:8080/api/submissions/1/grade
```

```json
{
  "marksObtained": 88,
  "feedback": "Good work. REST API implementation is clear and well structured.",
  "gradedById": 2
}
```

---

## How to Run Locally

### Prerequisites

Make sure the following are installed:

* Java 17
* Maven
* MySQL
* IntelliJ IDEA or VS Code
* Postman for API testing

### Steps

1. Clone the repository.

```bash
git clone https://github.com/your-username/AI-Enhanced-LMS-backend.git
```

2. Navigate into the backend folder.

```bash
cd AI-Enhanced-LMS-backend
```

3. Create MySQL database.

```sql
CREATE DATABASE ai_lms_db;
```

4. Update `application.properties` with your MySQL username and password.

5. Run the application.

```bash
mvn spring-boot:run
```

6. Backend will start on:

```text
http://localhost:8080
```

---

## Testing

APIs are currently tested using Postman.

Planned testing:

* Unit testing with JUnit
* Service layer testing with Mockito
* Controller testing with MockMvc
* Security testing for JWT-protected endpoints
* Integration testing for major LMS workflows

---

## Planned Features

* JWT role-based authorization for Admin, Teacher, and Student routes
* AWS S3 integration for resource uploads and assignment submissions
* AI automatic quiz generation
* Quiz publishing and quiz attempt tracking
* Discussion forum APIs
* Notification APIs
* User management APIs
* Global exception handling
* Dockerfile for backend
* Docker Compose with backend, frontend, and MySQL
* Full API documentation
* Backend test suite
* Deployment guide

---

## Future Improvements

* Use environment variables for secrets
* Add refresh token support
* Add pagination and filtering for large datasets
* Add audit logs for admin actions
* Add real-time notifications using WebSocket
* Add email notifications
* Add advanced recommendation ranking
* Add cloud deployment support

---

## Author

**Prajwal Mrithyunjay Hulamani**

Backend developed as part of a professional full-stack AI-enhanced LMS project using Spring Boot, MySQL, Next.js, AWS S3, Docker, and AI-powered learning features.
