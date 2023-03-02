FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
RUN mkdir /opt/webapi
COPY ${JAR_FILE} /opt/webapi/webapi.jar
ENTRYPOINT ["java","-jar","/opt/webapi/webapi.jar"]
