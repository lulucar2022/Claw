-- 程序员面试知识答题系统 - 初始化数据脚本
-- 版本: 1.0
-- 创建时间: 2026-03-17

-- 初始化题目分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order, is_active) VALUES
(0, '编程语言', 'programming-languages', '各种编程语言相关题目', 1, 1, TRUE),
(0, '数据结构与算法', 'data-structures-algorithms', '数据结构与算法题目', 1, 2, TRUE),
(0, '数据库', 'database', '数据库相关题目', 1, 3, TRUE),
(0, '操作系统', 'operating-system', '操作系统相关题目', 1, 4, TRUE),
(0, '计算机网络', 'computer-network', '计算机网络相关题目', 1, 5, TRUE),
(0, '系统设计', 'system-design', '系统设计相关题目', 1, 6, TRUE),
(0, '前端开发', 'frontend-development', '前端开发相关题目', 1, 7, TRUE),
(0, '后端开发', 'backend-development', '后端开发相关题目', 1, 8, TRUE);

-- 初始化Java子分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order, is_active) VALUES
(1, 'Java基础', 'java-basic', 'Java基础语法和核心概念', 2, 1, TRUE),
(1, 'Java集合框架', 'java-collections', 'Java集合框架相关题目', 2, 2, TRUE),
(1, 'Java并发编程', 'java-concurrency', 'Java多线程和并发编程', 2, 3, TRUE),
(1, 'JVM虚拟机', 'java-jvm', 'JVM原理和性能优化', 2, 4, TRUE),
(1, 'Spring框架', 'java-spring', 'Spring框架相关题目', 2, 5, TRUE),
(1, 'Spring Boot', 'java-spring-boot', 'Spring Boot相关题目', 2, 6, TRUE);

-- 初始化数据库子分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order, is_active) VALUES
(3, 'MySQL', 'database-mysql', 'MySQL数据库相关题目', 2, 1, TRUE),
(3, 'Redis', 'database-redis', 'Redis缓存数据库相关题目', 2, 2, TRUE),
(3, 'MongoDB', 'database-mongodb', 'MongoDB文档数据库相关题目', 2, 3, TRUE),
(3, 'SQL优化', 'database-sql-optimization', 'SQL查询优化相关题目', 2, 4, TRUE),
(3, '数据库设计', 'database-design', '数据库设计和规范化', 2, 5, TRUE);

-- 初始化前端开发子分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order, is_active) VALUES
(7, 'HTML/CSS', 'frontend-html-css', 'HTML和CSS基础题目', 2, 1, TRUE),
(7, 'JavaScript', 'frontend-javascript', 'JavaScript语言相关题目', 2, 2, TRUE),
(7, 'React', 'frontend-react', 'React框架相关题目', 2, 3, TRUE),
(7, 'Vue.js', 'frontend-vue', 'Vue.js框架相关题目', 2, 4, TRUE),
(7, 'TypeScript', 'frontend-typescript', 'TypeScript语言相关题目', 2, 5, TRUE);

-- 初始化后端开发子分类
INSERT INTO question_categories (parent_id, name, slug, description, level, sort_order, is_active) VALUES
(8, '微服务', 'backend-microservices', '微服务架构相关题目', 2, 1, TRUE),
(8, '分布式系统', 'backend-distributed', '分布式系统相关题目', 2, 2, TRUE),
(8, '消息队列', 'backend-message-queue', '消息队列相关题目', 2, 3, TRUE),
(8, 'API设计', 'backend-api-design', 'RESTful API设计相关题目', 2, 4, TRUE),
(8, '性能优化', 'backend-performance', '后端性能优化相关题目', 2, 5, TRUE);

-- 初始化管理员用户
INSERT INTO users (username, email, phone, password_hash, salt, nickname, gender, 
                   tech_stack, experience_years, target_position, current_level,
                   status, email_verified, phone_verified) VALUES
('admin', 'admin@claw.com', '13800138000', 
 '$2a$10$YourHashedPasswordHere', 'salt123', '系统管理员', 1,
 '["Java", "Spring", "MySQL", "Redis", "Docker"]', 5, '架构师', '高级',
 1, TRUE, TRUE);

-- 初始化测试用户
INSERT INTO users (username, email, phone, password_hash, salt, nickname, gender, 
                   tech_stack, experience_years, target_position, current_level,
                   status, email_verified, phone_verified) VALUES
('testuser', 'test@claw.com', '13900139000',
 '$2a$10$YourHashedPasswordHere', 'salt456', '测试用户', 1,
 '["Java", "Spring Boot", "MySQL", "Redis"]', 3, '高级开发工程师', '中级',
 1, TRUE, TRUE);

-- 初始化Java基础题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(9, 'Java中的final关键字有哪些用法？', 
 '请简述Java中final关键字的主要用法及其作用。',
 'single', 'easy',
 '["Java", "关键字", "final"]',
 '["变量修饰", "方法修饰", "类修饰"]',
 '[{"id": "A", "content": "修饰变量、方法、类"}, {"id": "B", "content": "只能修饰变量"}, {"id": "C", "content": "只能修饰方法"}, {"id": "D", "content": "只能修饰类"}]',
 'A',
 'final关键字可以修饰变量（表示常量）、方法（表示不可重写）、类（表示不可继承），所以正确答案是A。',
 2, 1);

-- 初始化Java集合框架题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(10, 'ArrayList和LinkedList的区别是什么？',
 '请从数据结构、性能特点、适用场景等方面比较ArrayList和LinkedList的区别。',
 'multiple', 'medium',
 '["Java", "集合", "ArrayList", "LinkedList"]',
 '["数据结构", "性能特点", "适用场景"]',
 '[{"id": "A", "content": "ArrayList基于数组实现，LinkedList基于链表实现"}, 
   {"id": "B", "content": "ArrayList随机访问快，LinkedList插入删除快"},
   {"id": "C", "content": "ArrayList内存连续，LinkedList内存不连续"},
   {"id": "D", "content": "两者性能完全一样"}]',
 'ABC',
 'ArrayList基于数组实现，支持快速随机访问，但插入删除需要移动元素；LinkedList基于双向链表实现，插入删除快，但随机访问需要遍历。所以正确答案是ABC。',
 2, 1);

-- 初始化MySQL题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(16, 'MySQL的索引有哪些类型？',
 '请简述MySQL中常见的索引类型及其特点。',
 'single', 'medium',
 '["MySQL", "数据库", "索引"]',
 '["索引类型", "B+树索引", "哈希索引", "全文索引"]',
 '[{"id": "A", "content": "B+树索引、哈希索引、全文索引"}, 
   {"id": "B", "content": "只有B+树索引"},
   {"id": "C", "content": "只有哈希索引"},
   {"id": "D", "content": "只有全文索引"}]',
 'A',
 'MySQL支持多种索引类型：B+树索引（最常用）、哈希索引（Memory引擎）、全文索引（MyISAM和InnoDB）、空间索引等。所以正确答案是A。',
 2, 1);

-- 初始化数据结构题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(2, '二叉树的遍历方式有哪些？',
 '请描述二叉树的常见遍历方式及其特点。',
 'multiple', 'easy',
 '["数据结构", "二叉树", "遍历"]',
 '["前序遍历", "中序遍历", "后序遍历", "层序遍历"]',
 '[{"id": "A", "content": "前序遍历：根左右"}, 
   {"id": "B", "content": "中序遍历：左根右"},
   {"id": "C", "content": "后序遍历：左右根"},
   {"id": "D", "content": "层序遍历：逐层遍历"}]',
 'ABCD',
 '二叉树常见的遍历方式有：前序遍历（根左右）、中序遍历（左根右）、后序遍历（左右根）、层序遍历（逐层遍历）。所以正确答案是ABCD。',
 2, 1);

-- 初始化操作系统题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(4, '进程和线程的区别是什么？',
 '请从资源分配、调度、通信方式等方面比较进程和线程的区别。',
 'multiple', 'medium',
 '["操作系统", "进程", "线程"]',
 '["资源分配", "调度开销", "通信方式", "并发性"]',
 '[{"id": "A", "content": "进程是资源分配的基本单位，线程是CPU调度的基本单位"}, 
   {"id": "B", "content": "进程拥有独立的地址空间，线程共享进程的地址空间"},
   {"id": "C", "content": "进程切换开销大，线程切换开销小"},
   {"id": "D", "content": "进程通信复杂，线程通信简单"}]',
 'ABCD',
 '进程是资源分配的基本单位，拥有独立的地址空间，切换开销大；线程是CPU调度的基本单位，共享进程资源，切换开销小，通信简单。所以正确答案是ABCD。',
 2, 1);

-- 初始化网络题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(5, 'TCP和UDP的区别是什么？',
 '请从连接性、可靠性、传输效率等方面比较TCP和UDP的区别。',
 'multiple', 'medium',
 '["计算机网络", "TCP", "UDP", "传输层"]',
 '["连接性", "可靠性", "传输效率", "应用场景"]',
 '[{"id": "A", "content": "TCP是面向连接的，UDP是无连接的"}, 
   {"id": "B", "content": "TCP提供可靠传输，UDP提供不可靠传输"},
   {"id": "C", "content": "TCP传输效率低，UDP传输效率高"},
   {"id": "D", "content": "TCP适合文件传输，UDP适合视频流"}]',
 'ABCD',
 'TCP是面向连接的可靠传输协议，提供流量控制和拥塞控制，传输效率相对较低；UDP是无连接的不可靠传输协议，传输效率高，适合实时应用。所以正确答案是ABCD。',
 2, 1);

-- 初始化系统设计题目
INSERT INTO questions (category_id, title, content, question_type, difficulty, 
                       tags, key_points, options, correct_answer, answer_explanation,
                       status, created_by) VALUES
(6, '什么是微服务架构？',
 '请简述微服务架构的特点和优势。',
 'single', 'hard',
 '["系统设计", "微服务", "架构"]',
 '["服务拆分", "独立部署", "技术异构", "容错性"]',
 '[{"id": "A", "content": "将单体应用拆分为多个小型服务，每个服务独立部署和扩展"}, 
   {"id": "B", "content": "所有服务必须使用相同技术栈"},
   {"id": "C", "content": "服务之间强耦合"},
   {"id": "D", "content": "只能用于小型项目"}]',
 'A',
 '微服务架构是将单体应用拆分为多个小型、独立的服务，每个服务可以独立开发、部署和扩展，支持技术异构，提高系统容错性和可维护性。所以正确答案是A。',
 2, 1);

-- 更新分类统计信息
UPDATE question_categories SET question_count = (
    SELECT COUNT(*) FROM questions WHERE category_id = question_categories.id
) WHERE id IN (SELECT DISTINCT category_id FROM questions);

-- 创建默认收藏夹
INSERT INTO favorite_folders (user_id, name, description, is_default, is_public, sort_order) VALUES
(1, '我的收藏', '默认收藏夹', TRUE, FALSE, 1),
(2, 'Java学习', 'Java相关题目收藏', TRUE, FALSE, 1),
(2, '系统设计', '系统设计相关题目收藏', FALSE, TRUE, 2);

-- 添加测试收藏记录
INSERT INTO user_favorites (user_id, question_id, folder_id, importance_level, created_at) VALUES
(2, 1, 2, 3, NOW()),
(2, 2, 2, 4, NOW()),
(2, 7, 3, 5, NOW());

-- 更新收藏夹统计
UPDATE favorite_folders SET item_count = (
    SELECT COUNT(*) FROM user_favorites WHERE folder_id = favorite_folders.id
) WHERE id IN (1, 2, 3);

-- 添加测试答题记录
INSERT INTO user_answers (user_id, question_id, user_answer, is_correct, score, time_spent, confidence_level, answered_at) VALUES
(2, 1, 'A', TRUE, 10.0, 45, 4, NOW()),
(2, 2, 'ABC', TRUE, 10.0, 60, 3, NOW()),
(2, 3, 'B', FALSE, 0.0, 30, 2, NOW()),
(2, 4, 'ABCD', TRUE, 10.0, 40, 4, NOW());

-- 更新题目统计信息
UPDATE questions SET 
    answer_count = (SELECT COUNT(*) FROM user_answers WHERE question_id = questions.id),
    correct_count = (SELECT COUNT(*) FROM user_answers WHERE question_id = questions.id AND is_correct = TRUE),
    wrong_count = (SELECT COUNT(*) FROM user_answers WHERE question_id = questions.id AND is_correct = FALSE)
WHERE id IN (1, 2, 3, 4);

-- 创建错题集
INSERT INTO wrong_answer_collections (user_id, name, description, category_id, is_public, review_interval_days, daily_review_limit) VALUES
(2, 'MySQL错题集', 'MySQL相关错题整理', 3, FALSE, 7, 10),
(2, 'Java基础错题', 'Java基础概念错题', 1, FALSE, 5, 8);

-- 添加错题项（用户答错的题目）
INSERT INTO wrong_answer_items (collection_id, question_id, user_answer_id, wrong_reason, correct_understanding, notes, mastery_level, error_count, last_error_at, next_review_at) VALUES
(1, 3, 3, '对索引类型理解不全面', 'MySQL支持多种索引类型，包括B+树索引、哈希索引、全文索引等', '需要重点复习MySQL索引章节', 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- 更新错题集统计
UPDATE wrong_answer_collections SET 
    question_count = (SELECT COUNT(*) FROM wrong_answer_items WHERE collection_id = wrong_answer_collections.id),
    mastery_rate = (SELECT AVG(mastery_level) FROM wrong_answer_items WHERE collection_id = wrong_answer_collections.id)
WHERE id IN (1, 2);

-- 创建用户学习统计视图
CREATE OR REPLACE VIEW user_study_statistics AS
SELECT 
    u.id as user_id,
    u.username,
    u.total_study_time,
    u.total_questions,
    u.correct_rate,
    COUNT(DISTINCT DATE(ua.answered_at)) as active_days,
    COUNT(CASE WHEN ua.is_correct = 1 THEN 1 END) as correct_count,
    COUNT(CASE WHEN ua.is_correct = 0 THEN 1 END) as wrong_count,
    AVG(ua.time_spent) as avg_answer_time,
    COUNT(DISTINCT ua.question_id) as distinct_questions
FROM users u
LEFT JOIN user_answers ua ON u.id = ua.user_id
GROUP BY u.id, u.username, u.total_study_time, u.total_questions, u.correct_rate;