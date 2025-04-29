FROM openjdk:21-jdk

WORkDIR /automation

COPY Awx-automation/Awx-automation/target/Awx-automation-0.0.1-SNAPSHOT.jar /automation/Awx-automation.jar

EXPOSE 8081

CMD ["java", "-jar", "Awx-automation.jar"]