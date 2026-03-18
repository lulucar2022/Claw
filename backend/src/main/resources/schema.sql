-- 程序员面试知识答题系统 - 数据库初始化脚本
-- 版本: v1.0
-- 创建时间: 2026-03-17

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `claw_db_dev` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `claw_db_dev`;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
    `salt` VARCHAR(50) NOT NULL COMMENT '密码盐值',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar_url` VARCHAR(500) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    `birth_date` DATE COMMENT '出生日期',
    
    `tech_stack` VARCHAR(500) COMMENT '技术栈，JSON数组',
    `experience_years` TINYINT DEFAULT 0 COMMENT '工作年限',
    `target_position` VARCHAR(50) COMMENT '目标岗位',
    `current_level` VARCHAR(50) COMMENT '当前水平',
    
    `daily_study_time` INT DEFAULT 30 COMMENT '每日学习时间(分钟)',
    `study_goal` TEXT COMMENT '学习目标',
    `preferred_difficulty` VARCHAR(20) DEFAULT 'medium' COMMENT '偏好难度',
    
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常, 2-锁定',
    `email_verified` BOOLEAN DEFAULT FALSE COMMENT '邮箱验证',
    `phone_verified` BOOLEAN DEFAULT FALSE COMMENT '手机验证',
    
    `total_study_time` INT DEFAULT 0 COMMENT '总学习时长(分钟)',
    `total_questions` INT DEFAULT 0 COMMENT '总答题数',
    `correct_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '正确率',
    `continuous_days` INT DEFAULT 0 COMMENT '连续学习天数',
    
    `last_login_at` DATETIME COMMENT '最后登录时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME COMMENT '删除时间',
    
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户扩展信息表
CREATE TABLE IF NOT EXISTS `user_profiles` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    
    `education_level` VARCHAR(50) COMMENT '学历',
    `school_name` VARCHAR(100) COMMENT '学校名称',
    `major` VARCHAR(100) COMMENT '专业',
    `graduation_year` YEAR COMMENT '毕业年份',
    
    `company_name` VARCHAR(100) COMMENT '公司名称',
    `job_title` VARCHAR(100) COMMENT '职位',
    `work_years` TINYINT COMMENT '工作年限',
    `industry` VARCHAR(100) COMMENT '行业',
    
    `skill_assessment` JSON COMMENT '技能评估，JSON格式',
    `weak_points` JSON COMMENT '薄弱点，JSON格式',
    `strong_points` JSON COMMENT '优势点，JSON格式',
    
    `learning_style` VARCHAR(50) COMMENT '学习风格',
    `preferred_topics` JSON COMMENT '偏好主题，JSON数组',
    `avoid_topics` JSON COMMENT '避免主题，JSON数组',
    
    `exam_target` VARCHAR(100) COMMENT '考试目标',
    `exam_date` DATE COMMENT '考试日期',
    `preparation_plan` TEXT COMMENT '备考计划',
    
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户扩展信息表';

-- 题目分类表
CREATE TABLE IF NOT EXISTS `question_categories` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `slug` VARCHAR(100) NOT NULL UNIQUE COMMENT '分类标识',
    `description` TEXT COMMENT '分类描述',
    `icon_url` VARCHAR(500) COMMENT '图标URL',
    
    `level` TINYINT DEFAULT 1 COMMENT '层级',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `is_leaf` BOOLEAN DEFAULT FALSE COMMENT '是否叶子节点',
    
    `question_count` INT DEFAULT 0 COMMENT '题目数量',
    `study_count` INT DEFAULT 0 COMMENT '学习次数',
    
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME COMMENT '删除时间',
    
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_slug` (`slug`),
    INDEX `idx_sort_order` (`sort_order`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目分类表';

-- 题目表
CREATE TABLE IF NOT EXISTS `questions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `title` VARCHAR(500) NOT NULL COMMENT '题目标题',
    `content` TEXT NOT NULL COMMENT '题目内容',
    `question_type` VARCHAR(50) NOT NULL COMMENT '题目类型: single/multiple/code/fill/short',
    
    `difficulty` VARCHAR(20) DEFAULT 'medium' COMMENT '难度: easy/medium/hard/expert',
    `tags` JSON COMMENT '标签，JSON数组',
    `key_points` JSON COMMENT '考点，JSON数组',
    
    `options` JSON COMMENT '选项，JSON数组',
    `correct_answer` TEXT COMMENT '正确答案',
    `answer_explanation` TEXT COMMENT '答案解析',
    
    `code_template` TEXT COMMENT '代码模板',
    `test_cases` JSON COMMENT '测试用例，JSON数组',
    `language` VARCHAR(50) COMMENT '编程语言',
    
    `source_type` VARCHAR(50) COMMENT '来源类型: system/user/import',
    `source_info` VARCHAR(500) COMMENT '来源信息',
    `original_url` VARCHAR(500) COMMENT '原始URL',
    
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `answer_count` INT DEFAULT 0 COMMENT '答题次数',
    `correct_count` INT DEFAULT 0 COMMENT '正确次数',
    `favorite_count` INT DEFAULT 0 COMMENT '收藏次数',
    `wrong_count` INT DEFAULT 0 COMMENT '错误次数',
    `avg_time_spent` INT COMMENT '平均耗时(秒)',
    
    `hot_score` DECIMAL(10,4) DEFAULT 0 COMMENT '热度分数',
    `trend_score` DECIMAL(10,4) DEFAULT 0 COMMENT '趋势分数',
    
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-审核中, 2-已发布, 3-下架',
    `is_recommended` BOOLEAN DEFAULT FALSE COMMENT '是否推荐',
    `is_hot` BOOLEAN DEFAULT FALSE COMMENT '是否热门',
    
    `reviewer_id` BIGINT COMMENT '审核人ID',
    `reviewed_at` DATETIME COMMENT '审核时间',
    `review_comment` TEXT COMMENT '审核意见',
    
    `created_by` BIGINT COMMENT '创建人ID',
    `updated_by` BIGINT COMMENT '更新人ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `published_at` DATETIME COMMENT '发布时间',
    
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_difficulty` (`difficulty`),
    INDEX `idx_status` (`status`),
    INDEX `idx_hot_score` (`hot_score`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_published_at` (`published_at`),
    FULLTEXT `idx_title_content` (`title`, `content`),
    FOREIGN KEY (`category_id`) REFERENCES `question_categories`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目表';

-- 用户答题记录表
CREATE TABLE IF NOT EXISTS `user_answers` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `session_id` VARCHAR(100) COMMENT '答题会话ID',
    
    `user_answer` TEXT COMMENT '用户答案',
    `is_correct` BOOLEAN COMMENT '是否正确',
    `score` DECIMAL(5,2) COMMENT '得分',
    `time_spent` INT COMMENT '耗时(秒)',
    
    `code_submission` TEXT COMMENT '提交的代码',
    `test_results` JSON COMMENT '测试结果，JSON数组',
    `execution_time` INT COMMENT '执行时间(ms)',
    `memory_usage` INT COMMENT '内存使用(KB)',
    
    `confidence_level` TINYINT COMMENT '信心等级: 1-5',
    `difficulty_perception` TINYINT COMMENT '难度感知: 1-5',
    `notes` TEXT COMMENT '用户笔记',
    
    `is_reviewed` BOOLEAN DEFAULT FALSE COMMENT '是否已复习',
    `review_count` INT DEFAULT 0 COMMENT '复习次数',
    
    `answered_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '答题时间',
    `reviewed_at` DATETIME COMMENT '复习时间',
    `next_review_at` DATETIME COMMENT '下次复习时间',
    
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_question_id` (`question_id`),
    INDEX `idx_is_correct` (`is_correct`),
    INDEX `idx_answered_at` (`answered_at`),
    INDEX `idx_user_question` (`user_id`, `question_id`),
    INDEX `idx_next_review_at` (`next_review_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`question_id`) REFERENCES `questions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户答题记录表';

-- 用户会话表
CREATE TABLE IF NOT EXISTS `user_sessions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `session_token` VARCHAR(255) NOT NULL COMMENT '会话令牌',
    `refresh_token` VARCHAR(255) COMMENT '刷新令牌',
    `device_info` VARCHAR(500) COMMENT '设备信息',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` VARCHAR(500) COMMENT '用户代理',
    
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '是否活跃',
    `expires_at` DATETIME NOT NULL COMMENT '过期时间',
    `last_activity_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后活动时间',
    
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_session_token` (`session_token`),
    INDEX `idx_expires_at` (`expires_at`),
    INDEX `idx_is_active` (`is_active`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- 初始化数据
-- 创建管理员用户
INSERT INTO `users` (`username`, `email`, `password_hash`, `salt`, `nickname`, `status`) VALUES
('admin', 'admin@example.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeKRZ0CwS5H8u7f6M3Bpz4JwSQ1YQzQ/W', 
 'salt123456', '系统管理员', 1);

-- 创建测试用户
INSERT INTO `users` (`username`, `email`, `password_hash`, `salt`, `nickname`, `tech_stack`, `experience_years`, `status`) VALUES
('testuser', 'test@example.com', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeKRZ0CwS5H8u7f6M3Bpz4JwSQ1YQzQ/W', 
 'salt123456', '测试用户', 'Java,Spring,MySQL', 3, 1);

-- 创建题目分类
INSERT INTO `question_categories` (`parent_id`, `name`, `slug`, `description`, `level`, `sort_order`) VALUES
(0, '编程语言', 'programming-languages', '各种编程语言相关题目', 1, 1),
(0, '数据结构与算法', 'data-structures-algorithms', '数据结构与算法题目', 1, 2),
(0, '数据库', 'database', '数据库相关题目', 1, 3),
(0, '操作系统', 'operating-system', '操作系统相关题目', 1, 4),
(0, '计算机网络', 'computer-network', '计算机网络相关题目', 1, 5),
(0, '系统设计', 'system-design', '系统设计相关题目', 1, 6),
(0, '前端开发', 'frontend-development', '前端开发相关题目', 1, 7),
(0, '后端开发', 'backend-development', '后端开发相关题目', 1, 8);

-- 创建Java子分类
INSERT INTO `question_categories` (`parent_id`, `name`, `slug`, `description`, `level`, `sort_order`) VALUES
(1, 'Java', 'java', 'Java相关题目', 2, 1),
(1, 'Python', 'python', 'Python相关题目', 2, 2),
(1, 'JavaScript', 'javascript', 'JavaScript相关题目', 2, 3),
(1, 'Go', 'go', 'Go语言相关题目', 2, 4);

-- 创建示例题目
INSERT INTO `questions` (`category_id`, `title`, `content`, `question_type`, `difficulty`, `tags`, `key_points`, `options`, `correct_answer`, `answer_explanation`, `status`, `created_by`, `published_at`) VALUES
(9, 'Java中HashMap的工作原理', '请简述HashMap的工作原理，包括其数据结构、哈希冲突处理、扩容机制等。', 'short', 'medium', '["Java", "集合", "HashMap"]', '["哈希冲突", "扩容机制", "线程安全"]', NULL, 'HashMap是基于哈希表实现的Map接口，使用数组+链表+红黑树（JDK8+）的数据结构。通过key的hashCode计算数组下标，使用equals方法判断key是否相等。当链表长度超过8时转换为红黑树，当元素数量超过容量*负载因子时进行扩容。', 'HashMap是Java中最常用的集合类之一，理解其工作原理对于Java开发至关重要。', 2, 1, NOW()),

(9, 'Java中String、StringBuilder和StringBuffer的区别', '请说明String、StringBuilder和StringBuffer三者的区别，包括线程安全性、性能、使用场景等。', 'single', 'easy', '["Java", "字符串"]', '["线程安全", "可变性", "性能"]', '["A: String是可变的，StringBuilder和StringBuffer是不可变的", "B: StringBuilder是线程安全的，StringBuffer不是线程安全的", "C: String是不可变的，StringBuilder和StringBuffer是可变的，其中StringBuffer是线程安全的", "D: 三者都是线程安全的"]', 'C', 'String是不可变类，每次修改都会创建新对象。StringBuilder和StringBuffer都是可变字符序列，StringBuffer是线程安全的，但性能稍差。StringBuilder是非线程安全的，性能最好。', 2, 1, NOW()),

(10, 'Python中列表和元组的区别', '请说明Python中列表（list）和元组（tuple）的主要区别。', 'multiple', 'easy', '["Python", "数据结构"]', '["可变性", "性能", "使用场景"]', '["A: 列表是可变的，元组是不可变的", "B: 列表使用方括号[]，元组使用圆括号()", "C: 元组比列表占用更少的内存", "D: 列表可以作为字典的键，元组不能"]', 'A,B,C', '列表是可变的，可以增删改元素；元组是不可变的，创建后不能修改。列表使用[]，元组使用()。由于元组不可变，Python可以对元组进行更多的优化，因此元组通常比列表占用更少的内存，访问速度也更快。列表不能作为字典的键，而元组可以（如果元组中的所有元素都是不可变的）。', 2, 1, NOW());

-- 创建示例答题记录
INSERT INTO `user_answers` (`user_id`, `question_id`, `user_answer`, `is_correct`, `score`, `time_spent`, `answered_at`) VALUES
(2, 1, 'HashMap是基于哈希表实现的Map接口...', true, 10, 45, NOW()),
(2, 2, 'C', true, 10, 30, NOW()),
(2, 3, 'A,B,C', true, 10, 25, NOW());

-- 更新统计信息
UPDATE `users` SET 
    `total_questions` = 3,
    `correct_rate` = 100.0,
    `total_study_time` = 100
WHERE `id` = 2;

UPDATE `questions` SET 
    `answer_count` = 1,
    `correct_count` = 1,
    `view_count` = 10,
    `hot_score` = 15.0
WHERE `id` IN (1, 2, 3);

UPDATE `question_categories` SET 
    `question_count` = (SELECT COUNT(*) FROM `questions` WHERE `category_id` = 9)
WHERE `id` = 9;

UPDATE `question_categories` SET 
    `question_count` = (SELECT COUNT(*) FROM `questions` WHERE `category_id` = 10)
WHERE `id` = 10;

COMMIT;