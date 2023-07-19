FROM maven:alpine AS build
COPY mvnw /code/mvnw
COPY .mvn /code/.mvn
COPY  pom.xml /code/
WORKDIR /code
RUN chmod +x mvnw
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
RUN ./mvnw package  -Dnative -DskipTests -Dquarkus.native.container-build=true
HEALTHCHECK --interval=300s --timeout=30s CMD ./mvnw --version  || exit 1
###
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
COPY --chown=1001:root --from=build /code/target/*-runner /work/application

EXPOSE 3000
USER 1001
HEALTHCHECK --interval=300s --timeout=3s CMD curl -f http://localhost:3000/ || exit 1
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
