# Learning-Management-System

## TOPIC REQUIREMENT

Learning Management System (LMS)

• User Roles – Admin, Teacher, Student roles with different access.

• Course Management – Create and manage courses and modules.

• Resource Upload – Upload PDFs, videos, and other learning materials.

• Assignment Submission – Students can submit assignments online.

• Grading Dashboard – Teachers can grade and track student performance.

• Progress Tracking – Monitor student completion and performance.

• Discussion Forum – Students can discuss topics/questions.

• Notifications – Alerts for new assignments, deadlines, and grades.

• AI Features:

• AI – Personalized Learning – Suggest resources based on student performance.

• AI – Automatic Quiz Generation – Generate quizzes automatically from course materials.



# AI-Enhanced Learning Management System

A full-stack **AI-Enhanced Learning Management System** built using **Java Spring Boot**, **MySQL**, **Next.js**, **AWS S3**, **SerpAPI**, and **Docker**.

This project is designed as a professional end-to-end LMS platform with role-based access for **Admin**, **Teacher**, and **Student** users. It includes course management, module management, resource uploads, assignment submissions, grading, analytics, discussion forums, notifications, AI-powered learning recommendations, and AI-based quiz generation.

---

## Project Overview

The AI-Enhanced Learning Management System is a full-stack web application that helps educational institutions manage courses, students, teachers, assignments, grades, learning resources, and student performance.

The system includes AI-powered features to improve student learning:

* Personalized learning recommendations based on student performance
* Automatic quiz generation from course materials
* Performance analytics with student marks trends
* Grade distribution histogram
* Student progress tracking
* Up/down trend arrows for assignment performance

The project is built to demonstrate professional full-stack development skills including backend API design, frontend dashboard development, database modeling, authentication, cloud file upload, AI integration, testing, Dockerization, and documentation.

---

## Key Features

### User Roles

The system supports three main roles:

* Admin
* Teacher
* Student

Each role has different access permissions and dashboard views.

---

### Admin Features

* Manage users
* Manage teachers and students
* Create and manage courses
* Assign teachers to courses
* View platform-level data
* Manage system-level operations

---

### Teacher Features

* Create and manage courses
* Add course modules
* Upload learning resources
* Create assignments
* View student submissions
* Grade assignments
* Provide feedback
* View student performance analytics
* View grade distribution histogram
* View student progress trends
* Generate AI-based quizzes
* Manage discussions and notifications

---

### Student Features

* Register and log in
* View enrolled courses
* Access course modules
* View learning resources
* Submit assignments
* View grades and feedback
* Track course progress
* View personalized AI recommendations
* Attempt AI-generated quizzes
* Participate in discussion forums
* Receive notifications for deadlines, grades, and new resources

---

## AI Features

### AI Personalized Learning Recommendations

The LMS uses **SerpAPI** to generate personalized learning recommendations based on student performance.

If a student performs poorly in a topic, the system generates up to **5 high-quality learning recommendations**.

The recommendation engine:

* Uses SerpAPI to search learning resources
* Filters out low-trust sources such as Reddit, Quora, forums, and random discussion boards
* Prioritizes official documentation, university pages, trusted tutorials, and learning platforms
* Scores each recommendation based on source credibility and topic relevance
* Returns the top 5 best-scored recommendations

Implemented SerpAPI-powered personalized learning recommendations with source credibility filtering and ranked scoring to avoid unreliable forum-based resources.

---

### AI Automatic Quiz Generation

The system will support automatic quiz generation from course material or module content.

Planned quiz generation features:

* Generate MCQs from course topics
* Generate questions from uploaded resource text
* Allow teachers to review generated quizzes
* Publish quizzes to students
* Track quiz attempts and scores

---

## Analytics Features

The LMS includes professional analytics for teachers and students.

### Teacher Analytics

* Course average score
* Highest score
* Lowest score
* Total assignments
* Total submissions
* Total graded submissions
* Grade distribution histogram
* Weak student identification
* Assignment-wise performance
* Student-wise performance trends

### Student Analytics

* Assignment score trend
* Up/down arrows for performance changes
* Course progress percentage
* Average score
* Weak topic recommendations
* Quiz performance history

Example trend:

```text
Assignment 1: 65
Assignment 2: 78 ↑
Assignment 3: 70 ↓
Assignment 4: 90 ↑
```

---

## Tech Stack

### Backend

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
* WebClient / WebFlux
* SerpAPI
* AWS S3
* JUnit
* Mockito
* MockMvc

### Frontend

* Next.js
* React
* TypeScript
* Tailwind CSS
* Axios
* Recharts / Chart.js
* JWT-based protected routes

### Database

* MySQL local database

### Cloud and Storage

* AWS S3 for resource uploads and assignment submissions

### DevOps

* Docker
* Docker Compose

---

## Project Architecture

```text
                 ┌─────────────────────────┐
                 │       Next.js UI         │
                 │ Admin / Teacher / Student│
                 └────────────┬────────────┘
                              │
                              │ REST API + JWT
                              │
                 ┌────────────▼────────────┐
                 │   Spring Boot Backend    │
                 │ Security + Business Logic│
                 └────────────┬────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐     ┌────────────────┐     ┌────────────────┐
│    MySQL     │     │     AWS S3      │     │  AI Services    │
│ User/Course  │     │ PDFs/Videos/    │     │ SerpAPI + Quiz │
│ Assignment   │     │ Submissions     │     │ Generation     │
└──────────────┘     └────────────────┘     └────────────────┘
```

---

## Repository Structure

```text
AI-Enhanced-LMS
│
├── backend
│   └── Spring Boot backend application
│
├── frontend
│   └── Next.js frontend application
│
├── docs
│   ├── system-design.md
│   ├── api-documentation.md
│   ├── database-schema.md
│   ├── testing-report.md
│   └── deployment-guide.md
│
├── docker-compose.yml
└── README.md
```

---

## Backend Structure

```text
backend/src/main/java/com/prajwalmh/AI_Enhanced/LMS/backend
│
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── repository
├── service
├── security
├── config
├── exception
└── AIEnhancedLmsBackendApplication.java
```

---

## Core Backend Modules

* Authentication
* User Management
* Course Management
* Course Module Management
* Enrollment Management
* Resource Upload
* Assignment Management
* Submission Management
* Grading
* Progress Tracking
* Analytics
* Discussion Forum
* Notifications
* AI Recommendations
* AI Quiz Generation
* Quiz Attempts

---

## Database Tables

Main database tables include:

```text
users
courses
course_modules
enrollments
resources
assignments
submissions
grades
progress
discussion_posts
discussion_replies
notifications
ai_recommendations
quizzes
quiz_questions
quiz_attempts
```

---

## Main API Endpoints

### Authentication APIs

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

## Backend Setup

### Prerequisites

Install the following:

* Java 17
* Maven
* MySQL
* IntelliJ IDEA or VS Code
* Postman

### Step 1: Clone Repository

```bash
git clone https://github.com/your-username/AI-Enhanced-LMS.git
cd AI-Enhanced-LMS
```

### Step 2: Create MySQL Database

```sql
CREATE DATABASE ai_lms_db;
```

### Step 3: Configure Backend

Open:

```text
backend/src/main/resources/application.properties
```

Add or update:

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

serpapi.api.key=YOUR_SERPAPI_KEY
serpapi.base.url=https://serpapi.com/search.json
```

### Step 4: Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

---

## Frontend Setup

### Prerequisites

Install:

* Node.js
* npm

### Step 1: Navigate to Frontend

```bash
cd frontend
```

### Step 2: Install Dependencies

```bash
npm install
```

### Step 3: Run Frontend

```bash
npm run dev
```

Frontend runs at:

```text
http://localhost:3000
```

---

## Environment Variables

For production, sensitive values should be moved to environment variables.

Recommended variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
SERPAPI_API_KEY
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
AWS_S3_BUCKET_NAME
```

---

## Sample API Requests

### Register Teacher

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

### Register Student

```http
POST http://localhost:8080/api/auth/register
```

```json
{
  "fullName": "Student One",
  "email": "student1@test.com",
  "password": "password123",
  "role": "STUDENT"
}
```

### Login

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

### Generate AI Recommendations

```http
POST http://localhost:8080/api/ai/recommendations/generate
```

```json
{
  "studentId": 3,
  "courseId": 1,
  "weakTopic": "Spring Security JWT Authentication",
  "score": 55
}
```

---

## Testing Plan

### Backend Testing

Planned backend testing includes:

* Unit tests with JUnit
* Service layer tests with Mockito
* Controller tests with MockMvc
* Authentication tests
* Role-based access tests
* Integration tests for major LMS workflows

### Frontend Testing

Planned frontend testing includes:

* Component tests
* Dashboard rendering tests
* Login form tests
* API integration tests
* Chart rendering tests

---

## Docker Plan

The project will include Docker support.

Planned files:

```text
backend/Dockerfile
frontend/Dockerfile
docker-compose.yml
```

Services:

```text
mysql
backend
frontend
```

Expected command:

```bash
docker compose up --build
```

---

## Current Development Status

Implemented backend features:

* Authentication
* JWT generation
* User roles
* Course APIs
* Course module APIs
* Enrollment APIs
* Assignment APIs
* Submission APIs
* Grade APIs
* Analytics APIs
* AI recommendation structure

In progress / planned:

* SerpAPI final integration
* AWS S3 resource upload
* Discussion forum APIs
* Notification APIs
* AI quiz generation
* Quiz attempt APIs
* Frontend dashboards
* Docker setup
* Full test suite
* Final documentation and report

---

## Future Enhancements

* Full JWT role-based authorization
* Refresh token support
* Global exception handling
* Pagination and filtering
* AWS S3 file upload
* Email notifications
* Real-time notifications using WebSocket
* AI quiz generation from uploaded resources
* Advanced performance analytics
* Student weakness heatmap
* Admin dashboard
* Dockerized deployment
* Cloud deployment

---

## Portfolio Summary

Built a full-stack AI-enhanced Learning Management System using **Spring Boot**, **MySQL**, **Next.js**, **AWS S3**, **Docker**, and **AI-powered learning features**.

The platform supports role-based dashboards for Admin, Teacher, and Student users, secure authentication, course and assignment management, grading workflows, student progress tracking, analytics with grade trends and histograms, SerpAPI-powered personalized recommendations, and planned automatic quiz generation.

---

## Author

**Prajwal Mrithyunjay Hulamani**

Graduate Computer Science student at The University of Texas at Arlington.

Portfolio: `https://prajwalmrithyunjayhulamani.netlify.app/`

GitHub: `[https://github.com/your-username](https://github.com/PrajwalMH)`



user passwords

Email: student1@test.com
Password: password123