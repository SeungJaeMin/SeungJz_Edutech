# Multi-stage build for optimized image size
FROM gradle:8.5-jdk19 AS builder

WORKDIR /app

# Copy gradle files for dependency caching
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Download dependencies
RUN gradle dependencies --no-daemon || return 0

# Copy source code
COPY src src

# Build application
RUN gradle bootJar --no-daemon

# Runtime stage
FROM openjdk:19-jdk-slim

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
