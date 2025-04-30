FROM openjdk:21-jdk

WORKDIR /awx-automation

COPY Awx-automation/Awx-automation/target/Awx-automation-0.0.1-SNAPSHOT.jar /awx-automation/Awx-automation.jar

EXPOSE 8080

CMD ["java", "-jar", "Awx-automation.jar"]
