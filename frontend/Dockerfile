# Imagen de producción: build de Vite + Nginx para servir el SPA estático.
# La URL del BFF se inyecta en build time (VITE_API_BASE_URL).

ARG NODE_VERSION=22

FROM node:${NODE_VERSION}-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# URL que el navegador usará para llamar al BFF (desde el host, no desde la red Docker del browser).
# Ejemplo con BFF expuesto en el host en el puerto 8080: http://localhost:8080
ARG VITE_API_BASE_URL=http://localhost:8080
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

RUN npm run build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
