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