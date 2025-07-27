# Java Job Offer Platform

A microservice-based platform for managing and browsing job offers. The system includes user authentication, external job data importing, and a modern frontend UI.

## 🏗 Tech Stack

### Backend
- **Java 17** + **Spring Boot**
- **Spring Security** + **Keycloak** (Authentication)
- **PostgreSQL** / **Cassandra**
- **Kafka** (Message queue)
- **Elasticsearch** (Search engine)
- **Feign** (Service-to-service communication)
- **Docker** + **Docker Compose**
- **Prometheus** + **Grafana** (Monitoring)

### Frontend
- **React** + **TypeScript**
- **Tailwind CSS** + **shadcn/ui**
- **Axios**
- **JWT Authentication**

## 🔧 Services Overview

| Service | Description |
|---------|-------------|
| `auth-service` | User authentication & registration |
| `joboffer-service` | CRUD operations on job offers |
| `joboffer-importer` | Fetches job offers from external APIs |
| `api-gateway` | Single entry point for frontend calls |
| `notification-service` | Sends email notifications |
| `monitoring-service` | Tracks service health & performance |

## 🚀 Getting Started

### Prerequisites
- **Docker** + **Docker Compose**
- **Java 17+**
- **Node.js** (for frontend)
- **pnpm** or **npm**

### Backend Setup

```bash
docker-compose up --build
```

### Frontend Setup

```bash
cd frontend
pnpm install
pnpm dev
```

## 🔐 Demo Accounts

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@admin.com` | `admin123` |
| **User** | `user@user.com` | `user123` |

## 📂 Sample Endpoints

- `GET /api/offers` – *Get all offers*
- `POST /api/offers` – *Add new offer (admin only)*
- `GET /api/user` – *Fetch current user details*

## 🧪 Testing

- **Unit tests** for services using **JUnit** & **Mockito**
- **Integration tests** with **Testcontainers** *(planned)*
- **Frontend tests** *(Jest or Cypress – planned)*

## 📈 Monitoring

- **Prometheus** + **Grafana** setup
- **Spring Boot Actuator** endpoints (`/actuator/health`, etc.)
