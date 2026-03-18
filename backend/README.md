# 程序员面试知识答题系统 - 后端项目

## 项目概述

基于Spring Boot 3.x的后端服务，为程序员面试知识答题系统提供API支持。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **语言**: Java 17
- **数据库**: MySQL 8.0 + Redis 7.0
- **安全**: Spring Security + JWT
- **API文档**: SpringDoc OpenAPI 3.0
- **构建工具**: Maven

## 项目结构

```
src/main/java/com/claw/
├── ClawApplication.java              # 主启动类
├── common/                           # 通用模块
│   ├── exception/                    # 异常处理
│   ├── model/                        # 基础模型
│   ├── response/                     # 统一响应
│   └── security/                     # 安全相关
├── config/                           # 配置类
├── modules/                          # 业务模块
│   ├── user/                         # 用户模块
│   │   ├── controller/               # 控制器
│   │   ├── dto/                      # 数据传输对象
│   │   ├── entity/                   # 实体类
│   │   ├── enums/                    # 枚举类
│   │   ├── repository/               # 数据访问层
│   │   └── service/                  # 业务逻辑层
│   ├── question/                     # 题目模块
│   └── recommendation/               # 推荐模块
└── resources/                        # 资源文件
    ├── application.yml               # 主配置文件
    ├── application-dev.yml           # 开发环境配置
    ├── application-prod.yml          # 生产环境配置
    └── schema.sql                    # 数据库初始化脚本
```

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.8+

### 数据库配置

1. 启动MySQL服务
2. 创建数据库：
   ```sql
   CREATE DATABASE claw_db_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 执行初始化脚本：
   ```bash
   mysql -u root -p claw_db_dev < src/main/resources/schema.sql
   ```

### Redis配置

1. 启动Redis服务
2. 默认配置：
   - 主机: localhost
   - 端口: 6379
   - 数据库: 1 (开发环境)

### 运行项目

1. 克隆项目到本地
2. 配置数据库连接信息（如果需要）
3. 运行以下命令：

```bash
# 使用Maven运行
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/claw-backend-1.0.0.jar
```

4. 访问以下地址：
   - 应用: http://localhost:8081
   - API文档: http://localhost:8081/swagger-ui.html
   - H2控制台: http://localhost:8081/h2-console

## API文档

### 认证接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | /api/v1/auth/register | 用户注册 | 不需要 |
| POST | /api/v1/auth/login | 用户登录 | 不需要 |
| POST | /api/v1/auth/refresh | 刷新Token | 需要Refresh Token |
| POST | /api/v1/auth/logout | 退出登录 | 需要Access Token |

### 用户接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/v1/users/profile | 获取用户信息 | 需要 |
| PUT | /api/v1/users/profile | 更新用户信息 | 需要 |
| GET | /api/v1/users | 分页查询用户 | 管理员 |
| PUT | /api/v1/users/{userId}/status | 更新用户状态 | 管理员 |

### 题目接口

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | /api/v1/questions | 分页查询题目 | 可选 |
| GET | /api/v1/questions/{questionId} | 获取题目详情 | 可选 |
| POST | /api/v1/questions/{questionId}/answer | 提交答案 | 需要 |
| GET | /api/v1/categories | 获取分类列表 | 可选 |

## 配置说明

### 开发环境

- 端口: 8081
- 数据库: claw_db_dev
- JWT密钥: dev-jwt-secret-key-1234567890
- 开启Swagger UI
- 开启H2控制台

### 生产环境

- 端口: 8080
- 数据库: claw_db_prod
- JWT密钥: 通过环境变量设置
- 关闭Swagger UI
- 启用HTTPS
- 启用监控

## 开发指南

### 添加新模块

1. 在`modules`目录下创建新模块
2. 按照现有结构创建包：
   - `controller/` - 控制器
   - `dto/` - 数据传输对象
   - `entity/` - 实体类
   - `repository/` - 数据访问层
   - `service/` - 业务逻辑层
3. 在实体类上添加JPA注解
4. 创建Repository接口继承JpaRepository
5. 创建Service类实现业务逻辑
6. 创建Controller类暴露API接口

### 异常处理

使用统一的异常处理机制：

```java
// 抛出业务异常
throw new BusinessException(400, "参数错误");

// 抛出数据不存在异常
throw DataNotFoundException.userNotFound(userId);

// 在Controller中返回统一响应
return ResponseEntity.ok(ApiResponse.success(data, "操作成功"));
```

### 安全配置

- 使用JWT进行认证
- 基于角色的权限控制
- 密码使用BCrypt加密
- 支持CORS跨域

## 测试

### 单元测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

### 集成测试

```bash
# 运行集成测试
mvn verify -P integration-test
```

## 部署

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/claw-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 环境变量

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| DB_URL | 数据库URL | jdbc:mysql://localhost:3306/claw_db_prod |
| DB_USERNAME | 数据库用户名 | prod_user |
| DB_PASSWORD | 数据库密码 | prod_password |
| JWT_SECRET_KEY | JWT密钥 | (必需) |
| REDIS_HOST | Redis主机 | localhost |
| REDIS_PORT | Redis端口 | 6379 |

## 监控

### 健康检查

- GET /actuator/health - 应用健康状态
- GET /actuator/info - 应用信息
- GET /actuator/metrics - 应用指标

### 性能监控

- 使用Prometheus收集指标
- 使用Grafana展示监控数据
- 日志输出到ELK Stack

## 常见问题

### 1. 数据库连接失败

检查：
- MySQL服务是否启动
- 数据库连接配置是否正确
- 用户权限是否足够

### 2. Redis连接失败

检查：
- Redis服务是否启动
- 防火墙是否开放6379端口
- Redis配置是否正确

### 3. JWT认证失败

检查：
- JWT密钥是否正确配置
- 令牌是否已过期
- 请求头是否正确包含Authorization

## 许可证

MIT License