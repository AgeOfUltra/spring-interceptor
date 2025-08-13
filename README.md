# Spring Boot Interceptors and Filters Demo

## Overview
This project demonstrates the implementation of **Spring Boot Interceptors** in a job application management system. The application showcases how interceptors work in the Spring MVC architecture to provide cross-cutting concerns like logging, authentication, and request/response monitoring.

## Spring Boot Interceptors vs Filters Theory

### What are Interceptors?
Spring Interceptors are components that allow you to intercept HTTP requests and responses in a Spring MVC application. They provide a way to implement cross-cutting concerns such as logging, security, auditing, and performance monitoring at the web layer.

### What are Filters?
Servlet Filters are components that operate at the servlet container level, intercepting requests and responses before they reach the Spring framework. They are part of the Java EE specification and work at a lower level than Spring Interceptors.

## Interceptors vs Filters Comparison

| Aspect | Spring Interceptors | Servlet Filters |
|--------|-------------------|-----------------|
| **Level** | Spring MVC Framework Level | Servlet Container Level |
| **Execution Order** | After Filters, before Controllers | Before Spring Framework |
| **Spring Context Access** | Full access to Spring beans and context | Limited Spring context access |
| **Handler Access** | Can access the controller method | Cannot access handler details |
| **URL Patterns** | More flexible path patterns | Basic URL patterns |
| **Exception Handling** | Better integration with Spring exception handling | Limited exception handling |
| **Configuration** | Spring-based configuration | web.xml or @WebFilter |

## Request Processing Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        HTTP Request Processing Flow                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────┐    ┌──────────────┐    ┌─────────────────────────────────┐ │
│  │             │    │              │    │                                 │ │
│  │   Client    │───▶│   Filter     │───▶│       Spring Context           │ │
│  │ (Browser)   │    │  (Optional)  │    │                                 │ │
│  └─────────────┘    └──────────────┘    └─────────────────────────────────┘ │
│                              │                           │                   │
│                              ▼                           ▼                   │
│                     ┌──────────────┐            ┌──────────────┐            │
│                     │              │            │              │            │
│                     │DispatcherServlet│──────▶│ Interceptor  │            │
│                     │              │            │  preHandle() │            │
│                     └──────────────┘            └──────────────┘            │
│                              │                           │                   │
│                              ▼                           ▼                   │
│                     ┌──────────────┐            ┌──────────────┐            │
│                     │              │            │              │            │
│                     │  Controller  │◀───────────│ Interceptor  │            │
│                     │   Handler    │            │ postHandle() │            │
│                     └──────────────┘            └──────────────┘            │
│                              │                           │                   │
│                              ▼                           ▼                   │
│                     ┌──────────────┐            ┌──────────────┐            │
│                     │              │            │              │            │
│                     │   Response   │◀───────────│ Interceptor  │            │
│                     │   Rendering  │            │afterCompletion()│         │
│                     └──────────────┘            └──────────────┘            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Interceptor Lifecycle

```
┌─────────────────────────────────────────────────────────────────┐
│                    Interceptor Execution Lifecycle              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. ┌─────────────────┐                                         │
│     │   preHandle()   │ ── Called before controller execution   │
│     │                 │    Returns: boolean (continue/stop)     │
│     └─────────────────┘                                         │
│              │                                                  │
│              ▼                                                  │
│  2. ┌─────────────────┐                                         │
│     │   Controller    │ ── Business logic execution            │
│     │   Execution     │                                         │
│     └─────────────────┘                                         │
│              │                                                  │
│              ▼                                                  │
│  3. ┌─────────────────┐                                         │
│     │  postHandle()   │ ── Called after controller execution   │
│     │                 │    Before view rendering                │
│     └─────────────────┘                                         │
│              │                                                  │
│              ▼                                                  │
│  4. ┌─────────────────┐                                         │
│     │afterCompletion()│ ── Called after complete request       │
│     │                 │    processing (even if exception)      │
│     └─────────────────┘                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Spring Interceptor Interface Methods

### 1. preHandle()
```java
public boolean preHandle(HttpServletRequest request, 
                        HttpServletResponse response, 
                        Object handler) throws Exception
```
- **Called**: Before the controller method execution
- **Purpose**: Authentication, logging, validation
- **Return**: `true` to continue, `false` to stop processing
- **Use Cases**: Security checks, request logging, rate limiting

### 2. postHandle()
```java
public void postHandle(HttpServletRequest request, 
                      HttpServletResponse response, 
                      Object handler, 
                      ModelAndView modelAndView) throws Exception
```
- **Called**: After controller execution, before view rendering
- **Purpose**: Modify model data, add common attributes
- **Use Cases**: Adding common model attributes, response modification

### 3. afterCompletion()
```java
public void afterCompletion(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler, 
                           Exception ex) throws Exception
```
- **Called**: After complete request processing
- **Purpose**: Cleanup, resource management, final logging
- **Use Cases**: Resource cleanup, performance monitoring, audit logging

## Project Architecture Analysis

### 1. Web Configuration (`WebConfig.java`)
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    LoggingInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/api/**");
    }
}
```

**Analysis:**
- **WebMvcConfigurer**: Interface to customize Spring MVC configuration
- **InterceptorRegistry**: Used to register interceptors with specific URL patterns
- **Path Patterns**: `/api/**` means interceptor applies to all API endpoints
- **Dependency Injection**: Uses `@Autowired` to inject interceptor as Spring bean

**Registration Methods:**
1. **Direct Instantiation**: `new LoggingInterceptor()` - Creates new instance
2. **Bean Injection**: `@Autowired` - Uses Spring-managed bean (recommended)

### 2. Logging Interceptor (`LoggingInterceptor.java`)
```java
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        System.out.println("prehandler request uri: " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, 
                          HttpServletResponse response, 
                          Object handler, 
                          ModelAndView modelAndView) throws Exception {
        System.out.println("post handler : executed handler for " + request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, 
                               Exception ex) throws Exception {
        System.out.println("After completion : request completed " + request.getRequestURI());
    }
}
```

**Analysis:**
- **@Component**: Makes it a Spring-managed bean
- **HandlerInterceptor**: Spring's interceptor interface
- **Logging Strategy**: Logs request URI at different lifecycle stages
- **Return Value**: `preHandle()` returns `true` to continue processing

## Domain Model Analysis

### Entity Relationships
```
┌─────────────────────────────────────────────────────────────────┐
│                      Entity Relationship Diagram                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│              ┌─────────────────┐                                │
│              │                 │                                │
│              │   Applicant     │                                │
│              │  ───────────    │                                │
│              │  - id           │                                │
│              │  - name         │                                │
│              │  - email        │                                │
│              │  - phone        │                                │
│              │  - status       │                                │
│              └─────────────────┘                                │
│                     │                                           │
│            ┌────────┼────────┐                                  │
│            │        │        │                                  │
│            ▼        ▼        ▼                                  │
│   ┌─────────────┐  │  ┌─────────────┐                          │
│   │             │  │  │             │                          │
│   │   Resume    │  │  │ Application │                          │
│   │ ──────────  │  │  │ ──────────  │                          │
│   │ - id        │  │  │ - id        │                          │
│   │ - content   │  │  │ - status    │                          │
│   │             │  │  │ - position  │                          │
│   └─────────────┘  │  └─────────────┘                          │
│        (1:1)       │       (1:N)                               │
│                    │                                           │
│                    ▼                                           │
│            ┌─────────────┐                                     │
│            │             │                                     │
│            │     Job     │◀──────────────────┐                │
│            │ ──────────  │                   │                │
│            │ - id        │                   │                │
│            │ - title     │            (M:N Relationship)      │
│            │ - description│                  │                │
│            └─────────────┘                   │                │
│                                              │                │
│                                              │                │
│              ┌───────────────────────────────┘                │
│              │                                                │
│              ▼                                                │
│     ┌─────────────────┐                                       │
│     │ Applicant_Job   │ (Join Table)                         │
│     │ ─────────────   │                                       │
│     │ - applicantId   │                                       │
│     │ - jobId         │                                       │
│     └─────────────────┘                                       │
│                                                                │
└─────────────────────────────────────────────────────────────────┘
```

### 3. Controllers Analysis

#### ApplicantController
```java
@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {
    // CRUD operations for applicants
}
```
**Interceptor Coverage**: All endpoints intercepted due to `/api/**` pattern

#### ApplicationController
```java
@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    // Application management endpoints
}
```

#### JobController & ResumeController
Similar structure with REST endpoints under `/api/**` path.

**Analysis:**
- **Consistent Pattern**: All controllers use `/api/` prefix
- **RESTful Design**: Proper HTTP methods and resource naming
- **Interceptor Integration**: All endpoints automatically logged by interceptor

## Interceptor Execution Example

### Request Flow for `POST /api/applicants/addApplicant`:

```
1. ┌─────────────────────────────────────────────────────────┐
   │ HTTP POST /api/applicants/addApplicant                  │
   └─────────────────────────────────────────────────────────┘
                              │
                              ▼
2. ┌─────────────────────────────────────────────────────────┐
   │ LoggingInterceptor.preHandle()                         │
   │ Output: "prehandler request uri: /api/applicants/..."   │
   │ Returns: true (continue processing)                     │
   └─────────────────────────────────────────────────────────┘
                              │
                              ▼
3. ┌─────────────────────────────────────────────────────────┐
   │ ApplicantController.saveApplicant()                    │
   │ Business logic execution                                │
   └─────────────────────────────────────────────────────────┘
                              │
                              ▼
4. ┌─────────────────────────────────────────────────────────┐
   │ LoggingInterceptor.postHandle()                        │
   │ Output: "post handler : executed handler for /api/..." │
   └─────────────────────────────────────────────────────────┘
                              │
                              ▼
5. ┌─────────────────────────────────────────────────────────┐
   │ Response sent to client                                 │
   └─────────────────────────────────────────────────────────┘
                              │
                              ▼
6. ┌─────────────────────────────────────────────────────────┐
   │ LoggingInterceptor.afterCompletion()                   │
   │ Output: "After completion : request completed /api/..." │
   └─────────────────────────────────────────────────────────┘
```

## Advanced Interceptor Concepts

### Multiple Interceptors
```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SecurityInterceptor())
            .addPathPatterns("/api/**")
            .order(1);
            
    registry.addInterceptor(new LoggingInterceptor())
            .addPathPatterns("/api/**")
            .order(2);
            
    registry.addInterceptor(new MetricsInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/public/**")
            .order(3);
}
```

### Path Pattern Examples
```java
registry.addInterceptor(interceptor)
    .addPathPatterns("/api/**")           // All API endpoints
    .addPathPatterns("/admin/**")         // Admin endpoints only
    .addPathPatterns("/**/*.json")        // All JSON requests
    .excludePathPatterns("/api/public/**") // Exclude public APIs
    .excludePathPatterns("/health")        // Exclude health checks
```

## Use Cases for Interceptors

### 1. Security Interceptor
```java
@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !isValidToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false; // Stop processing
        }
        return true;
    }
}
```

### 2. Performance Monitoring Interceptor
```java
@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Request {} completed in {} ms", 
                   request.getRequestURI(), duration);
    }
}
```

### 3. Audit Logging Interceptor
```java
@Component
public class AuditInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
        String user = getCurrentUser(request);
        String action = request.getMethod() + " " + request.getRequestURI();
        String status = response.getStatus() + "";
        
        auditService.log(user, action, status, new Date());
    }
}
```

## When to Use Filters vs Interceptors

### Use Filters When:
- **Security**: Authentication/authorization at servlet level
- **Encoding**: Character encoding, compression
- **Low-level Processing**: Request/response modification before Spring
- **Non-Spring Applications**: Working with plain servlets
- **Container-level Concerns**: CORS, security headers

### Use Interceptors When:
- **Spring Integration**: Need access to Spring beans and context
- **Controller-specific Logic**: Handler-aware processing  
- **Model Manipulation**: Modifying ModelAndView
- **Spring Security Integration**: Working with Spring Security
- **Exception Handling**: Better Spring exception integration

## Best Practices

### 1. Interceptor Design
- Keep interceptors lightweight and focused
- Use appropriate lifecycle method for each concern
- Handle exceptions gracefully
- Return `false` from `preHandle()` to stop processing

### 2. Configuration
- Use `@Order` for multiple interceptors
- Be specific with path patterns
- Use `excludePathPatterns()` for exceptions
- Register as Spring beans for dependency injection

### 3. Error Handling
```java
@Override
public boolean preHandle(HttpServletRequest request, 
                       HttpServletResponse response, 
                       Object handler) throws Exception {
    try {
        // Interceptor logic
        return true;
    } catch (Exception e) {
        logger.error("Interceptor error", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return false;
    }
}
```

### 4. Testing Interceptors
```java
@TestConfiguration
public class TestWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockInterceptor())
               .addPathPatterns("/api/**");
    }
}
```

## Getting Started

### Prerequisites
- Java 11+
- Spring Boot 3.x
- Maven/Gradle
- Database (H2/MySQL/PostgreSQL)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/AgeOfUltra/spring-interceptor.git
   cd spring-interceptor
   ```

2. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

3. **Test the interceptor**
   ```bash
   # Create an applicant - watch console for interceptor logs
   curl -X POST http://localhost:8080/api/applicants/addApplicant \
     -H "Content-Type: application/json" \
     -d '{"name":"John Doe","email":"john@example.com","status":"ACTIVE"}'
   ```

### Expected Console Output
```
prehandler request uri: /api/applicants/addApplicant
prehandler request org.apache.catalina.connector.RequestFacade@...
post handler : executed handler for /api/applicants/addApplicant
After completion : request completed /api/applicants/addApplicant
```

## API Endpoints

### Applicant Management
- `GET /api/applicants/getAllApplicant` - Get all applicants
- `POST /api/applicants/addApplicant` - Create new applicant
- `GET /api/applicants/page?page=0&size=10` - Paginated applicants
- `GET /api/applicants/getByStatus?status=ACTIVE` - Filter by status
- `GET /api/applicants/getByName?name=John` - Search by name

### Job Management
- `POST /api/job/createJob` - Create new job
- `GET /api/job/getAllJobs` - Get all jobs
- `GET /api/job/{id}` - Get job by ID
- `POST /api/job/add-job-to-applicant` - Associate job with applicant

### Application & Resume Management
- `POST /api/application/{applicantId}/saveApplication` - Create application
- `POST /api/resume/{applicantId}/addResume` - Add resume

## Database Schema

The application uses JPA entities with the following relationships:
- **Applicant ↔ Resume**: One-to-One bidirectional
- **Applicant ↔ Application**: One-to-Many bidirectional
- **Applicant ↔ Job**: Many-to-Many with join table

## Performance Considerations

- Interceptors execute for every matching request
- Keep interceptor logic lightweight
- Use async processing for heavy operations
- Consider caching for expensive operations
- Monitor interceptor performance impact

## Future Enhancements

- Add authentication interceptor
- Implement rate limiting interceptor
- Add request/response body logging
- Integrate with monitoring tools (Micrometer)
- Add correlation ID tracking
- Implement request timeout handling

## Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
