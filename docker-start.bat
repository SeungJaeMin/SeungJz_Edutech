@echo off
echo.
echo 🚀 Starting SeungJz Edutech Platform...
echo.

echo 📦 Stopping existing containers...
docker-compose -f docker-compose.simple.yml down

echo.
echo 🔨 Building and starting services...
docker-compose -f docker-compose.simple.yml up --build -d

echo.
echo ✅ Services started successfully!
echo.
echo 📍 Access URLs:
echo    Frontend:   http://localhost:3000
echo    Backend:    http://localhost:8080
echo    Swagger:    http://localhost:8080/swagger-ui.html
echo    H2 Console: http://localhost:8080/h2-console
echo    PostgreSQL: localhost:5432
echo.
echo 📊 View logs:
echo    docker-compose -f docker-compose.simple.yml logs -f
echo.
echo 🛑 Stop all services:
echo    docker-compose -f docker-compose.simple.yml down
echo.
pause
