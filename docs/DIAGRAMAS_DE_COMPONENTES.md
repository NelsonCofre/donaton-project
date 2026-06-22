# Diagramas de Componentes

Este documento contiene versiones resumidas de los diagramas de componentes del proyecto en formato Mermaid.

Servicios incluidos:

- Frontend
- BFF
- Auth Service
- Donation Service
- Necessity Service
- Logistics Service

Haz click derecho en la pestaña de este archivo y selecciona "Open Preview" para visualizar los diagramas.

## Frontend

```mermaid
flowchart LR
    A[Usuario] --> B[Pages]
    B --> C[Features]
    C --> D[API Client]
    D --> E[BFF / API Gateway]

    C --> F[Entities]
    B --> G[Auth Guard]
```

## Auth Service

```mermaid
flowchart LR
    A[Frontend / BFF] --> B[AuthController]
    B --> C[AuthService]
    C --> D[UserRepository]
    D --> E[(Auth Database)]

    C --> F[JWT Service]
    B --> G[DTOs]
```

## Donation Service

```mermaid
flowchart LR
    A[Frontend / BFF] --> B[DonationController]
    B --> C[DonationService]
    C --> D[DonationRepository]
    D --> E[(PostgreSQL)]

    C --> F[Donation Entity]
    B --> G[DTOs]
```

## Necessity Service

```mermaid
flowchart LR
    A[Frontend / BFF] --> B[NecessityController]
    B --> C[NecessityService]
    C --> D[NecessityRepository]
    D --> E[(PostgreSQL)]

    C --> F[Necessity Entity]
    B --> G[DTOs]
```

## BFF

```mermaid
flowchart LR
    A[Frontend] --> B[JwtAuthFilter]
    B --> C[BffControllers]
    C --> D[Mappers]
    C --> E[Service Clients]
    E --> F[Auth Service]
    E --> G[Donation Service]
    E --> H[Necessity Service]
    E --> I[Logistics Service]

    C --> J[DTOs]
```

## Logistics Service

```mermaid
flowchart LR
    A[Frontend / BFF] --> B[LogisticsController]
    B --> C[LogisticsService]
    C --> D[Repositories]
    D --> E[(PostgreSQL)]

    C --> F[Logistics Entities]
    B --> G[DTOs]
```
