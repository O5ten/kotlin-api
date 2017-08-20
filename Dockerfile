FROM java:8

ADD target/kotlin-api.jar /
ADD .properties /

EXPOSE 4567
ENTRYPOINT ["java", "-jar", "kotlin-api.jar", "-Dmongodb.host=192.168.0.198"]
