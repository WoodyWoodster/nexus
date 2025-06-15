.PHONY: all clean build spotless docker-up

# Default target
all: clean spotless build docker-up

# Clean build directories
clean:
	./gradlew clean

# Run spotlessApply
spotless:
	./gradlew spotlessApply

# Build the application
build:
	./gradlew build

# Run docker-compose with build flag
docker-up:
	docker compose up --build

# Stop docker containers
docker-down:
	docker compose down

integration-test:
	./gradlew integrationTest

# Run all tests
test: unit-test integration-test

# Run tests for specific service
test-order-service:
	./gradlew :services:order-service:test

test-user-service:
	./gradlew :services:user-service:test

# Run unit tests for all services
unit-test:
	./gradlew test

# Show help
help:
	@echo "Available commands:"
	@echo ""
	@echo "make                  	- Clean, format, build, and start application with Docker"
	@echo "make clean            	- Remove all build artifacts"
	@echo "make spotless         	- Format all source code using spotless"
	@echo "make build            	- Build the entire application"
	@echo "make docker-up        	- Start all services using Docker Compose"
	@echo "make docker-down      	- Stop all Docker containers"
	@echo "make integration-test 	- Run integration tests for the application"
	@echo "make test             	- Run all tests across all services"
	@echo "make test-order-service - Run tests for the order service only"
	@echo "make test-user-service  - Run tests for the user service only"
	@echo "make unit-test        	- Run unit tests for all services"
	@echo ""
	@echo "Example usage:"
	@echo "  make               	- Full build and start"
	@echo "  make clean build   	- Clean and rebuild without starting"