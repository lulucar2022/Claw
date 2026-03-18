# 程序员面试知识答题系统 - API接口规划

## 1. API设计规范

### 1.1 设计原则
- **RESTful风格**: 遵循RESTful API设计规范
- **版本控制**: API版本前缀 `/api/v1/`
- **统一响应格式**: 标准化响应数据结构
- **安全认证**: JWT Token认证机制
- **限流防护**: 接口访问频率限制
- **文档化**: 自动生成API文档

### 1.2 通用约定

#### 1.2.1 请求头
```http
Authorization: Bearer {token}
Content-Type: application/json
Accept: application/json
X-Request-ID: {request_id}
X-Client-Version: {version}
```

#### 1.2.2 响应格式
```json
{
  "code": 200,
  "message": "Success",
  "data": {},
  "timestamp": "2026-03-17T10:30:00Z",
  "requestId": "req_123456"
}
```

#### 1.2.3 错误码规范
| 错误码 | 说明 | HTTP状态码 |
|--------|------|------------|
| 200 | 成功 | 200 OK |
| 400 | 请求参数错误 | 400 Bad Request |
| 401 | 未授权 | 401 Unauthorized |
| 403 | 禁止访问 | 403 Forbidden |
| 404 | 资源不存在 | 404 Not Found |
| 429 | 请求过于频繁 | 429 Too Many Requests |
| 500 | 服务器内部错误 | 500 Internal Server Error |

## 2. 用户认证模块

### 2.1 用户注册
- **接口**: `POST /api/v1/auth/register`
- **描述**: 用户注册接口
- **认证**: 不需要

**请求参数**:
```json
{
  "username": "string, required, 用户名",
  "email": "string, required, 邮箱",
  "password": "string, required, 密码",
  "nickname": "string, optional, 昵称",
  "phone": "string, optional, 手机号",
  "tech_stack": "array, optional, 技术栈",
  "experience_years": "integer, optional, 工作年限"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 123,
    "username": "testuser",
    "email": "test@example.com",
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 3600
  }
}
```

### 2.2 用户登录
- **接口**: `POST /api/v1/auth/login`
- **描述**: 用户登录接口
- **认证**: 不需要

**请求参数**:
```json
{
  "username": "string, required, 用户名或邮箱",
  "password": "string, required, 密码",
  "rememberMe": "boolean, optional, 记住我"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 123,
    "username": "testuser",
    "nickname": "测试用户",
    "avatar": "https://example.com/avatar.jpg",
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 3600,
    "permissions": ["question:read", "answer:create"]
  }
}
```

### 2.3 刷新Token
- **接口**: `POST /api/v1/auth/refresh`
- **描述**: 刷新访问令牌
- **认证**: 需要Refresh Token

**请求参数**:
```json
{
  "refreshToken": "string, required, 刷新令牌"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "刷新成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 3600
  }
}
```

### 2.4 获取用户信息
- **接口**: `GET /api/v1/users/profile`
- **描述**: 获取当前用户详细信息
- **认证**: 需要Access Token

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "userId": 123,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "avatar": "https://example.com/avatar.jpg",
    "gender": 1,
    "birthDate": "1990-01-01",
    "techStack": ["Java", "Spring", "MySQL"],
    "experienceYears": 5,
    "targetPosition": "高级开发工程师",
    "currentLevel": "中级",
    "dailyStudyTime": 60,
    "totalStudyTime": 1200,
    "totalQuestions": 150,
    "correctRate": 85.5,
    "continuousDays": 7,
    "createdAt": "2026-01-01T10:00:00Z",
    "lastLoginAt": "2026-03-17T09:30:00Z"
  }
}
```

### 2.5 更新用户信息
- **接口**: `PUT /api/v1/users/profile`
- **描述**: 更新用户个人信息
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "nickname": "string, optional, 昵称",
  "avatar": "string, optional, 头像URL",
  "gender": "integer, optional, 性别",
  "birthDate": "string, optional, 出生日期",
  "techStack": "array, optional, 技术栈",
  "experienceYears": "integer, optional, 工作年限",
  "targetPosition": "string, optional, 目标岗位",
  "dailyStudyTime": "integer, optional, 每日学习时间"
}
```

## 3. 题目管理模块

### 3.1 获取题目列表
- **接口**: `GET /api/v1/questions`
- **描述**: 分页获取题目列表
- **认证**: 可选

**查询参数**:
```
page: integer, optional, 页码，默认1
size: integer, optional, 每页数量，默认20
categoryId: integer, optional, 分类ID
difficulty: string, optional, 难度级别
tags: string, optional, 标签，逗号分隔
keyword: string, optional, 搜索关键词
sortBy: string, optional, 排序字段
sortOrder: string, optional, 排序方向
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "items": [
      {
        "questionId": 1,
        "title": "Java中HashMap的工作原理",
        "content": "请简述HashMap的工作原理...",
        "questionType": "single",
        "difficulty": "medium",
        "categoryId": 1,
        "categoryName": "Java",
        "tags": ["Java", "集合", "HashMap"],
        "viewCount": 1500,
        "answerCount": 800,
        "correctRate": 65.5,
        "favoriteCount": 120,
        "hotScore": 85.6,
        "createdAt": "2026-01-15T10:00:00Z",
        "isFavorite": false
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 150,
      "totalPages": 8
    }
  }
}
```

### 3.2 获取题目详情
- **接口**: `GET /api/v1/questions/{questionId}`
- **描述**: 获取题目详细信息
- **认证**: 可选

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "questionId": 1,
    "title": "Java中HashMap的工作原理",
    "content": "请简述HashMap的工作原理...",
    "questionType": "single",
    "difficulty": "medium",
    "categoryId": 1,
    "categoryName": "Java",
    "tags": ["Java", "集合", "HashMap"],
    "keyPoints": ["哈希冲突", "扩容机制", "线程安全"],
    "options": [
      {"id": "A", "content": "基于数组实现"},
      {"id": "B", "content": "基于链表实现"},
      {"id": "C", "content": "基于红黑树实现"},
      {"id": "D", "content": "基于哈希表实现"}
    ],
    "correctAnswer": "D",
    "answerExplanation": "HashMap是基于哈希表实现的Map接口...",
    "codeTemplate": "// 请在此处编写代码\npublic class Solution {\n    // ...\n}",
    "testCases": [
      {"input": "[]", "output": "[]", "description": "空数组测试"},
      {"input": "[1,2,3]", "output": "[1,2,3]", "description": "普通测试"}
    ],
    "language": "java",
    "sourceInfo": "来自《Java编程思想》",
    "viewCount": 1500,
    "answerCount": 800,
    "correctCount": 520,
    "favoriteCount": 120,
    "avgTimeSpent": 45,
    "createdAt": "2026-01-15T10:00:00Z",
    "relatedQuestions": [
      {"id": 2, "title": "ConcurrentHashMap的实现原理", "similarity": 0.85},
      {"id": 3, "title": "HashTable和HashMap的区别", "similarity": 0.78}
    ],
    "isFavorite": false,
    "userAnswer": null
  }
}
```

### 3.3 提交答案
- **接口**: `POST /api/v1/questions/{questionId}/answer`
- **描述**: 提交题目答案
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "userAnswer": "string, required, 用户答案",
  "timeSpent": "integer, optional, 答题耗时(秒)",
  "confidenceLevel": "integer, optional, 信心等级",
  "notes": "string, optional, 用户笔记",
  "codeSubmission": "string, optional, 代码提交",
  "sessionId": "string, optional, 答题会话ID"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "answerId": 12345,
    "questionId": 1,
    "isCorrect": true,
    "userAnswer": "D",
    "correctAnswer": "D",
    "score": 10,
    "timeSpent": 45,
    "answerExplanation": "HashMap是基于哈希表实现的Map接口...",
    "testResults": [
      {"testCaseId": 1, "passed": true, "output": "[]", "expected": "[]"},
      {"testCaseId": 2, "passed": true, "output": "[1,2,3]", "expected": "[1,2,3]"}
    ],
    "executionTime": 120,
    "memoryUsage": 256,
    "analysis": {
      "weakPoints": ["哈希冲突处理"],
      "suggestions": ["建议深入学习哈希表扩容机制"],
      "nextReviewTime": "2026-03-24T10:00:00Z"
    }
  }
}
```

### 3.4 获取分类列表
- **接口**: `GET /api/v1/categories`
- **描述**: 获取题目分类树
- **认证**: 可选

**查询参数**:
```
parentId: integer, optional, 父分类ID
level: integer, optional, 分类层级
includeStatistics: boolean, optional, 是否包含统计信息
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "categoryId": 1,
      "name": "编程语言",
      "slug": "programming-languages",
      "description": "各种编程语言相关题目",
      "icon": "https://example.com/icons/code.svg",
      "level": 1,
      "sortOrder": 1,
      "questionCount": 1500,
      "studyCount": 12000,
      "children": [
        {
          "categoryId": 11,
          "name": "Java",
          "slug": "java",
          "description": "Java相关题目",
          "questionCount": 800,
          "studyCount": 7000
        },
        {
          "categoryId": 12,
          "name": "Python",
          "slug": "python",
          "description": "Python相关题目",
          "questionCount": 500,
          "studyCount": 4000
        }
      ]
    }
  ]
}
```

## 4. 推荐算法模块

### 4.1 个性化推荐
- **接口**: `GET /api/v1/recommend/personal`
- **描述**: 获取个性化推荐题目
- **认证**: 需要Access Token

**查询参数**:
```
count: integer, optional, 推荐数量，默认10
categoryId: integer, optional, 指定分类
difficulty: string, optional, 指定难度
excludeViewed: boolean, optional, 排除已查看题目
algorithm: string, optional, 推荐算法类型
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "recommendationId": "rec_123456",
    "algorithm": "hybrid",
    "algorithmVersion": "v1.2",
    "generatedAt": "2026-03-17T10:00:00Z",
    "expiresAt": "2026-03-17T23:59:59Z",
    "recommendations": [
      {
        "questionId": 123,
        "title": "Spring Boot自动配置原理",
        "reason": "基于您的Java技术栈和近期学习记录",
        "relevanceScore": 0.92,
        "difficulty": "medium",
        "category": "后端开发",
        "tags": ["Spring Boot", "Java", "后端"],
        "estimatedTime": 120,
        "priority": "high"
      },
      {
        "questionId": 124,
        "title": "MySQL索引优化策略",
        "reason": "与您之前答错的数据库题目相关",
        "relevanceScore": 0.85,
        "difficulty": "hard",
        "category": "数据库",
        "tags": ["MySQL", "数据库", "优化"],
        "estimatedTime": 180,
        "priority": "medium"
      }
    ],
    "metadata": {
      "userFeatures": {
        "techStack": ["Java", "Spring", "MySQL"],
        "preferredDifficulty": "medium",
        "recentTopics": ["集合框架", "并发编程"]
      },
      "coverage": {
        "categoryDiversity": 0.75,
        "difficultyDistribution": {"easy": 0.2, "medium": 0.6, "hard": 0.2},
        "freshness": 0.8
      }
    }
  }
}
```

### 4.2 热点排行榜
- **接口**: `GET /api/v1/recommend/hot`
- **描述**: 获取热点题目排行榜
- **认证**: 可选

**查询参数**:
```
categoryId: integer, optional, 分类ID
difficulty: string, optional, 难度级别
timeRange: string, optional, 时间范围
limit: integer, optional, 返回数量
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "timeRange": "weekly",
    "generatedAt": "2026-03-17T10:00:00Z",
    "rankings": [
      {
        "rank": 1,
        "questionId": 456,
        "title": "Redis缓存穿透解决方案",
        "hotScore": 95.6,
        "trend": "up",
        "trendChange": 12.5,
        "viewCount": 2500,
        "answerCount": 1200,
        "correctRate": 68.3,
        "difficulty": "medium",
        "category": "数据库",
        "tags": ["Redis", "缓存", "高并发"]
      },
      {
        "rank": 2,
        "questionId": 789,
        "title": "微服务服务发现机制",
        "hotScore": 92.3,
        "trend": "up",
        "trendChange": 8.7,
        "viewCount": 1800,
        "answerCount": 950,
        "correctRate": 72.1,
        "difficulty": "hard",
        "category": "系统设计",
        "tags": ["微服务", "服务发现", "Spring Cloud"]
      }
    ],
    "analysis": {
      "hotTopics": ["微服务", "缓存", "并发"],
      "risingTopics": ["云原生", "Serverless"],
      "decliningTopics": ["单体架构", "传统部署"]
    }
  }
}
```

### 4.3 相似题推荐
- **接口**: `GET /api/v1/questions/{questionId}/similar`
- **描述**: 获取相似题目推荐
- **认证**: 可选

**查询参数**:
```
count: integer, optional, 推荐数量，默认5
minSimilarity: number, optional, 最小相似度
includeExplanation: boolean, optional, 包含解析
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "sourceQuestionId": 1,
    "sourceTitle": "Java中HashMap的工作原理",
    "similarQuestions": [
      {
        "questionId": 2,
        "title": "ConcurrentHashMap的实现原理",
        "similarity": 0.85,
        "similarityReason": "相同数据结构，不同并发实现",
        "difficulty": "hard",
        "keyPoints": ["线程安全", "分段锁", "性能优化"],
        "answerCount": 600,
        "correctRate": 58.2
      },
      {
        "questionId": 3,
        "title": "HashTable和HashMap的区别",
        "similarity": 0.78,
        "similarityReason": "同类数据结构的比较",
        "difficulty": "medium",
        "keyPoints": ["线程安全", "性能", "null值处理"],
        "answerCount": 450,
        "correctRate": 62.5
      }
    ],
    "algorithm": "content_based",
    "featureWeights": {
      "title": 0.3,
      "content": 0.4,
      "tags": 0.2,
      "keyPoints": 0.1
    }
  }
}
```

## 5. 错题集模块

### 5.1 获取错题列表
- **接口**: `GET /api/v1/wrong-answers`
- **描述**: 获取用户错题列表
- **认证**: 需要Access Token

**查询参数**:
```
collectionId: integer, optional, 错题集ID
categoryId: integer, optional, 分类ID
difficulty: string, optional, 难度级别
status: string, optional, 掌握状态
page: integer, optional, 页码
size: integer, optional, 每页数量
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "items": [
      {
        "wrongAnswerId": 123,
        "questionId": 456,
        "questionTitle": "Spring事务传播机制",
        "category": "Spring",
        "difficulty": "medium",
        "userAnswer": "REQUIRES_NEW",
        "correctAnswer": "REQUIRED",
        "wrongReason": "对事务传播行为理解错误",
        "notes": "需要重点复习Spring事务",
        "errorCount": 3,
        "masteryLevel": 2,
        "lastErrorAt": "2026-03-15T14:30:00Z",
        "nextReviewAt": "2026-03-22T10:00:00Z",
        "reviewCount": 2,
        "isMastered": false
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "total": 45,
      "totalPages": 3
    },
    "statistics": {
      "totalCount": 45,
      "masteredCount": 15,
      "pendingReviewCount": 20,
      "avgMasteryLevel": 2.8,
      "byCategory": {"Spring": 10, "MySQL": 8, "并发": 12},
      "byDifficulty": {"easy": 5, "medium": 25, "hard": 15}
    }
  }
}
```

### 5.2 创建错题集
- **接口**: `POST /api/v1/wrong-answer-collections`
- **描述**: 创建新的错题集
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "name": "string, required, 错题集名称",
  "description": "string, optional, 错题集描述",
  "tags": "array, optional, 标签",
  "categoryId": "integer, optional, 分类ID",
  "isPublic": "boolean, optional, 是否公开",
  "reviewIntervalDays": "integer, optional, 复习间隔天数",
  "dailyReviewLimit": "integer, optional, 每日复习限制"
}
```

### 5.3 添加到错题集
- **接口**: `POST /api/v1/wrong-answer-collections/{collectionId}/items`
- **描述**: 添加题目到错题集
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "questionId": "integer, required, 题目ID",
  "userAnswerId": "integer, optional, 答题记录ID",
  "wrongReason": "string, optional, 错误原因",
  "correctUnderstanding": "string, optional, 正确理解",
  "notes": "string, optional, 个人笔记"
}
```

### 5.4 错题专项练习
- **接口**: `GET /api/v1/wrong-answers/practice`
- **描述**: 生成错题专项练习
- **认证**: 需要Access Token

**查询参数**:
```
collectionId: integer, optional, 错题集ID
categoryId: integer, optional, 分类ID
difficulty: string, optional, 难度级别
count: integer, optional, 题目数量
includeNew: boolean, optional, 包含新题
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "practiceId": "practice_123456",
    "name": "Spring框架错题专项练习",
    "description": "基于您的错题记录生成的专项练习",
    "totalQuestions": 10,
    "estimatedTime": 600,
    "categories": ["Spring", "事务", "AOP"],
    "difficultyDistribution": {"easy": 2, "medium": 5, "hard": 3},
    "questions": [
      {
        "questionId": 123,
        "title": "Spring事务传播机制",
        "difficulty": "medium",
        "previousErrors": 3,
        "lastErrorAt": "2026-03-15T14:30:00Z",
        "masteryLevel": 2,
        "suggestedFocus": "事务传播行为定义"
      }
    ],
    "learningObjectives": [
      "掌握Spring事务传播机制",
      "理解AOP代理原理",
      "熟悉Bean生命周期"
    ]
  }
}
```

## 6. 收藏夹模块

### 6.1 获取收藏夹列表
- **接口**: `GET /api/v1/favorites/folders`
- **描述**: 获取用户收藏夹列表
- **认证**: 需要Access Token

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "folderId": 1,
      "name": "Java核心",
      "description": "Java核心知识点收藏",
      "itemCount": 25,
      "isDefault": true,
      "isPublic": false,
      "createdAt": "2026-01-10T10:00:00Z",
      "updatedAt": "2026-03-15T14:30:00Z"
    },
    {
      "folderId": 2,
      "name": "系统设计",
      "description": "系统设计相关题目",
      "itemCount": 15,
      "isDefault": false,
      "isPublic": true,
      "createdAt": "2026-02-05T11:00:00Z",
      "updatedAt": "2026-03-10T09:45:00Z"
    }
  ]
}
```

### 6.2 收藏题目
- **接口**: `POST /api/v1/questions/{questionId}/favorite`
- **描述**: 收藏题目
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "folderId": "integer, optional, 收藏夹ID",
  "tags": "array, optional, 标签",
  "notes": "string, optional, 收藏笔记",
  "importanceLevel": "integer, optional, 重要程度"
}
```

### 6.3 获取收藏题目列表
- **接口**: `GET /api/v1/favorites/items`
- **描述**: 获取收藏的题目列表
- **认证**: 需要Access Token

**查询参数**:
```
folderId: integer, optional, 收藏夹ID
categoryId: integer, optional, 分类ID
tags: string, optional, 标签，逗号分隔
importanceLevel: integer, optional, 重要程度
page: integer, optional, 页码
size: integer, optional, 每页数量
```

## 7. 模拟考试模块

### 7.1 获取试卷列表
- **接口**: `GET /api/v1/exams/papers`
- **描述**: 获取试卷列表
- **认证**: 需要Access Token

**查询参数**:
```
type: string, optional, 试卷类型
difficulty: string, optional, 难度级别
categoryId: integer, optional, 分类ID
isPublic: boolean, optional, 是否公开
page: integer, optional, 页码
size: integer, optional, 每页数量
```

### 7.2 开始模拟考试
- **接口**: `POST /api/v1/exams/start`
- **描述**: 开始新的模拟考试
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "paperId": "integer, optional, 试卷ID",
  "config": {
    "categoryId": "integer, optional, 分类ID",
    "difficulty": "string, optional, 难度",
    "questionCount": "integer, optional, 题目数量",
    "timeLimit": "integer, optional, 时间限制",
    "name": "string, optional, 考试名称"
  }
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "examId": "exam_123456",
    "paperId": 1,
    "paperName": "Java高级开发模拟考试",
    "totalQuestions": 20,
    "timeLimit": 1800,
    "totalScore": 200,
    "passingScore": 120,
    "startTime": "2026-03-17T10:00:00Z",
    "endTime": "2026-03-17T10:30:00Z",
    "questions": [
      {
        "questionId": 123,
        "questionNumber": 1,
        "title": "Java内存模型",
        "content": "请简述Java内存模型...",
        "questionType": "single",
        "score": 10,
        "timeLimit": 90
      }
    ],
    "sessionToken": "exam_session_token"
  }
}
```

### 7.3 提交考试答案
- **接口**: `POST /api/v1/exams/{examId}/submit`
- **描述**: 提交考试答案
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "answers": [
    {
      "questionId": 123,
      "userAnswer": "string, required, 用户答案",
      "timeSpent": 45
    }
  ],
  "submitTime": "string, optional, 提交时间"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "examId": "exam_123456",
    "totalScore": 200,
    "obtainedScore": 165,
    "correctCount": 16,
    "wrongCount": 3,
    "unansweredCount": 1,
    "timeSpent": 1620,
    "isPassed": true,
    "performanceAnalysis": {
      "byCategory": {
        "Java基础": {"score": 45, "total": 50, "rate": 90},
        "并发编程": {"score": 40, "total": 50, "rate": 80},
        "JVM": {"score": 35, "total": 50, "rate": 70}
      },
      "byDifficulty": {
        "easy": {"score": 30, "total": 30, "rate": 100},
        "medium": {"score": 75, "total": 100, "rate": 75},
        "hard": {"score": 60, "total": 70, "rate": 85.7}
      },
      "weakAreas": ["JVM垃圾回收", "并发工具类"],
      "strengths": ["集合框架", "IO/NIO"],
      "recommendations": [
        "建议重点复习JVM内存管理",
        "加强并发工具类的学习"
      ]
    },
    "ranking": {
      "percentile": 85,
      "totalParticipants": 1000,
      "betterThan": "85%的考生"
    }
  }
}
```

## 8. 学习分析模块

### 8.1 获取学习统计
- **接口**: `GET /api/v1/analytics/study`
- **描述**: 获取用户学习统计数据
- **认证**: 需要Access Token

**查询参数**:
```
timeRange: string, optional, 时间范围
categoryId: integer, optional, 分类ID
groupBy: string, optional, 分组方式
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "timeRange": "monthly",
    "startDate": "2026-02-01",
    "endDate": "2026-03-17",
    "overview": {
      "totalStudyTime": 1800,
      "totalQuestions": 150,
      "correctRate": 82.5,
      "continuousDays": 15,
      "avgDailyStudyTime": 120,
      "completionRate": 75.3
    },
    "dailyProgress": [
      {"date": "2026-03-01", "studyTime": 135, "questions": 12, "correctRate": 83.3},
      {"date": "2026-03-02", "studyTime": 120, "questions": 10, "correctRate": 80.0}
    ],
    "byCategory": [
      {"category": "Java", "studyTime": 600, "questions": 50, "correctRate": 85.0},
      {"category": "数据库", "studyTime": 450, "questions": 40, "correctRate": 80.0},
      {"category": "并发", "studyTime": 300, "questions": 30, "correctRate": 75.0}
    ],
    "byDifficulty": [
      {"difficulty": "easy", "questions": 30, "correctRate": 95.0},
      {"difficulty": "medium", "questions": 80, "correctRate": 85.0},
      {"difficulty": "hard", "questions": 40, "correctRate": 65.0}
    ],
    "trends": {
      "studyTimeTrend": "up",
      "correctRateTrend": "up",
      "difficultyProgress": "improving"
    },
    "insights": [
      "您在Java方面的表现优秀，正确率达到85%",
      "并发编程是您的薄弱环节，建议加强练习",
      "学习连续性良好，已连续学习15天"
    ]
  }
}
```

### 8.2 获取能力评估
- **接口**: `GET /api/v1/analytics/assessment`
- **描述**: 获取用户能力评估报告
- **认证**: 需要Access Token

**响应示例**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "assessmentId": "assess_123456",
    "generatedAt": "2026-03-17T10:00:00Z",
    "overallScore": 78.5,
    "level": "中级开发工程师",
    "dimensions": [
      {
        "name": "基础知识",
        "score": 85.0,
        "weight": 0.3,
        "details": {
          "数据结构": 90,
          "算法": 80,
          "设计模式": 85
        }
      },
      {
        "name": "专业技能",
        "score": 75.0,
        "weight": 0.4,
        "details": {
          "Java核心": 85,
          "Spring框架": 80,
          "数据库": 70,
          "并发编程": 65
        }
      },
      {
        "name": "系统设计",
        "score": 70.0,
        "weight": 0.3,
        "details": {
          "架构设计": 75,
          "性能优化": 70,
          "高可用": 65
        }
      }
    ],
    "strengths": [
      "数据结构掌握扎实",
      "Java核心知识全面",
      "设计模式应用熟练"
    ],
    "weaknesses": [
      "并发编程需要加强",
      "数据库优化经验不足",
      "高可用方案了解有限"
    ],
    "recommendations": [
      {
        "priority": "high",
        "area": "并发编程",
        "action": "完成并发编程专项练习",
        "resources": ["JUC包详解", "并发设计模式"]
      },
      {
        "priority": "medium",
        "area": "数据库优化",
        "action": "学习数据库索引优化",
        "resources": ["MySQL性能优化", "SQL调优实战"]
      }
    ],
    "comparison": {
      "percentile": 75,
      "betterThan": "75%的同水平用户",
      "targetPosition": "高级开发工程师",
      "gapAnalysis": "还需提升系统设计能力"
    }
  }
}
```

## 9. 系统管理模块

### 9.1 提交反馈
- **接口**: `POST /api/v1/feedbacks`
- **描述**: 提交用户反馈
- **认证**: 需要Access Token

**请求参数**:
```json
{
  "type": "string, required, 反馈类型",
  "title": "string, required, 反馈标题",
  "content": "string, required, 反馈内容",
  "relatedType": "string, optional, 关联类型",
  "relatedId": "integer, optional, 关联ID",
  "attachments": "array, optional, 附件信息"
}
```

### 9.2 获取通知列表
- **接口**: `GET /api/v1/notifications`
- **描述**: 获取用户通知列表
- **认证**: 需要Access Token

**查询参数**:
```
type: string, optional, 通知类型
isRead: boolean, optional, 是否已读
page: integer, optional, 页码
size: integer, optional, 每页数量
```

## 10. WebSocket接口

### 10.1 实时答题
- **连接地址**: `ws://api.example.com/ws/answer`
- **描述**: 实时答题和代码执行

**消息格式**:
```json
// 客户端发送
{
  "type": "code_execution",
  "examId": "exam_123456",
  "questionId": 123,
  "code": "public class Solution {...}",
  "language": "java"
}

// 服务器响应
{
  "type": "execution_result",
  "questionId": 123,
  "results": [
    {"testCaseId": 1, "passed": true, "output": "[]"},
    {"testCaseId": 2, "passed": true, "output": "[1,2,3]"}
  ],
  "executionTime": 120,
  "memoryUsage": 256
}
```

### 10.2 实时排名
- **连接地址**: `ws://api.example.com/ws/ranking`
- **描述**: 实时考试排名更新

**消息格式**:
```json
{
  "type": "ranking_update",
  "examId": "exam_123456",
  "rankings": [
    {"userId": 123, "username": "user1", "score": 185, "timeSpent": 1500},
    {"userId": 124, "username": "user2", "score": 175, "timeSpent": 1600}
  ],
  "myRank": 2,
  "totalParticipants": 100
}
```

## 11. 文件上传接口

### 11.1 上传头像
- **接口**: `POST /api/v1/upload/avatar`
- **描述**: 上传用户头像
- **认证**: 需要Access Token
- **Content-Type**: `multipart/form-data`

**请求参数**:
```
file: file, required, 图片文件
```

**响应示例**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://cdn.example.com/avatars/123.jpg",
    "size": 102400,
    "width": 200,
    "height": 200,
    "format": "jpeg"
  }
}
```

### 11.2 上传题目附件
- **接口**: `POST /api/v1/upload/question-attachment`
- **描述**: 上传题目相关附件
- **认证**: 需要Access Token
- **Content-Type**: `multipart/form-data`

**请求参数**:
```
file: file, required, 文件
questionId: integer, optional, 关联题目ID
type: string, required, 文件类型
```

## 12. API监控和限流

### 12.1 限流策略
| 接口类型 | 限流规则 | 说明 |
|----------|----------|------|
| 公开接口 | 100次/分钟/IP | 防止爬虫和恶意访问 |
| 认证接口 | 50次/分钟/用户 | 保护用户账户安全 |
| 高频接口 | 1000次/分钟/用户 | 答题、搜索等高频率操作 |
| 管理接口 | 10次/分钟/用户 | 系统管理操作限制 |

### 12.2 监控指标
1. **性能指标**:
   - 接口响应时间(P95 < 500ms)
   - 接口成功率(> 99.9%)
   - 并发连接数

2. **业务指标**:
   - 每日API调用量
   - 用户活跃度
   - 接口错误率

3. **安全指标**:
   - 异常访问检测
   - 恶意请求拦截
   - 认证失败率

---

*本文档为API接口规划初稿，将在项目实施过程中持续完善*  
*最后更新: 2026-03-17*  
*版本: v1.0*