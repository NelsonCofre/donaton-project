# Diagramas de Componentes

Este documento contiene los diagramas de componentes en formato Mermaid para los contenedores trabajados recientemente en el proyecto:

- Frontend
- Auth Service
- Donation Service

Hacer click derecho en la pestaña de este archivo y seleccionar "Open Preview" para visualizar los diagramas


## Frontend

```mermaid
flowchart TD
    A["main.tsx"] --> B["App"]
    A --> C["global.css"]

    B --> D["AuthProvider"]
    D --> E["RouterProvider / createBrowserRouter"]

    E --> F["MainLayout"]
    E --> G["ProtectedRoute"]

    E --> H["LoginPage"]
    E --> I["RegisterPage"]
    G --> J["DonationsPage"]

    H --> K["LoginForm"]
    I --> L["RegisterForm"]
    J --> M["DonationsDashboard"]

    M --> N["DonationList"]
    M --> O["CreateDonationForm"]
    O --> P["DonationForm"]
    M --> P

    K --> Q["userApi"]
    L --> Q
    M --> R["donationApi"]

    Q --> S["shared/api/client"]
    R --> S
    S --> T["env.apiBaseUrl / BFF or backend API"]

    D --> U["authStorage + auth events + useAuth"]
    F --> U
    G --> U
    K --> U

    R --> V["Donation entity types + mapper"]
    Q --> W["User entity types"]
```

## Auth Service

```mermaid
flowchart TD
    A["AuthController"] --> B["AuthService"]

    B --> C["UserAccountRepository"]
    B --> D["JwtService"]
    B --> E["RefreshTokenService"]
    B --> F["TokenBlacklistService"]
    B --> G["AuthenticationManager"]
    B --> H["PasswordEncoder"]

    E --> I["RefreshTokenRepository"]
    F --> J["BlacklistedTokenRepository"]

    K["SecurityConfig"] --> L["JwtAuthenticationFilter"]
    K --> M["CustomUserDetailsService"]
    K --> G
    K --> H

    L --> D
    L --> M
    L --> F

    M --> C

    C --> N["UserAccount"]
    I --> O["RefreshToken"]
    J --> P["BlacklistedToken"]

    Q["ApiExceptionHandler"] --> A
    R["DTOs"] --> A
    B --> R

    C --> S["Auth Database"]
    I --> S
    J --> S

    T["Flyway Migrations"] --> S
    U["application.properties"] --> K
    U --> D
```

## Donation Service

```mermaid
flowchart TD
    A["DonationController"] --> B["DonationService"]
    B --> C["DonationServiceImpl"]

    C --> D["DonationRepository"]
    C --> E["Donation entity"]
    C --> F["DonationRequestDto"]
    C --> G["DonationResponseDto"]

    H["SecurityConfiguration"] --> A
    I["GlobalExceptionHandler"] --> A

    D --> J["Donation Database"]
    K["Flyway Migrations"] --> J

    L["application.properties"] --> H
    L --> C
```
