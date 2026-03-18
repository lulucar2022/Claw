# 程序员面试知识答题系统

## 项目概述

为满足程序员面试备考、知识巩固及能力提升需求，开发一套程序员面试知识答题系统，整合个性化推荐、热点排行、相似题推荐及算法监控等核心功能。

## 系统特点

1. **个性化推荐** - 基于用户画像和历史行为智能推荐
2. **热点排行** - 实时展示高频面试题目
3. **相似题推荐** - 智能匹配知识点相关题目
4. **算法监控** - 可视化跟踪推荐效果
5. **错题集** - 自动记录和专项练习
6. **备考规划** - 个性化学习路径规划
7. **模拟答题** - 真实面试场景模拟

## 技术栈

### 后端
- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.0 + Redis 7.0
- **消息队列**: RabbitMQ
- **搜索引擎**: Elasticsearch
- **算法**: Python + Scikit-learn

### 前端
- **框架**: React 18
- **状态管理**: Redux Toolkit
- **UI组件**: Ant Design 5.x
- **构建工具**: Vite
- **图表**: ECharts

### 部署
- **容器化**: Docker + Docker Compose
- **CI/CD**: Jenkins + GitLab CI
- **监控**: Prometheus + Grafana
- **日志**: ELK Stack

## 项目结构

```
claw/
├── docs/                    # 项目文档
├── backend/                 # 后端代码
├── frontend/                # 前端代码
├── devops/                  # 运维部署配置
├── tests/                   # 测试相关
└── README.md               # 项目说明
```

## 快速开始

### 环境要求
+ JDK 17+
+ Node.js 18+
+ Docker 20+
+ MySQL 8.0
+ Redis 7.0

### 启动步骤
1. 克隆项目
2. 配置环境变量
3. 启动数据库服务
4. 运行后端服务
5. 启动前端应用

## 开发文档

详细文档请查看 `docs/` 目录：
- [开发时间线](docs/development_timeline.md)
- [技术架构设计](docs/technical_architecture.md)
- [数据库设计](docs/database_design.md)
- [API接口文档](docs/api_documentation.md)
- [测试策略](docs/testing_strategy.md)

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码变更
4. 编写测试用例
5. 提交 Pull Request

## 许可证

MIT License