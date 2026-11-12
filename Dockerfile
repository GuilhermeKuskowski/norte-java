# Etapa 1 - build: compila o projeto com Maven e Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean -DskipTests package

# Etapa 2 - execucao: imagem leve so com o Java 21 e o .jar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ENV TZ=America/Sao_Paulo
COPY --from=build /app/target/norte-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -Duser.timezone=America/Sao_Paulo -jar app.jar"]
