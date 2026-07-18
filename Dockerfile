FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw -q -DskipTests package -DskipTests
COPY src src
RUN ./mvnw -q -DskipTests package
EXPOSE 8080
CMD ["java", "-jar", "target/job-tracker-backend-0.0.1-SNAPSHOT.jar"]
