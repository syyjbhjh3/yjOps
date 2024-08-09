FROM openjdk:22-jdk-slim
ADD ./build/libs/yjops-operator-0.0.1-SNAPSHOT.jar /
WORKDIR /

ENTRYPOINT ["java", "-Dfile.encoding=UTF8", "-Duser.timezone=Asia/Seoul", "-jar", "yjops-operator-0.0.1-SNAPSHOT.jar"]