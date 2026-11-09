# EstateHub — Real Estate Management Platform

A full-stack real-estate marketplace for property owners, buyers and renters. The application is a Java 21/Spring Boot modular monolith with a React/TypeScript frontend.

## Implemented features

- Email/password registration and JWT authentication
- Role-based access for USER, OWNER and ADMIN
- Owner identity comes from the authenticated JWT, never from a client-supplied owner ID
- Sale and rental property listings
- House, commercial and land categories
- Owner property creation, update, delete and listing dashboard
- Admin moderation: pending listings, approve and reject
- Public property search by listing type, category, city, area and price range
- Property detail pages
- Multiple property image upload backed by MinIO with signed image URLs
- Favorites for authenticated users
- Property inquiries
- Responsive marketplace UI for desktop and mobile
- Liquibase migrations, PostgreSQL and Docker Compose
- Swagger/OpenAPI
- Spotless/Google Java Format, Prettier, ESLint and GitHub Actions CI

## Stack

**Frontend:** React 19, TypeScript, Vite, React Router, Lucide icons  
**Backend:** Java 21, Spring Boot 3.5, Spring Security, JWT, JPA/Hibernate, Liquibase  
**Data:** PostgreSQL  
**Object storage:** MinIO  
**Infrastructure:** Docker Compose, GitHub Actions

## Local setup

1. Copy `.env.example` to `.env` and replace placeholder secrets.
2. Start PostgreSQL and MinIO with `docker compose up -d`.
3. Run the backend from `backend` with `mvn spring-boot:run`.
4. Run the frontend from `frontend` with `npm install && npm run dev`.
5. Open the Vite URL shown in the terminal.

The frontend expects the backend at `http://localhost:8080/api/v1` by default. Override it with `VITE_API_URL`.

## Roles

- **USER** — browse properties, save favorites and send inquiries.
- **OWNER** — all user capabilities plus create and manage owned property listings and upload images.
- **ADMIN** — moderate property listings through the admin API/UI.

Public registration cannot create ADMIN accounts. Provision administrators through a trusted database/operations process.

## Security

Do not commit real passwords, tokens, API keys or JWT secrets. Use environment variables and local ignored `.env` files. JWT signing secrets must be at least 32 bytes.

## Scope boundary

Real payment-provider integration and OTP are intentionally excluded because they require provider-specific credentials and requirements. The existing schema keeps a boundary for later integration.
