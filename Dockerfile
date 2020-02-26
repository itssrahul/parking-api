FROM java:11
EXPOSE 8082
ADD /target/tollparking-app-0.0.1-SNAPSHOT.jar tollparking-app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "tollparking-app-0.0.1-SNAPSHOT.jar"]