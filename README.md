# Real Estate Management Platform

Production-oriented real estate platform implemented as a modular monolith.

## Stack
- Frontend: React, TypeScript, Vite, PWA, Tailwind CSS, shadcn/ui
- Backend: Java 21, Spring Boot, Spring Security, JWT, JPA/Hibernate, Liquibase, Maven
- Data: PostgreSQL
- Object storage: MinIO
- Infrastructure: Docker Compose, GitHub Actions

## Modules
Users and authentication, property management, search, booking, advertisements, administration, notifications, and a payment boundary.

Payment provider integration and OTP are intentionally not implemented until provider requirements are confirmed.

## Repository layout
- `backend/` Spring Boot application
- `frontend/` React application
- `docs/` architecture, business and API documentation
- `.github/workflows/` CI
- `docker-compose.yml` local infrastructure

## Security configuration
Never commit real usernames, passwords, tokens, or API keys. Local secrets belong in an ignored `.env` file. Copy `.env.example` to `.env` and provide local values.

## Status
Initial production foundation and core domain implementation.
