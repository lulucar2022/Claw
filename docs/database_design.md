# 程序员面试知识答题系统 - 数据库表结构设计

## 1. 数据库设计原则

### 1.1 设计原则
- **范式化设计**: 满足第三范式，减少数据冗余
- **性能优先**: 合理索引，查询优化
- **扩展性**: 支持水平分表，业务增长
- **安全性**: 敏感数据加密，权限隔离
- **一致性**: 事务保证，数据一致

### 1.2 命名规范
- **表名**: 小写，下划线分隔，复数形式，如 `users`, `questions`
- **字段名**: 小写，下划线分隔，如 `user_id`, `created_at`
- **主键**: `id` (bigint, 自增)
- **外键**: `xxx_id` 格式，如 `user_id`
- **时间字段**: `created_at`, `updated_at`, `deleted_at`

## 2. 核心表结构设计

### 2.1 用户模块

#### 表: users (用户表)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    salt VARCHAR(50) NOT NULL COMMENT '密码盐值',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    birth_date DATE COMMENT '出生日期',
    
    -- 技术信息
    tech_stack VARCHAR(500) COMMENT '技术栈，JSON数组',
    experience_years TINYINT DEFAULT 0 COMMENT '工作年限',
    target_position VARCHAR(50) COMMENT '目标岗位',
    current_level VARCHAR(50) COMMENT '当前水平',
    
    -- 学习信息
    daily_study_time INT DEFAULT 30 COMMENT '每日学习时间(分钟)',
    study_goal TEXT COMMENT '学习目标',
    preferred_difficulty VARCHAR(20) DEFAULT 'medium' COMMENT '偏好难度',
    
    -- 状态信息
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常, 2-锁定',
    email_verified BOOLEAN DEFAULT FALSE COMMENT '邮箱验证',
    phone_verified BOOLEAN DEFAULT FALSE COMMENT '手机验证',
    
    -- 统计信息
    total_study_time INT DEFAULT 0 COMMENT '总学习时长(分钟)',
    total_questions INT DEFAULT 0 COMMENT '总答题数',
    correct_rate DECIMAL(5,2) DEFAULT 0 COMMENT '正确率',
    continuous_days INT DEFAULT 0 COMMENT '连续学习天数',
    
    -- 时间戳
    last_login_at DATETIME COMMENT '最后登录时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME COMMENT '删除时间',
    
    -- 索引
    INDEX idx_username(username),
    INDEX idx_email(email),
    INDEX idx_status(status),
    INDEX idx_created_at(created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

#### 表: user_profiles (用户扩展信息表)
```sql
CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    
    -- 教育背景
    education_level VARCHAR(50) COMMENT '学历',
    school_name VARCHAR(100) COMMENT '学校名称',
    major VARCHAR(100) COMMENT '专业',
    graduation_year YEAR COMMENT '毕业年份',
    
    -- 工作经历
    company_name VARCHAR(100) COMMENT '公司名称',
    job_title VARCHAR(100) COMMENT '职位',
    work_years TINYINT COMMENT '工作年限',
    industry VARCHAR(100) COMMENT '行业',
    
    -- 技能评估
    skill_assessment JSON COMMENT '技能评估，JSON格式',
    weak_points JSON COMMENT '薄弱点，JSON格式',
    strong_points JSON COMMENT '优势点，JSON格式',
    
    -- 学习偏好
    learning_style VARCHAR(50) COMMENT '学习风格',
    preferred_topics JSON COMMENT '偏好主题，JSON数组',
    avoid_topics JSON COMMENT '避免主题，JSON数组',
    
    -- 备考信息
    exam_target VARCHAR(100) COMMENT '考试目标',
    exam_date DATE COMMENT '考试日期',
    preparation_plan TEXT COMMENT '备考计划',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 外键和索引
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_id(user_id),
    INDEX idx_user_id(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户扩展信息表';
```

#### 表: user_sessions (用户会话表)
```sql
CREATE TABLE user_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    session_token VARCHAR(255) NOT NULL COMMENT '会话令牌',
    refresh_token VARCHAR(255) COMMENT '刷新令牌',
    device_info VARCHAR(500) COMMENT '设备信息',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    
    -- 会话状态
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否活跃',
    expires_at DATETIME NOT NULL COMMENT '过期时间',
    last_activity_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后活动时间',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_session_token(session_token),
    INDEX idx_expires_at(expires_at),
    INDEX idx_is_active(is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';
```

### 2.2 题目模块

#### 表: question_categories (题目分类表)
```sql
CREATE TABLE question_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    slug VARCHAR(100) NOT NULL UNIQUE COMMENT '分类标识',
    description TEXT COMMENT '分类描述',
    icon_url VARCHAR(500) COMMENT '图标URL',
    
    -- 层级信息
    level TINYINT DEFAULT 1 COMMENT '层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_leaf BOOLEAN DEFAULT FALSE COMMENT '是否叶子节点',
    
    -- 统计信息
    question_count INT DEFAULT 0 COMMENT '题目数量',
    study_count INT DEFAULT 0 COMMENT '学习次数',
    
    -- 状态
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_parent_id(parent_id),
    INDEX idx_slug(slug),
    INDEX idx_sort_order(sort_order),
    INDEX idx_is_active(is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目分类表';
```

#### 表: questions (题目表)
```sql
CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    title VARCHAR(500) NOT NULL COMMENT '题目标题',
    content TEXT NOT NULL COMMENT '题目内容',
    question_type VARCHAR(50) NOT NULL COMMENT '题目类型: single/multiple/code/fill/short',
    
    -- 难度和标签
    difficulty VARCHAR(20) DEFAULT 'medium' COMMENT '难度: easy/medium/hard/expert',
    tags JSON COMMENT '标签，JSON数组',
    key_points JSON COMMENT '考点，JSON数组',
    
    -- 选项（针对选择题）
    options JSON COMMENT '选项，JSON数组',
    correct_answer TEXT COMMENT '正确答案',
    answer_explanation TEXT COMMENT '答案解析',
    
    -- 代码题特殊字段
    code_template TEXT COMMENT '代码模板',
    test_cases JSON COMMENT '测试用例，JSON数组',
    language VARCHAR(50) COMMENT '编程语言',
    
    -- 来源信息
    source_type VARCHAR(50) COMMENT '来源类型: system/user/import',
    source_info VARCHAR(500) COMMENT '来源信息',
    original_url VARCHAR(500) COMMENT '原始URL',
    
    -- 统计信息
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    answer_count INT DEFAULT 0 COMMENT '答题次数',
    correct_count INT DEFAULT 0 COMMENT '正确次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    wrong_count INT DEFAULT 0 COMMENT '错误次数',
    avg_time_spent INT COMMENT '平均耗时(秒)',
    
    -- 热度信息
    hot_score DECIMAL(10,4) DEFAULT 0 COMMENT '热度分数',
    trend_score DECIMAL(10,4) DEFAULT 0 COMMENT '趋势分数',
    
    -- 状态
    status TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-审核中, 2-已发布, 3-下架',
    is_recommended BOOLEAN DEFAULT FALSE COMMENT '是否推荐',
    is_hot BOOLEAN DEFAULT FALSE COMMENT '是否热门',
    
    -- 审核信息
    reviewer_id BIGINT COMMENT '审核人ID',
    reviewed_at DATETIME COMMENT '审核时间',
    review_comment TEXT COMMENT '审核意见',
    
    -- 时间戳
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    published_at DATETIME COMMENT '发布时间',
    
    -- 索引
    INDEX idx_category_id(category_id),
    INDEX idx_difficulty(difficulty),
    INDEX idx_status(status),
    INDEX idx_hot_score(hot_score),
    INDEX idx_created_at(created_at),
    INDEX idx_published_at(published_at),
    FULLTEXT idx_title_content(title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';
```

#### 表: question_tags (题目标签表)
```sql
CREATE TABLE question_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    name VARCHAR(100) NOT NULL COMMENT '标签名称',
    slug VARCHAR(100) NOT NULL UNIQUE COMMENT '标签标识',
    description TEXT COMMENT '标签描述',
    color VARCHAR(20) COMMENT '标签颜色',
    icon_url VARCHAR(500) COMMENT '图标URL',
    
    -- 统计信息
    question_count INT DEFAULT 0 COMMENT '题目数量',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    
    -- 状态
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_slug(slug),
    INDEX idx_is_active(is_active),
    INDEX idx_question_count(question_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目标签表';
```

#### 表: question_relations (题目关联表)
```sql
CREATE TABLE question_relations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    related_question_id BIGINT NOT NULL COMMENT '关联题目ID',
    relation_type VARCHAR(50) NOT NULL COMMENT '关联类型: similar/prerequisite/next',
    similarity_score DECIMAL(5,4) COMMENT '相似度分数',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    -- 索引和约束
    UNIQUE KEY uk_question_relation(question_id, related_question_id, relation_type),
    INDEX idx_question_id(question_id),
    INDEX idx_related_question_id(related_question_id),
    INDEX idx_relation_type(relation_type),
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (related_question_id) REFERENCES questions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目关联表';
```

### 2.3 答题模块

#### 表: user_answers (用户答题记录表)
```sql
CREATE TABLE user_answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    session_id VARCHAR(100) COMMENT '答题会话ID',
    
    -- 答题信息
    user_answer TEXT COMMENT '用户答案',
    is_correct BOOLEAN COMMENT '是否正确',
    score DECIMAL(5,2) COMMENT '得分',
    time_spent INT COMMENT '耗时(秒)',
    
    -- 代码题特殊字段
    code_submission TEXT COMMENT '提交的代码',
    test_results JSON COMMENT '测试结果，JSON数组',
    execution_time INT COMMENT '执行时间(ms)',
    memory_usage INT COMMENT '内存使用(KB)',
    
    -- 分析信息
    confidence_level TINYINT COMMENT '信心等级: 1-5',
    difficulty_perception TINYINT COMMENT '难度感知: 1-5',
    notes TEXT COMMENT '用户笔记',
    
    -- 状态
    is_reviewed BOOLEAN DEFAULT FALSE COMMENT '是否已复习',
    review_count INT DEFAULT 0 COMMENT '复习次数',
    
    -- 时间戳
    answered_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
    reviewed_at DATETIME COMMENT '复习时间',
    next_review_at DATETIME COMMENT '下次复习时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_question_id(question_id),
    INDEX idx_is_correct(is_correct),
    INDEX idx_answered_at(answered_at),
    INDEX idx_user_question(user_id, question_id),
    INDEX idx_next_review_at(next_review_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户答题记录表';
```

#### 表: wrong_answer_collections (错题集表)
```sql
CREATE TABLE wrong_answer_collections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '错题集ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '错题集名称',
    description TEXT COMMENT '错题集描述',
    
    -- 标签信息
    tags JSON COMMENT '标签，JSON数组',
    category_id BIGINT COMMENT '关联分类ID',
    
    -- 统计信息
    question_count INT DEFAULT 0 COMMENT '题目数量',
    review_count INT DEFAULT 0 COMMENT '复习次数',
    mastery_rate DECIMAL(5,2) DEFAULT 0 COMMENT '掌握率',
    
    -- 设置
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    review_interval_days INT DEFAULT 7 COMMENT '复习间隔天数',
    daily_review_limit INT DEFAULT 10 COMMENT '每日复习限制',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_reviewed_at DATETIME COMMENT '最后复习时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_category_id(category_id),
    INDEX idx_created_at(created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES question_categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错题集表';
```

#### 表: wrong_answer_items (错题项表)
```sql
CREATE TABLE wrong_answer_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '错题项ID',
    collection_id BIGINT NOT NULL COMMENT '错题集ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    user_answer_id BIGINT NOT NULL COMMENT '答题记录ID',
    
    -- 错题信息
    wrong_reason VARCHAR(200) COMMENT '错误原因',
    correct_understanding TEXT COMMENT '正确理解',
    notes TEXT COMMENT '个人笔记',
    
    -- 掌握情况
    mastery_level TINYINT DEFAULT 1 COMMENT '掌握等级: 1-5',
    error_count INT DEFAULT 1 COMMENT '错误次数',
    last_error_at DATETIME COMMENT '最后错误时间',
    
    -- 复习信息
    review_count INT DEFAULT 0 COMMENT '复习次数',
    is_mastered BOOLEAN DEFAULT FALSE COMMENT '是否已掌握',
    mastered_at DATETIME COMMENT '掌握时间',
    
    -- 时间戳
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    last_reviewed_at DATETIME COMMENT '最后复习时间',
    next_review_at DATETIME COMMENT '下次复习时间',
    
    -- 索引
    UNIQUE KEY uk_collection_question(collection_id, question_id),
    INDEX idx_collection_id(collection_id),
    INDEX idx_question_id(question_id),
    INDEX idx_next_review_at(next_review_at),
    INDEX idx_mastery_level(mastery_level),
    FOREIGN KEY (collection_id) REFERENCES wrong_answer_collections(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (user_answer_id) REFERENCES user_answers(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错题项表';
```

### 2.4 收藏模块

#### 表: user_favorites (用户收藏表)
```sql
CREATE TABLE user_favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    
    -- 收藏信息
    folder_id BIGINT COMMENT '收藏夹ID',
    tags JSON COMMENT '标签，JSON数组',
    notes TEXT COMMENT '收藏笔记',
    importance_level TINYINT DEFAULT 1 COMMENT '重要程度: 1-5',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    UNIQUE KEY uk_user_question(user_id, question_id),
    INDEX idx_user_id(user_id),
    INDEX idx_question_id(question_id),
    INDEX idx_folder_id(folder_id),
    INDEX idx_created_at(created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';
```

#### 表: favorite_folders (收藏夹表)
```sql
CREATE TABLE favorite_folders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏夹ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '收藏夹名称',
    description TEXT COMMENT '收藏夹描述',
    
    -- 设置
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认收藏夹',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    sort_order INT DEFAULT 0 COMMENT '排序',
    
    -- 统计信息
    item_count INT DEFAULT 0 COMMENT '收藏项数量',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_is_default(is_default),
    INDEX idx_created_at(created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏夹表';
```

### 2.5 推荐算法模块

#### 表: user_behavior_logs (用户行为日志表)
```sql
CREATE TABLE user_behavior_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    session_id VARCHAR(100) COMMENT '会话ID',
    
    -- 行为信息
    behavior_type VARCHAR(50) NOT NULL COMMENT '行为类型: view/answer/favorite/share',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型: question/category',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    
    -- 行为详情
    behavior_detail JSON COMMENT '行为详情，JSON格式',
    duration INT COMMENT '持续时间(秒)',
    scroll_depth INT COMMENT '滚动深度(百分比)',
    
    -- 上下文信息
    page_url VARCHAR(500) COMMENT '页面URL',
    referrer_url VARCHAR(500) COMMENT '来源URL',
    device_type VARCHAR(50) COMMENT '设备类型',
    browser_type VARCHAR(50) COMMENT '浏览器类型',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_behavior_type(behavior_type),
    INDEX idx_target_type_target_id(target_type, target_id),
    INDEX idx_created_at(created_at),
    INDEX idx_user_behavior(user_id, behavior_type, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户行为日志表';
```

#### 表: user_preferences (用户偏好表)
```sql
CREATE TABLE user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '偏好ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    
    -- 技术偏好
    preferred_technologies JSON COMMENT '偏好技术，JSON数组',
    avoided_technologies JSON COMMENT '避免技术，JSON数组',
    
    -- 难度偏好
    preferred_difficulty_distribution JSON COMMENT '难度分布偏好',
    
    -- 学习时间偏好
    study_time_preferences JSON COMMENT '学习时间偏好',
    
    -- 推荐偏好
    recommendation_preferences JSON COMMENT '推荐偏好设置',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    UNIQUE KEY uk_user_id(user_id),
    INDEX idx_user_id(user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好表';
```

#### 表: recommendation_results (推荐结果表)
```sql
CREATE TABLE recommendation_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推荐ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    
    -- 推荐信息
    recommendation_type VARCHAR(50) NOT NULL COMMENT '推荐类型: personal/hot/similar',
    algorithm_version VARCHAR(50) NOT NULL COMMENT '算法版本',
    recommended_items JSON NOT NULL COMMENT '推荐项，JSON数组',
    
    -- 特征信息
    user_features JSON COMMENT '用户特征',
    context_features JSON COMMENT '上下文特征',
    
    -- 效果跟踪
    is_viewed BOOLEAN DEFAULT FALSE COMMENT '是否被查看',
    view_count INT DEFAULT 0 COMMENT '查看次数',
    click_count INT DEFAULT 0 COMMENT '点击次数',
    conversion_rate DECIMAL(5,4) COMMENT '转化率',
    
    -- 时间戳
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    expires_at DATETIME COMMENT '过期时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_recommendation_type(recommendation_type),
    INDEX idx_generated_at(generated_at),
    INDEX idx_user_type_time(user_id, recommendation_type, generated_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐结果表';
```

### 2.6 模拟考试模块

#### 表: exam_papers (试卷表)
```sql
CREATE TABLE exam_papers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '试卷ID',
    user_id BIGINT COMMENT '用户ID（自定义试卷）',
    name VARCHAR(200) NOT NULL COMMENT '试卷名称',
    description TEXT COMMENT '试卷描述',
    
    -- 试卷设置
    paper_type VARCHAR(50) NOT NULL COMMENT '试卷类型: system/custom/simulation',
    difficulty_level VARCHAR(20) COMMENT '难度等级',
    time_limit INT COMMENT '时间限制(分钟)',
    total_score INT COMMENT '总分',
    passing_score INT COMMENT '及格分',
    
    -- 题目配置
    question_config JSON NOT NULL COMMENT '题目配置，JSON格式',
    question_count INT DEFAULT 0 COMMENT '题目数量',
    
    -- 统计信息
    attempt_count INT DEFAULT 0 COMMENT '尝试次数',
    avg_score DECIMAL(5,2) DEFAULT 0 COMMENT '平均分',
    avg_time_spent INT DEFAULT 0 COMMENT '平均耗时(秒)',
    
    -- 状态
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_paper_type(paper_type),
    INDEX idx_difficulty_level(difficulty_level),
    INDEX idx_created_at(created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷表';
```

#### 表: exam_questions (试卷题目关联表)
```sql
CREATE TABLE exam_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    exam_paper_id BIGINT NOT NULL COMMENT '试卷ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    
    -- 题目在试卷中的设置
    question_number INT COMMENT '题号',
    score INT DEFAULT 10 COMMENT '分值',
    sort_order INT DEFAULT 0 COMMENT '排序',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    -- 索引和约束
    UNIQUE KEY uk_exam_question(exam_paper_id, question_id),
    INDEX idx_exam_paper_id(exam_paper_id),
    INDEX idx_question_id(question_id),
    FOREIGN KEY (exam_paper_id) REFERENCES exam_papers(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷题目关联表';
```

#### 表: exam_records (考试记录表)
```sql
CREATE TABLE exam_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '考试记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    exam_paper_id BIGINT NOT NULL COMMENT '试卷ID',
    
    -- 考试信息
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    submit_time DATETIME COMMENT '提交时间',
    time_spent INT COMMENT '耗时(秒)',
    
    -- 成绩信息
    total_score INT DEFAULT 0 COMMENT '总分',
    obtained_score INT DEFAULT 0 COMMENT '得分',
    correct_count INT DEFAULT 0 COMMENT '正确数',
    wrong_count INT DEFAULT 0 COMMENT '错误数',
    unanswered_count INT DEFAULT 0 COMMENT '未答题数',
    
    -- 状态
    status VARCHAR(50) DEFAULT 'in_progress' COMMENT '状态: in_progress/completed/terminated',
    is_passed BOOLEAN DEFAULT FALSE COMMENT '是否通过',
    
    -- 分析信息
    performance_analysis JSON COMMENT '表现分析，JSON格式',
    weak_categories JSON COMMENT '薄弱分类，JSON数组',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_exam_paper_id(exam_paper_id),
    INDEX idx_status(status),
    INDEX idx_created_at(created_at),
    INDEX idx_user_exam(user_id, exam_paper_id, created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (exam_paper_id) REFERENCES exam_papers(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考试记录表';
```

### 2.7 反馈与系统模块

#### 表: user_feedbacks (用户反馈表)
```sql
CREATE TABLE user_feedbacks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '反馈ID',
    user_id BIGINT COMMENT '用户ID',
    
    -- 反馈内容
    feedback_type VARCHAR(50) NOT NULL COMMENT '反馈类型: bug/suggestion/question/other',
    title VARCHAR(200) NOT NULL COMMENT '反馈标题',
    content TEXT NOT NULL COMMENT '反馈内容',
    
    -- 关联信息
    related_type VARCHAR(50) COMMENT '关联类型: question/answer/system',
    related_id BIGINT COMMENT '关联ID',
    
    -- 附件
    attachments JSON COMMENT '附件信息，JSON数组',
    
    -- 状态
    status VARCHAR(50) DEFAULT 'pending' COMMENT '状态: pending/processing/resolved/closed',
    priority TINYINT DEFAULT 2 COMMENT '优先级: 1-紧急, 2-高, 3-中, 4-低',
    
    -- 处理信息
    processor_id BIGINT COMMENT '处理人ID',
    processed_at DATETIME COMMENT '处理时间',
    process_comment TEXT COMMENT '处理意见',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_feedback_type(feedback_type),
    INDEX idx_status(status),
    INDEX idx_created_at(created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表';
```

#### 表: system_notifications (系统通知表)
```sql
CREATE TABLE system_notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    
    -- 通知内容
    notification_type VARCHAR(50) NOT NULL COMMENT '通知类型: system/recommendation/reminder/achievement',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    
    -- 关联信息
    related_type VARCHAR(50) COMMENT '关联类型',
    related_id BIGINT COMMENT '关联ID',
    
    -- 状态
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    is_important BOOLEAN DEFAULT FALSE COMMENT '是否重要',
    
    -- 时间戳
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    read_at DATETIME COMMENT '阅读时间',
    expires_at DATETIME COMMENT '过期时间',
    
    -- 索引
    INDEX idx_user_id(user_id),
    INDEX idx_notification_type(notification_type),
    INDEX idx_is_read(is_read),
    INDEX idx_created_at(created_at),
    INDEX idx_user_read_status(user_id, is_read, created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统通知表';
```

## 3. 数据库优化策略

### 3.1 索引策略
1. **主键索引**: 所有表使用自增主键
2. **唯一索引**: 业务唯一约束字段
3. **组合索引**: 常用查询条件组合
4. **全文索引**: 搜索字段使用全文索引
5. **覆盖索引**: 查询只走索引，避免回表

### 3.2 分表策略
1. **水平分表**:
   - `user_answers` 按用户ID哈希分表
   - `user_behavior_logs` 按月分表
   - `exam_records` 按时间范围分表

2. **垂直分表**:
   - 用户基础信息和扩展信息分离
   - 题目基础信息和统计信息分离
   - 答题记录和详细分析分离

### 3.3 缓存策略
1. **Redis缓存**:
   - 用户Session信息
   - 热点题目数据
   - 推荐结果缓存
   - 排行榜数据

2. **本地缓存**:
   - 分类和标签数据
   - 系统配置信息
   - 用户权限信息

### 3.4 数据归档
1. **冷热数据分离**:
   - 热数据: 最近3个月的数据
   - 温数据: 3-12个月的数据
   - 冷数据: 1年以上的数据

2. **归档策略**:
   - 答题记录按月归档
   - 行为日志按周归档
   - 系统日志按日归档

## 4. 数据库初始化脚本

### 4.1 基础数据初始化
```sql
-- 初始化题目分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order) VALUES
(0, '编程语言', 'programming-languages', '各种编程语言相关题目', 1, 1),
(0, '数据结构与算法', 'data-structures-algorithms', '数据结构与算法题目', 1, 2),
(0, '数据库', 'database', '数据库相关题目', 1, 3),
(0, '操作系统', 'operating-system', '操作系统相关题目', 1, 4),
(0, '计算机网络', 'computer-network', '计算机网络相关题目', 1, 5),
(0, '系统设计', 'system-design', '系统设计相关题目', 1, 6),
(0, '前端开发', 'frontend-development', '前端开发相关题目', 1, 7),
(0, '后端开发', 'backend-development', '后端开发相关题目', 1, 8);

-- 初始化题目标签
INSERT INTO question_tags (name, slug, description, color) VALUES
('Java', 'java', 'Java相关题目', '#FF6B6B'),
('Python', 'python', 'Python相关题目', '#4ECDC4'),
('JavaScript', 'javascript', 'JavaScript相关题目', '#45B7D1'),
('算法', 'algorithm', '算法相关题目', '#96CEB4'),
('数据库', 'database', '数据库相关题目', '#FFEAA7'),
('并发', 'concurrency', '并发编程相关题目', '#DDA0DD'),
('设计模式', 'design-pattern', '设计模式相关题目', '#98D8C8'),
('系统设计', 'system-design', '系统设计相关题目', '#F7DC6F');

-- 初始化默认用户
INSERT INTO users (username, email, password_hash, salt, nickname, status) VALUES
('admin', 'admin@example.com', 'hashed_password', 'salt_value', '系统管理员', 1);
```

### 4.2 视图定义
```sql
-- 用户学习统计视图
CREATE VIEW user_study_statistics AS
SELECT 
    u.id as user_id,
    u.username,
    u.total_study_time,
    u.total_questions,
    u.correct_rate,
    COUNT(DISTINCT DATE(ua.answered_at)) as active_days,
    COUNT(CASE WHEN ua.is_correct = 1 THEN 1 END) as correct_count,
    COUNT(CASE WHEN ua.is_correct = 0 THEN 1 END) as wrong_count,
    AVG(ua.time_spent) as avg_answer_time
FROM users u
LEFT JOIN user_answers ua ON u.id = ua.user_id
GROUP BY u.id, u.username, u.total_study_time, u.total_questions, u.correct_rate;

-- 题目热度视图
CREATE VIEW question_hotness AS
SELECT 
    q.id as question_id,
    q.title,
    q.difficulty,
    q.view_count,
    q.answer_count,
    q.correct_count,
    q.favorite_count,
    q.hot_score,
    (q.view_count * 0.3 + q.answer_count * 0.4 + q.favorite_count * 0.3) as calculated_hot_score,
    qc.name as category_name
FROM questions q
LEFT JOIN question_categories qc ON q.category_id = qc.id
WHERE q.status = 2; -- 只统计已发布的题目
```

## 5. 数据库维护计划

### 5.1 日常维护
1. **每日任务**:
   - 备份增量数据
   - 检查数据库连接
   - 监控慢查询日志

2. **每周任务**:
   - 优化表碎片
   - 更新统计信息
   - 清理过期数据

3. **每月任务**:
   - 全量数据备份
   - 索引重建
   - 性能分析报告

### 5.2 监控指标
1. **性能指标**:
   - 查询响应时间
   - 连接池使用率
   - 锁等待时间

2. **容量指标**:
   - 磁盘使用率
   - 表空间使用率
   - 数据增长趋势

3. **业务指标**:
   - 用户答题量
   - 题目访问量
   - 推荐效果数据

---

*本文档为数据库设计初稿，将在项目实施过程中持续完善*  
*最后更新: 2026-03-17*  
*版本: v1.0*