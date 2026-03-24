# WSL 中使用 Docker 启动 Claw 项目

## 项目服务说明

本项目包含以下服务：

1. **claw-backend** (Spring Boot 应用) - 端口：9090
2. **mysql** (数据库) - 端口：3306
3. **redis** (缓存) - 端口：6379

## 在 WSL 中启动步骤

### 1. 确保 Docker 已安装并运行

```bash
# 检查 Docker 是否安装
docker --version
docker-compose --version

# 如果未安装，在 WSL 中安装 Docker
# Ubuntu/Debian:
sudo apt update
sudo apt install docker.io docker-compose -y

# 启动 Docker 服务
sudo service docker start
```

### 2. 进入项目目录

```bash
cd /mnt/d/wxl/Intellij_project/Claw/backend
```

### 3. 构建并启动所有服务

```bash
# 一次性构建并启动 MySQL、Redis 和 Spring Boot 应用
docker-compose up -d --build
```

### 4. 查看服务状态

```bash
# 查看所有容器运行状态
docker-compose ps

# 查看应用日志
docker-compose logs -f app

# 查看 MySQL 日志
docker-compose logs -f mysql

# 查看 Redis 日志
docker-compose logs -f redis
```

### 5. 访问服务

- **后端 API**: http://localhost:9090/api
- **API 文档**: http://localhost:9090/api/swagger-ui.html
- **健康检查**: http://localhost:9090/api/actuator/health

### 6. 停止服务

```bash
# 停止所有服务
docker-compose down

# 停止并删除数据卷（谨慎使用！会删除数据库数据）
docker-compose down -v
```

## 常用命令

```bash
# 重启应用
docker-compose restart app

# 重新构建应用
docker-compose build app

# 进入应用容器
docker-compose exec app sh

# 进入 MySQL 容器
docker-compose exec mysql bash

# 进入 Redis 容器
docker-compose exec redis sh

# 查看资源使用情况
docker stats
```

## 环境变量配置

可以通过修改 `docker-compose.yml` 中的环境变量来调整配置：

- `SPRING_PROFILES_ACTIVE`: Spring 配置文件（dev/test/prod）
- `JWT_SECRET_KEY`: JWT 密钥
- `SPRING_DATASOURCE_*`: 数据库连接配置
- `SPRING_REDIS_*`: Redis 连接配置
- `JAVA_OPTS`: JVM 参数

## 持久化数据

数据存储在 Docker volumes 中：

- `mysql_data`: MySQL 数据文件
- `redis_data`: Redis 数据文件

这些 volume 会持久化保存，即使删除容器数据也不会丢失。

## 故障排查

### 应用启动失败

```bash
# 查看详细日志
docker-compose logs app

# 检查依赖服务是否正常
docker-compose ps
```

### 数据库连接失败

确保 MySQL 已经完全启动：

```bash
# 等待 MySQL 健康检查通过
docker-compose ps mysql

# 测试数据库连接
docker-compose exec mysql mysqladmin ping -h localhost -u root -proot
```

### 端口冲突

如果端口被占用，修改 `docker-compose.yml` 中的端口映射：

```yaml
ports:
  - "9091:9090"  # 将宿主机的 9091 映射到容器的 9090
```
