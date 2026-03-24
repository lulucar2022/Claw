# 多阶段构建 - 编译阶段
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制 pom.xml 并下载依赖（利用 Docker 缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache curl

# 从构建阶段复制 jar 包
COPY --from=builder /app/target/claw-backend-1.0.0.jar app.jar

# 暴露端口（根据配置文件是 9090）
EXPOSE 9090

# JVM 参数优化
ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:9090/api/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
