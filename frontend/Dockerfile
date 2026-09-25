# ============================================================
# Multi-stage Dockerfile for React Frontend
# ============================================================
# Stage 1: npm build — compiles React to static files
# Stage 2: Nginx — serves the static files in production
# ============================================================

FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
