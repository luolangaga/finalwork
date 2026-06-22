FROM node:20-alpine AS build
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

ARG SPRING_API=/api
ARG DOTNET_API=/api
ENV SPRING_API=${SPRING_API}
ENV DOTNET_API=${DOTNET_API}

RUN sed -i "s|__SPRING_API__|${SPRING_API}|g" /usr/share/nginx/html/config.js && \
    sed -i "s|__DOTNET_API__|${DOTNET_API}|g" /usr/share/nginx/html/config.js

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]