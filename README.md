# 🎓 EduForge - Lecturer Management API

EduForge is a Spring Boot-based RESTful API to manage lecturers in an educational platform. It includes CRUD operations, supports multipart file uploads, and allows querying based on lecturer types.

---

## 🛠 Project Evolution

This project was initially developed using **Spring Framework (without Spring Boot)** to manually configure key aspects like:
- Bean creation
- Dependency injection
- Transaction management
- Data source configuration
- Dispatcher servlet and context setup

This manual setup phase provided deep experience and understanding of Spring's core mechanisms.

Gradually, the project evolved:
1. Introduced **Spring AOP** for modularizing cross-cutting concerns like logging and exception handling.
2. Transitioned to **Spring Boot**, leveraging auto-configuration, embedded server, and improved developer productivity.

---

## 🧩 Project Structure

- `controller/` – REST controllers (e.g., `LecturerHttpController`)
- `advice/` - Global Exception handler
- `converter/` - Custom converter for LecturerType
- `exception/` - App Wide Exception for business logic and API
- `service/` – Business logic interface and implementation
- `dto/` – Data Transfer Objects (request/response)
- `util/` – Utility classes and enums (e.g., `LecturerType`)
- `repository/` – Spring Data JPA repositories
- `entity/` – JPA entity classes
- `validation/` - Custom validation annotation for Profile Picture

---

## 🚀 Features

- Add new lecturers with file upload
- **Connects with Firebase Storage to save profile pictures**
- Retrieve, update, and delete lecturer records
- Filter lecturers by type (`full-time` / `visiting`)
- Form and JSON-based data input support
- Bean validation and custom validations
- Centralized exception handling with `@ControllerAdvice`
- Enum converters for request parameter binding

---

## 📡 API Endpoints

### Create a new lecturer
```http
POST /api/v1/lecturers
Content-Type: multipart/form-data
````

### Get lecturer details by ID

```http
GET /api/v1/lecturers/{lecturer-id}
```

### Update lecturer via multipart

```http
PATCH /api/v1/lecturers/{lecturer-id}
Content-Type: multipart/form-data
```

### Update lecturer via JSON

```http
PATCH /api/v1/lecturers/{lecturer-id}
Content-Type: application/json
```

### Delete lecturer

```http
DELETE /api/v1/lecturers/{lecturer-id}
```

### Get all lecturers

```http
GET /api/v1/lecturers
```

### Get full-time lecturers

```http
GET /api/v1/lecturers?type=full-time
```

### Get visiting lecturers

```http
GET /api/v1/lecturers?type=visiting
```

---

## ⚙️ Technologies Used

* Java 22+
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Bean Validation (`jakarta.validation`)
* RESTful APIs
* MySQL / PostgreSQL (configurable)
* Maven

---

## 🛠 How to Run

1. **Clone the repo:**

   ```bash
   git clone https://github.com/vimukthijayasanka/eduforge-backend
   cd eduforge-backend
   ```

2. **Configure `application.properties`:**

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/db_name
    spring.datasource.username=username
    spring.datasource.password=password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.generate-ddl=true
    spring.jpa.show-sql=true
    spring.datasource.hikaricp.maximum-pool-size=10
    spring.servlet.multipart.max-file-size=5MB
    
    application.firebase.storage.bucket=firebase-storage-bucket-name
    application.name=EduForge
    application.version=v1.0.0
    
    #logging.level.root=warn // logging level control
    logging.file.name=logs/app.log
    logging.logback.rollingpolicy.max-history=30
   ```

3. **Run the app:**

   ```bash
   mvn spring-boot:run
   ```

4. **Access the API:**

   ```
   http://localhost:8080/api/v1/lecturers
   ```

---

## 📎 Notes

* Multipart endpoints use `@ModelAttribute` for binding.
* Validations and error responses are handled via standard Spring mechanisms.
* The project can be extended with features like pagination, search, and role-based access control.

---

## 👤 Author

**Vimukthi Jayasanka**
GitHub: [@Vimukthi Jayasnka](https://github.com/vimukthijayasanka)

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](license.txt) file for details.

