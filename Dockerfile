FROM java:8

ADD target/skribentus-api.jar /
ADD .properties /

EXPOSE 4567
ENTRYPOINT ["java", "-jar", "skribentus-api.jar"]
