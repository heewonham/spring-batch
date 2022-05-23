# 서버에 구동시킬 자바
FROM openjdk:11

# `JAR_FILE` 이라는 이름으로 build 한 jar 파일을 지정
ARG JAR_FILE=build/libs/payment-0.0.1-SNAPSHOT.jar

# 지정한 jar 파일을 app.jar 이라는 이름올 Docker Container에 추가
COPY ${JAR_FILE} app.jar

# app.jar 파일을 실행
#ENTRYPOINT ["java","-jar","app.jar"]
ENTRYPOINT ["./wait-for-it.sh", "database:3307", "--", "java", "-jar", "app.jar"]