FROM maven:3.9.8-amazoncorretto-17
WORKDIR /app
COPY ./backend ./
EXPOSE 8081

ENTRYPOINT ["mvn", "spring-boot:run"]