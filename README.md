# CMTS Inventory

A props and equipment inventory management system for Columbia Musical Theatre Society (CMTS) and partner theatre groups. Allows productions to check out and return items, with role-based access control for admins and crew.

## Tech Stack

- **Backend:** Java 21, Spring Boot 3, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Frontend:** React *(in progress)*
- **Auth:** JWT

## Domain

- **Users** — belong to one or more productions, role of `ADMIN` or `CREW`
- **Productions** — theatre productions that can be active or archived
- **Items** — props/equipment with categories, status, and location
- **Checkouts** — tracks who checked out what item for which production, with due dates and return timestamps

## API Endpoints

### Auth
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/auth` | Login and receive JWT token |

### Items
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/items` | Get all items |
| GET | `/api/v1/items/{id}` | Get item by ID |
| POST | `/api/v1/items` | Create item (Admin only) |
| PUT | `/api/v1/items/{id}` | Update item (Admin only) |
| DELETE | `/api/v1/items/{id}` | Delete item (Admin only) |

### Productions
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/productions` | Get all productions |
| GET | `/api/v1/productions/{id}` | Get production by ID |
| GET | `/api/v1/productions/name/{name}` | Get production by name |
| GET | `/api/v1/productions/{id}/users` | Get all users in a production |
| POST | `/api/v1/productions` | Create production (Admin only) |
| PATCH | `/api/v1/productions/{id}/archive` | Archive production (Admin only) |
| DELETE | `/api/v1/productions/{id}` | Delete production (Admin only, blocked if active checkouts exist) |

### Users
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/users` | Get all users (Admin only) |
| GET | `/api/v1/users/{id}` | Get user by ID |
| GET | `/api/v1/users/{id}/productions` | Get all productions a user belongs to |
| POST | `/api/v1/users` | Register a new user |
| DELETE | `/api/v1/users/{id}` | Delete user (Admin only) |

### Checkouts
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/checkouts` | Get all active checkouts |
| POST | `/api/v1/checkouts` | Check out an item |
| PUT | `/api/v1/checkouts/items/{itemId}/checkin` | Check in an item |

## Running Locally

**Prerequisites:** Java 21, Maven, PostgreSQL, Node.js

1. Create a PostgreSQL database called `cmts_props`
2. Update `src/main/resources/application.properties` with your DB credentials and a JWT secret
3. Run the backend:
```bash
./mvnw spring-boot:run
```
4. Run the frontend:
```bash
cd frontend
npm install
npm run dev
```

## Roadmap

### Backend
- [x] Entity/repository/service/controller layers for all four domains
- [x] Many-to-many user-production relationships
- [x] Production archiving with active checkout validation
- [x] Block production deletion if active checkouts exist
- [x] Global exception handling with typed error responses
- [x] DTO validation across all endpoints
- [x] Password hashing with BCrypt
- [x] JWT authentication and role-based access control
- [x] Endpoints for getting a user's productions and a production's users
- [x] Available quantity calculated per item (total minus active checkouts)

### Frontend (React — in progress)
- [x] Project setup (Vite + React + axios + react-router-dom)
- [ ] Item Catalog page with category filters and search
- [ ] Login page
- [ ] My Checkouts page
- [ ] Checkout flow for crew
- [ ] Admin dashboard — manage items, productions, users
- [ ] Archive production UI

### Deployment
- [ ] Move JWT secret to environment variable
- [ ] Deploy backend to AWS Elastic Beanstalk
- [ ] Deploy PostgreSQL to AWS RDS
- [ ] Deploy frontend (Vercel or S3 + CloudFront)
- [ ] Set `spring.jpa.hibernate.ddl-auto` to `validate` in production
