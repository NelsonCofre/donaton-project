# Imagen de producción: build de Vite + Nginx para servir el SPA estático.
# Las URLs públicas de API se inyectan en build time (Vite).

ARG NODE_VERSION=22

FROM node:${NODE_VERSION}-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# URLs que el navegador usará para llamar al API Gateway (desde el host, no desde la red Docker/K8s).
ARG VITE_API_BASE_URL=http://localhost:8080
ARG VITE_NECESSITY_API_BASE_URL=
ARG VITE_LOGISTICS_API_BASE_URL=
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}
ENV VITE_NECESSITY_API_BASE_URL=${VITE_NECESSITY_API_BASE_URL}
ENV VITE_LOGISTICS_API_BASE_URL=${VITE_LOGISTICS_API_BASE_URL}

RUN npm run build

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
