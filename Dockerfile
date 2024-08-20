FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS build
# Receiving app version
ARG APP_VERSION=0.0.1
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
RUN chmod +x mvnw
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src

RUN ./mvnw versions:set -DnewVersion=${APP_VERSION} -f pom.xml -DskipTests -Dtests.skip=true -Dskip.unit.tests=true && \
    ./mvnw versions:commit -f pom.xml -DskipTests -Dtests.skip=true -Dskip.unit.tests=true \

RUN ./mvnw package -Pnative -DskipTests
#RUN ./mvnw package -DskipTests for JVM mode
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



#
#FROM registry.access.redhat.com/ubi8/openjdk-17:1.16

#ENV LANGUAGE='en_US:en'


# We make four distinct layers so if there are application changes the library layers can be re-used
#COPY --chown=185 --from=build /code/target/quarkus-app/lib/ /deployments/lib/
#COPY --chown=185 --from=build /code/target/quarkus-app/*.jar /deployments/
#COPY --chown=185 --from=build /code/target/quarkus-app/app/ /deployments/app/
#COPY --chown=185 --from=build /code/target/quarkus-app/quarkus/ /deployments/quarkus/

#EXPOSE 8080
#USER 185
#ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
#ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
#HEALTHCHECK --interval=300s --timeout=3s CMD curl -f http://localhost:3000/ || exit 1
#ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
