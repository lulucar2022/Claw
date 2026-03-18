-- 程序员面试知识答题系统 - 数据库初始化脚本
-- 版本: 1.0
-- 创建时间: 2026-03-17

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
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
    
    INDEX idx_username(username),
    INDEX idx_email(email),
    INDEX idx_status(status),
    INDEX idx_created_at(created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建用户扩展信息表
CREATE TABLE IF NOT EXISTS user_profiles (
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

-- 创建用户会话表
CREATE TABLE IF NOT EXISTS user_sessions (
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

-- 创建题目分类表
CREATE TABLE IF NOT EXISTS question_categories (
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

-- 创建题目表
CREATE TABLE IF NOT EXISTS questions (
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

-- 创建用户答题记录表
CREATE TABLE IF NOT EXISTS user_answers (
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
    INDEX idx_next_review_at(next_review_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户答题记录表';

-- 创建错题集表
CREATE TABLE IF NOT EXISTS wrong_answer_collections (
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

-- 创建错题项表
CREATE TABLE IF NOT EXISTS wrong_answer_items (
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

-- 创建用户收藏表
CREATE TABLE IF NOT EXISTS user_favorites (
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

-- 创建收藏夹表
CREATE TABLE IF NOT EXISTS favorite_folders (
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