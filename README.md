# Burlington Arcade — Sales Intelligence Portal

> **The World's First Shopping Mall · Est. 1819 · Mayfair, London**
> A cinematic, full-stack interactive sales deck with full authentication system.

---

## Features

### Cinematic Frontend (Phase 1 + Phase 2)
- **Hero Page** — Cinematic intro with animated arches, particles, video background
- **4 Sub-Modules** — Leasing (4 paths), Events (3 tabs), Sponsorship (3 tabs), Venues
- **Non-Linear Navigation** — Interactive cards, tab-based exploration
- **Regency Noir Design** — Obsidian, champagne gold, Cinzel + Cormorant Garamond typography
- **Custom Cursor** — Luxury-grade cursor follower
- **Fully Responsive** — Desktop, tablet, mobile

### Full Authentication System
- **Signup with strong validation** — Username, email, password rules
- **Login** — Username OR email accepted
- **JWT Tokens** — 1h access + 24h refresh
- **BCrypt** — Password hashing at strength 12 (2^12 rounds)
- **Account Lockout** — Auto-locks after 5 failed attempts for 30 mins
- **Rate Limiting** — Per-IP brute-force protection (5 logins / 3 signups per 15 min)
- **Password Strength Meter** — Real-time feedback during signup
- **H2 Database Persistence** — File-based, survives restarts
- **Role-Based Access** — ADMIN, SALES, PROSPECT
- **Auto Demo Mode** — Frontend works even without backend

---

## Tech Stack

| Layer        | Technology                              |
|--------------|------------------------------------------|
| Backend      | Java 21 · Spring Boot 3.2               |
| Security     | Spring Security · JJWT 0.12.5 · BCrypt  |
| Persistence  | Spring Data JPA · Hibernate · H2 DB     |
| Validation   | Jakarta Bean Validation                  |
| Frontend     | Vanilla JS · HTML5 · CSS3                |
| Build        | Apache Maven 3.8+                        |

---

## Project Structure

```
burlington-arcade/
├── README.md
├── .gitignore
├── pom.xml
└── src/main/
    ├── java/com/burlington/arcade/
    │   ├── ArcadeApplication.java          # Entry point
    │   ├── config/
    │   │   ├── SecurityConfig.java         # JWT + CORS + CSP
    │   │   └── DataInitializer.java        # Seeds default users
    │   ├── controller/
    │   │   ├── AuthController.java         # signup · login · refresh · validate
    │   │   └── SalesDataController.java    # All sales data endpoints
    │   ├── entity/
    │   │   └── User.java                   # JPA entity + UserDetails
    │   ├── exception/
    │   │   └── AuthExceptions.java         # Custom exception types
    │   ├── model/
    │   │   └── AuthModels.java             # Request/Response DTOs
    │   ├── repository/
    │   │   └── UserRepository.java         # Spring Data JPA
    │   ├── security/
    │   │   ├── JwtService.java             # Token generation & validation
    │   │   └── JwtAuthenticationFilter.java # Per-request filter
    │   └── service/
    │       ├── AuthService.java            # Signup/Login/Refresh logic
    │       ├── CustomUserDetailsService.java # JPA-backed user lookup
    │       └── RateLimitService.java       # Per-IP rate limiter
    └── resources/
        ├── application.properties          # JWT, DB, JPA config
        └── static/
            └── index.html                  # Complete SPA
```

---

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+

### Run
```bash
cd burlington-arcade
mvn spring-boot:run
```

Open **http://localhost:8080**

The H2 database is created automatically at `./data/burlington-arcade.mv.db`.
Three default users are seeded on first boot.

---

## Demo Credentials

| Username    | Password                | Role     |
|-------------|-------------------------|----------|
| `admin`     | `Admin@Burlington2024`  | ADMIN    |
| `salesrep`  | `Burlington@2024`       | SALES    |
| `prospect`  | `Prospect@2024`         | PROSPECT |

You can also **create a new account** through the Signup tab on the login screen.

---

## API Endpoints

### Public (No JWT Required)

| Method | Endpoint              | Description                      |
|--------|-----------------------|----------------------------------|
| POST   | `/api/auth/signup`    | Create new account               |
| POST   | `/api/auth/login`     | Login (username or email)        |
| POST   | `/api/auth/refresh`   | Refresh access token             |

### Protected (JWT Required)

| Method | Endpoint                              | Role          |
|--------|---------------------------------------|---------------|
| GET    | `/api/auth/validate`                  | Any           |
| GET    | `/api/sales/property-stats`           | Any           |
| GET    | `/api/sales/demographics`             | Any           |
| GET    | `/api/sales/leasing/categories`       | Any           |
| GET    | `/api/sales/events/capabilities`      | Any           |
| GET    | `/api/sales/events/highlights`        | Any           |
| GET    | `/api/sales/sponsorship/tiers`        | SALES, ADMIN  |
| GET    | `/api/sales/sponsorship/audience-data`| Any           |
| GET    | `/api/sales/venues`                   | Any           |
| POST   | `/api/sales/enquiry`                  | Any           |

---

## API Examples

### Signup
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jane.smith",
    "email": "jane@brand.com",
    "password": "SecureP@ss123",
    "fullName": "Jane Smith",
    "company": "Luxury Brand Co."
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "jane.smith",
    "password": "SecureP@ss123"
  }'
```

### Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{ "refreshToken": "<your-refresh-token>" }'
```

### Authenticated Request
```bash
curl http://localhost:8080/api/sales/property-stats \
  -H "Authorization: Bearer <your-access-token>"
```

---

## Security Features

| Feature                  | Implementation                                   |
|--------------------------|--------------------------------------------------|
| Password Hashing         | BCrypt strength 12 (2^12 = 4096 rounds)          |
| Token Algorithm          | HMAC-SHA256                                      |
| Access Token Expiry      | 1 hour                                           |
| Refresh Token Expiry     | 24 hours                                         |
| Account Lockout          | Auto-lock after 5 failures for 30 minutes        |
| Login Rate Limit         | 5 attempts / 15 min per IP                       |
| Signup Rate Limit        | 3 attempts / 15 min per IP                       |
| Password Requirements    | 8-100 chars, upper/lower/number/special required |
| Username Requirements    | 3-50 chars, [a-zA-Z0-9_.-] only                  |
| Email Validation         | Jakarta @Email + format check                    |
| CSRF                     | Disabled (stateless JWT)                         |
| CORS                     | Restricted to localhost & burlingtonarcade.com   |
| Content Security Policy  | Locked down to self + Google Fonts               |
| X-Frame-Options          | sameOrigin (DENY in production)                  |
| Referrer Policy          | strict-origin-when-cross-origin                  |
| Sessions                 | Stateless                                        |
| SQL Injection            | Prevented by JPA parameterized queries           |
| Error Information        | Hidden in responses                              |

---

## Database (H2)

The app uses H2 in **file mode** — data persists across restarts.

### Access H2 Console (dev only)
1. Visit `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:file:./data/burlington-arcade`
3. Username: `sa` · Password: *(empty)*

### To Reset DB
```bash
rm -rf ./data/
```

### To Migrate to PostgreSQL/MySQL (Production)
1. Replace H2 dependency in `pom.xml`
2. Update `spring.datasource.url`, `username`, `password`
3. Change `spring.jpa.properties.hibernate.dialect`

---

## Production Hardening Checklist

- [ ] Replace `jwt.secret` with `openssl rand -base64 32` and store in env vars
- [ ] Disable H2 console (`spring.h2.console.enabled=false`)
- [ ] Switch to PostgreSQL/MySQL
- [ ] Replace in-memory `RateLimitService` with Redis + Bucket4j
- [ ] Add HTTPS / TLS (use behind nginx or use embedded SSL)
- [ ] Change `frameOptions(sameOrigin)` → `frameOptions(deny)` in `SecurityConfig`
- [ ] Configure proper CORS origins for your production domain
- [ ] Add audit logging for all auth events
- [ ] Add email verification for signup
- [ ] Add password reset flow
- [ ] Add 2FA / TOTP

---

## License

© 2024 Burlington Arcade. Sales Intelligence Portal. All rights reserved.
This codebase is proprietary and confidential.
