FROM gradle:8.10.1-jdk17-alpine AS build

LABEL student="Закатов Игорь Сергеевич ИКБО-01-21"
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

COPY src src

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim

WORKDIR /app

ENV DB_HOST=host.docker.internal
ENV DB_PORT=5432
ENV DB_USERNAME=zakat
ENV DB_PASSWORD=zakat
ENV DB_NAME=postgres

COPY --from=build /app/build/libs/*.jar app.jar

RUN apt-get update && apt-get install -y wget && \
    wget https://www.mirea.ru/upload/medialibrary/80f/MIREA_Gerb_Colour.png -O /MIREA_Gerb_Colour.png || \
    (echo "Error" && exit 1)

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

ONBUILD RUN echo "Сборка и запуск произведены. Автор: Закатов И.С."

