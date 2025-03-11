FROM openjdk:23-ea-jdk-slim


# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file
COPY target/mini1.jar /app/mini1.jar

# Create the data directory inside the container
RUN mkdir -p /app/data
ENV DOCKER_ENV=true

# Copy JSON data files from src/main/java/com/example/data
COPY src/main/java/com/example/data/carts.json /app/data/carts.json
COPY src/main/java/com/example/data/orders.json /app/data/orders.json
COPY src/main/java/com/example/data/products.json /app/data/products.json
COPY src/main/java/com/example/data/users.json /app/data/users.json

# Set environment variables for JSON file paths inside the container
ENV CART_DATA_PATH="/app/data/carts.json"
ENV ORDER_DATA_PATH="/app/data/orders.json"
ENV PRODUCT_DATA_PATH="/app/data/products.json"
ENV USER_DATA_PATH="/app/data/users.json"

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Run the Spring Boot JAR file
CMD ["java", "-jar", "/app/mini1.jar"]
