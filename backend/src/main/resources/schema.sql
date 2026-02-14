-- 创建数据库
CREATE DATABASE IF NOT EXISTS opencode DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE opencode;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    department VARCHAR(50),
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',
    status INT DEFAULT 1 COMMENT '1:正常 0:禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '0:未删除 1:已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    user_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 项目表
CREATE TABLE IF NOT EXISTS sys_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(50),
    color VARCHAR(20),
    member_count INT DEFAULT 0,
    start_date DATETIME,
    progress INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 任务表
CREATE TABLE IF NOT EXISTS sys_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    priority VARCHAR(20) DEFAULT 'medium',
    status VARCHAR(20) DEFAULT 'todo',
    assignee VARCHAR(50),
    project_id BIGINT,
    due_date DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 文档表
CREATE TABLE IF NOT EXISTS sys_document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(50),
    size VARCHAR(20),
    type VARCHAR(20),
    url VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 日志表
CREATE TABLE IF NOT EXISTS sys_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    operation VARCHAR(100),
    ip VARCHAR(50),
    method VARCHAR(50),
    params TEXT,
    status INT DEFAULT 1,
    error_msg TEXT,
    duration BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 知识库分类表
CREATE TABLE IF NOT EXISTS sys_wiki_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    article_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 知识库文章表
CREATE TABLE IF NOT EXISTS sys_wiki_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(500),
    content TEXT,
    author VARCHAR(50),
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    INDEX idx_category (category_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 设置表
CREATE TABLE IF NOT EXISTS sys_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    dark_mode TINYINT DEFAULT 0,
    email_notify TINYINT DEFAULT 1,
    desktop_notify TINYINT DEFAULT 0,
    language VARCHAR(20) DEFAULT 'zh-CN',
    timezone VARCHAR(50) DEFAULT 'Asia/Shanghai',
    bio VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 日程表
CREATE TABLE IF NOT EXISTS sys_calendar (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    user_id BIGINT,
    type VARCHAR(20) DEFAULT 'event',
    color VARCHAR(20) DEFAULT '#0ea5e9',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报表表
CREATE TABLE IF NOT EXISTS sys_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    type VARCHAR(50),
    url VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 插入测试数据（仅在表为空时执行）
-- ============================================

-- 插入12个用户（仅在sys_user表为空时）
INSERT INTO sys_user (username, password, name, email, phone, department, role, status)
SELECT * FROM (VALUES ROW('admin', 'password', '系统管理员', 'admin@opencode.com', '13800138000', '技术部', 'ADMIN', 1),
                      ROW('zhangsan', 'password', '张三', 'zhangsan@opencode.com', '13800138001', '产品部', 'USER', 1),
                      ROW('lisi', 'password', '李四', 'lisi@opencode.com', '13800138002', '开发部', 'EDITOR', 1),
                      ROW('wangwu', 'password', '王五', 'wangwu@opencode.com', '13800138003', '测试部', 'USER', 1),
                      ROW('zhaoliu', 'password', '赵六', 'zhaoliu@opencode.com', '13800138004', '运维部', 'USER', 1),
                      ROW('qianqi', 'password', '钱七', 'qianqi@opencode.com', '13800138005', '设计部', 'USER', 1),
                      ROW('sunba', 'password', '孙八', 'sunba@opencode.com', '13800138006', '市场部', 'USER', 0),
                      ROW('zhoujiu', 'password', '周九', 'zhoujiu@opencode.com', '13800138007', '销售部', 'USER', 1),
                      ROW('wushi', 'password', '吴十', 'wushi@opencode.com', '13800138008', '客服部', 'USER', 1),
                      ROW('zhengshi', 'password', '郑十一', 'zhengshi@opencode.com', '13800138009', '财务部', 'USER', 1),
                      ROW('dongfang', 'password', '董方', 'dongfang@opencode.com', '13800138010', '人事部', 'USER', 1),
                      ROW('ximen', 'password', '西门', 'ximen@opencode.com', '13800138011', '行政部', 'USER', 1)) AS tmp(username, password, name, email, phone, department, role, status)
WHERE NOT EXISTS (SELECT 1 FROM sys_user LIMIT 1);

-- 插入8个角色（仅在sys_role表为空时）
INSERT INTO sys_role (name, code, description, user_count)
SELECT * FROM (VALUES ROW('超级管理员', 'SUPER_ADMIN', '拥有系统所有权限，可管理所有模块', 1),
                      ROW('系统管理员', 'ADMIN', '管理系统配置和用户权限', 3),
                      ROW('产品经理', 'PRODUCT_MANAGER', '负责产品规划和需求管理', 5),
                      ROW('开发工程师', 'DEVELOPER', '负责系统开发和维护', 25),
                      ROW('测试工程师', 'TESTER', '负责系统测试和质量保证', 12),
                      ROW('运维工程师', 'OPERATOR', '负责系统部署和运维', 8),
                      ROW('内容编辑', 'EDITOR', '负责内容编辑和发布', 15),
                      ROW('普通用户', 'USER', '普通访问权限', 150)) AS tmp(name, code, description, user_count)
WHERE NOT EXISTS (SELECT 1 FROM sys_role LIMIT 1);

-- 插入12个项目（仅在sys_project表为空时）
INSERT INTO sys_project (name, description, icon, color, member_count, progress, status, start_date)
SELECT * FROM (VALUES ROW('企业管理系统V2.0', '全面升级企业内部管理系统，包含OA、CRM、ERP模块', '💼', '#0ea5e9', 12, 75, 'active', '2024-01-15'),
                      ROW('电商平台重构', '基于微服务架构重构电商平台，提升性能和扩展性', '🛒', '#34d399', 18, 60, 'active', '2024-02-01'),
                      ROW('移动端APP开发', '开发iOS和Android双平台APP，支持离线功能', '📱', '#f472b6', 8, 30, 'active', '2024-03-01'),
                      ROW('安全审计系统', '建立完整的安全审计体系，包含日志分析和异常检测', '🔒', '#fbbf24', 5, 100, 'done', '2023-11-01'),
                      ROW('数据可视化平台', '构建企业级数据可视化平台，支持多数据源接入', '📊', '#8b5cf6', 10, 45, 'active', '2024-02-15'),
                      ROW('智能客服系统', '基于AI的智能客服系统，支持自然语言处理', '🤖', '#06b6d4', 6, 20, 'pending', '2024-04-01'),
                      ROW('供应链管理系统', '优化供应链管理流程，实现全程可追溯', '📦', '#f59e0b', 14, 80, 'active', '2023-12-01'),
                      ROW('人力资源系统', '数字化人力资源管理，包含招聘、绩效、培训模块', '👥', '#ec4899', 7, 55, 'active', '2024-01-20'),
                      ROW('财务管理系统', '企业财务一体化管理，支持多账套和报表分析', '💰', '#10b981', 9, 90, 'active', '2023-10-15'),
                      ROW('知识管理系统', '企业知识沉淀和共享平台，支持全文检索', '📚', '#6366f1', 4, 100, 'done', '2023-09-01'),
                      ROW('物联网平台', '连接和管理IoT设备，支持实时监控和预警', '🌐', '#14b8a6', 11, 35, 'active', '2024-02-20'),
                      ROW('区块链存证系统', '基于区块链的数据存证和溯源系统', '⛓️', '#64748b', 3, 15, 'pending', '2024-05-01')) AS tmp(name, description, icon, color, member_count, progress, status, start_date)
WHERE NOT EXISTS (SELECT 1 FROM sys_project LIMIT 1);

-- 插入15个任务（仅在sys_task表为空时）
INSERT INTO sys_task (title, description, priority, status, assignee, project_id, due_date)
SELECT * FROM (VALUES ROW('完成用户模块API开发', '开发用户注册、登录、权限管理相关接口', 'high', 'done', '张三', 1, '2024-02-15'),
                      ROW('设计系统首页UI', '设计响应式首页，包含数据看板和快捷入口', 'high', 'done', '李四', 1, '2024-02-10'),
                      ROW('编写接口文档', '使用Swagger编写完整的API文档', 'medium', 'inprogress', '王五', 1, '2024-03-01'),
                      ROW('数据库性能优化', '优化慢查询，添加索引，提升查询效率', 'high', 'todo', '赵六', 2, '2024-03-15'),
                      ROW('单元测试覆盖率提升', '将核心模块测试覆盖率提升到80%以上', 'medium', 'inprogress', '钱七', 2, '2024-03-20'),
                      ROW('iOS首页开发', '开发iOS端首页，实现下拉刷新和上拉加载', 'high', 'inprogress', '孙八', 3, '2024-04-01'),
                      ROW('Android适配测试', '在主流Android设备上进行适配测试', 'medium', 'todo', '周九', 3, '2024-04-15'),
                      ROW('安全漏洞修复', '修复渗透测试发现的安全漏洞', 'high', 'done', '吴十', 4, '2024-01-30'),
                      ROW('日志分析模块', '开发日志收集和分析模块', 'low', 'done', '郑十一', 4, '2024-01-20'),
                      ROW('数据大屏开发', '开发实时数据可视化大屏', 'high', 'inprogress', '董方', 5, '2024-03-30'),
                      ROW('客服机器人训练', '训练NLP模型，提升问答准确率', 'medium', 'todo', '西门', 6, '2024-05-15'),
                      ROW('供应商管理模块', '开发供应商准入和评价模块', 'medium', 'inprogress', '张三', 7, '2024-03-10'),
                      ROW('员工绩效考核', '开发绩效考核打分和统计功能', 'low', 'todo', '李四', 8, '2024-04-01'),
                      ROW('财务报表导出', '支持PDF和Excel格式的财务报表导出', 'medium', 'done', '王五', 9, '2024-02-28'),
                      ROW('知识库分类整理', '对现有知识文档进行分类整理', 'low', 'done', '赵六', 10, '2024-01-15')) AS tmp(title, description, priority, status, assignee, project_id, due_date)
WHERE NOT EXISTS (SELECT 1 FROM sys_task LIMIT 1);

-- 插入12个文档（仅在sys_document表为空时）
INSERT INTO sys_document (name, description, icon, size, type, url)
SELECT * FROM (VALUES ROW('企业管理系统需求规格说明书V2.0.docx', '详细描述V2.0版本的功能需求和技术要求', '📕', '5.2 MB', 'docx', '/docs/requirements_v2.docx'),
                      ROW('系统架构设计文档.pdf', '微服务架构设计，包含服务拆分和接口定义', '📗', '8.5 MB', 'pdf', '/docs/architecture.pdf'),
                      ROW('API接口文档V1.0.md', 'RESTful API接口详细说明，包含请求响应示例', '📘', '1.8 MB', 'md', '/docs/api_v1.md'),
                      ROW('测试用例全集.xlsx', '功能测试、性能测试、安全测试用例汇总', '📙', '2.1 MB', 'xlsx', '/docs/test_cases.xlsx'),
                      ROW('用户操作手册.pdf', '面向终端用户的详细操作指南', '📓', '12.4 MB', 'pdf', '/docs/user_manual.pdf'),
                      ROW('数据库设计文档.pdm', 'PowerDesigner数据库设计模型文件', '📔', '3.6 MB', 'pdm', '/docs/database_design.pdm'),
                      ROW('项目进度计划.mpp', 'Microsoft Project项目进度管理文件', '📊', '1.2 MB', 'mpp', '/docs/schedule.mpp'),
                      ROW('代码规范文档.md', '团队代码编写规范和最佳实践', '📄', '856 KB', 'md', '/docs/coding_standards.md'),
                      ROW('部署运维手册.pdf', '系统部署、监控、故障处理指南', '🔧', '4.3 MB', 'pdf', '/docs/operations.pdf'),
                      ROW('安全审计报告.pdf', '第三方安全公司出具的安全审计报告', '🛡️', '2.8 MB', 'pdf', '/docs/security_audit.pdf'),
                      ROW('培训教材.pptx', '新员工入职培训使用的PPT教材', '🎓', '15.6 MB', 'pptx', '/docs/training.pptx'),
                      ROW('会议纪要-2024-03-01.docx', '3月1日项目周会会议纪要和待办事项', '📝', '456 KB', 'docx', '/docs/meeting_20240301.docx')) AS tmp(name, description, icon, size, type, url)
WHERE NOT EXISTS (SELECT 1 FROM sys_document LIMIT 1);

-- 插入15条操作日志（仅在sys_log表为空时）
INSERT INTO sys_log (username, operation, ip, method, params, status, duration)
SELECT * FROM (VALUES ROW('admin', '登录系统', '192.168.1.100', 'POST', '{"username":"admin"}', 1, 120),
                      ROW('zhangsan', '创建新项目-企业管理系统V2.0', '192.168.1.101', 'POST', '{"name":"企业管理系统V2.0"}', 1, 350),
                      ROW('lisi', '编辑用户信息-修改手机号', '192.168.1.102', 'PUT', '{"userId":3,"phone":"13800138002"}', 1, 180),
                      ROW('wangwu', '删除任务-完成首页设计', '192.168.1.103', 'DELETE', '{"taskId":2}', 1, 95),
                      ROW('zhaoliu', '导出用户列表Excel', '192.168.1.104', 'GET', '{"format":"excel"}', 1, 2500),
                      ROW('qianqi', '上传文档-设计规范V2.0.pdf', '192.168.1.105', 'POST', '{"filename":"设计规范V2.0.pdf","size":"5.2MB"}', 1, 3200),
                      ROW('sunba', '修改个人设置-开启深色模式', '192.168.1.106', 'PUT', '{"darkMode":true}', 1, 85),
                      ROW('zhoujiu', '创建日程-客户拜访', '192.168.1.107', 'POST', '{"title":"客户拜访","time":"2024-03-15 14:00"}', 1, 150),
                      ROW('wushi', '生成报表-月度销售统计', '192.168.1.108', 'GET', '{"type":"sales","month":"2024-02"}', 1, 5600),
                      ROW('zhengshi', '审批通过-请假申请', '192.168.1.109', 'PUT', '{"leaveId":15,"status":"approved"}', 1, 200),
                      ROW('dongfang', '发布知识库文章-Vue3最佳实践', '192.168.1.110', 'POST', '{"title":"Vue3最佳实践","category":"技术文档"}', 1, 420),
                      ROW('ximen', '修改项目进度-电商平台重构', '192.168.1.111', 'PUT', '{"projectId":2,"progress":75}', 1, 175),
                      ROW('admin', '系统配置-修改邮件服务器', '192.168.1.100', 'PUT', '{"smtp":"smtp.opencode.com","port":587}', 1, 280),
                      ROW('zhangsan', '批量导入用户-10人', '192.168.1.101', 'POST', '{"count":10,"department":"开发部"}', 1, 1200),
                      ROW('lisi', '数据库备份-全量备份', '192.168.1.102', 'POST', '{"type":"full","size":"2.5GB"}', 1, 45000)) AS tmp(username, operation, ip, method, params, status, duration)
WHERE NOT EXISTS (SELECT 1 FROM sys_log LIMIT 1);

-- 插入10个知识库分类（仅在sys_wiki_category表为空时）
INSERT INTO sys_wiki_category (name, description, icon, article_count)
SELECT * FROM (VALUES ROW('新手入门', '系统使用入门指南和常见问题解答', '🚀', 25),
                      ROW('前端技术', 'Vue3、React、CSS等前端开发技术文档', '💻', 48),
                      ROW('后端技术', 'Spring Boot、MySQL、Redis等后端技术文档', '⚙️', 52),
                      ROW('产品文档', '产品需求文档、原型设计、用户手册', '📖', 35),
                      ROW('运维指南', '系统部署、监控告警、故障处理手册', '🔧', 28),
                      ROW('测试文档', '测试用例、测试报告、自动化测试脚本', '🧪', 31),
                      ROW('常见问题', 'FAQ汇总，包含使用问题和解决方案', '📋', 65),
                      ROW('最佳实践', '团队经验总结和最佳实践案例', '💡', 42),
                      ROW('API文档', '接口定义、调用示例、错误码说明', '🔌', 38),
                      ROW('设计规范', 'UI设计规范、图标库、配色方案', '🎨', 22)) AS tmp(name, description, icon, article_count)
WHERE NOT EXISTS (SELECT 1 FROM sys_wiki_category LIMIT 1);

-- 插入知识库文章（仅在sys_wiki_article表为空时）
INSERT INTO sys_wiki_article (category_id, title, summary, content, author, view_count, like_count)
SELECT * FROM (VALUES ROW(1, '欢迎使用 OpenCode 企业管理系统', '系统介绍和快速入门指南', 'OpenCode 是一个功能强大的企业管理系统，包含项目管理、任务跟踪、文档管理等多个模块。本文将介绍系统的基本使用方法...', '系统管理员', 156, 23),
                      ROW(2, 'Vue3 Composition API 最佳实践', 'Vue3 组合式API的使用技巧和注意事项', 'Composition API 是 Vue3 的重要特性，本文将介绍如何在项目中正确使用 setup 函数、ref、reactive 等核心概念...', '张三', 89, 15),
                      ROW(2, '前端性能优化指南', '提升前端应用性能的实用技巧', '性能优化是前端开发的重要课题。本文将从代码分割、懒加载、缓存策略等多个方面介绍如何提升应用性能...', '李四', 67, 12),
                      ROW(3, 'Spring Boot 3.0 新特性详解', '深入了解 Spring Boot 3.0 的主要更新', 'Spring Boot 3.0 带来了许多激动人心的新特性，包括对 Jakarta EE 9 的支持、原生镜像支持等...', '王五', 134, 28),
                      ROW(3, 'MyBatis-Plus 使用教程', '快速上手 MyBatis-Plus 插件', 'MyBatis-Plus 是 MyBatis 的增强工具，可以极大地简化数据库操作。本文将介绍常用的 CRUD 操作...', '赵六', 98, 19),
                      ROW(4, '产品需求文档编写规范', '如何编写清晰完整的产品需求文档', '一份好的 PRD 是项目成功的基础。本文将介绍 PRD 的结构、编写技巧和注意事项...', '钱七', 45, 8),
                      ROW(5, 'Docker 容器化部署指南', '使用 Docker 部署应用的完整流程', 'Docker 是现代应用部署的标准工具。本文将介绍 Dockerfile 编写、镜像构建和容器运行的完整流程...', '孙八', 78, 14),
                      ROW(6, '自动化测试实践', '构建可靠的自动化测试体系', '自动化测试是保证代码质量的重要手段。本文将介绍单元测试、集成测试和端到端测试的实践方法...', '周九', 56, 10),
                      ROW(7, '常见登录问题解决方案', '解决系统登录相关的常见问题', '本文汇总了用户在使用系统过程中遇到的登录问题及其解决方案，包括密码错误、账号锁定等情况...', '吴十', 234, 45),
                      ROW(8, '敏捷开发最佳实践', '团队协作和敏捷开发的实战经验', '敏捷开发强调快速迭代和持续交付。本文将分享我们团队在实施敏捷开发过程中的经验和教训...', '郑十一', 112, 22),
                      ROW(9, 'RESTful API 设计规范', '设计优雅易用的 RESTful 接口', '好的 API 设计能够提高开发效率和用户体验。本文将介绍 RESTful API 的设计原则和最佳实践...', '董方', 145, 31),
                      ROW(10, 'UI设计系统规范', '建立一致的视觉设计系统', '设计系统是保证产品视觉一致性的关键。本文将介绍颜色、字体、间距等设计规范的制定方法...', '西门', 67, 13)) AS tmp(category_id, title, summary, content, author, view_count, like_count)
WHERE NOT EXISTS (SELECT 1 FROM sys_wiki_article LIMIT 1);

-- 插入12个日程（仅在sys_calendar表为空时）
INSERT INTO sys_calendar (title, description, start_time, end_time, user_id, type, color)
SELECT * FROM (VALUES ROW('周一晨会', '部门周例会，汇报上周工作和本周计划', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), 1, 'meeting', '#0ea5e9'),
                      ROW('项目评审-电商平台', '电商平台重构项目进度评审', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR), 2, 'review', '#f59e0b'),
                      ROW('技术分享-Vue3新特性', '前端团队技术分享会', DATE_ADD(NOW(), INTERVAL 5 HOUR), DATE_ADD(NOW(), INTERVAL 6 HOUR), 3, 'share', '#8b5cf6'),
                      ROW('客户拜访-ABC公司', '与ABC公司讨论合作事宜', DATE_ADD(NOW(), INTERVAL 26 HOUR), DATE_ADD(NOW(), INTERVAL 28 HOUR), 4, 'event', '#10b981'),
                      ROW('产品需求评审', '新功能需求讨论和评估', DATE_ADD(NOW(), INTERVAL 27 HOUR), DATE_ADD(NOW(), INTERVAL 28 HOUR), 5, 'meeting', '#ec4899'),
                      ROW('代码审查', '核心模块代码审查会议', DATE_ADD(NOW(), INTERVAL 50 HOUR), DATE_ADD(NOW(), INTERVAL 51 HOUR), 6, 'review', '#06b6d4'),
                      ROW('团队建设活动', '季度团建聚餐', DATE_ADD(NOW(), INTERVAL 74 HOUR), DATE_ADD(NOW(), INTERVAL 78 HOUR), 1, 'event', '#f472b6'),
                      ROW('安全培训', '信息安全意识培训', DATE_ADD(NOW(), INTERVAL 75 HOUR), DATE_ADD(NOW(), INTERVAL 77 HOUR), 7, 'training', '#ef4444'),
                      ROW('项目启动会-智能客服', '智能客服项目正式启动', DATE_ADD(NOW(), INTERVAL 98 HOUR), DATE_ADD(NOW(), INTERVAL 99 HOUR), 2, 'meeting', '#6366f1'),
                      ROW('季度总结会', 'Q1季度工作总结和规划', DATE_ADD(NOW(), INTERVAL 122 HOUR), DATE_ADD(NOW(), INTERVAL 124 HOUR), 1, 'meeting', '#14b8a6'),
                      ROW('架构评审', '系统架构方案评审', DATE_ADD(NOW(), INTERVAL 123 HOUR), DATE_ADD(NOW(), INTERVAL 125 HOUR), 3, 'review', '#64748b'),
                      ROW('上线发布会', '企业管理系统V2.0上线发布', DATE_ADD(NOW(), INTERVAL 146 HOUR), DATE_ADD(NOW(), INTERVAL 148 HOUR), 1, 'event', '#84cc16')) AS tmp(title, description, start_time, end_time, user_id, type, color)
WHERE NOT EXISTS (SELECT 1 FROM sys_calendar LIMIT 1);

-- 插入10个报表（仅在sys_report表为空时）
INSERT INTO sys_report (title, description, icon, type, url)
SELECT * FROM (VALUES ROW('2024年3月销售报表', '包含各区域销售额、增长率、TOP10客户统计', '📊', 'sales', '/reports/sales_202403.pdf'),
                      ROW('用户增长分析报告', '新用户注册、活跃留存、用户画像分析', '👥', 'user', '/reports/user_growth_202403.pdf'),
                      ROW('月度财务报表', '收入支出明细、利润分析、现金流报表', '💰', 'finance', '/reports/finance_202403.pdf'),
                      ROW('库存周转报表', '库存周转率、滞销品分析、补货建议', '📦', 'inventory', '/reports/inventory_202403.pdf'),
                      ROW('团队绩效排名', '各部门KPI完成情况和个人绩效排名', '🎯', 'performance', '/reports/performance_202403.pdf'),
                      ROW('网站流量分析', 'PV/UV统计、流量来源、页面跳出率分析', '📱', 'traffic', '/reports/traffic_202403.pdf'),
                      ROW('项目进度总览', '所有项目进度甘特图和里程碑完成情况', '📅', 'project', '/reports/project_progress_202403.pdf'),
                      ROW('系统运行监控', '服务器负载、接口响应时间、错误率统计', '💻', 'system', '/reports/system_monitor_202403.pdf'),
                      ROW('客户满意度调查', '客户反馈统计、满意度评分、改进建议', '😊', 'customer', '/reports/satisfaction_202403.pdf'),
                      ROW('代码质量报告', '代码覆盖率、Bug统计、技术债务分析', '🔍', 'quality', '/reports/code_quality_202403.pdf')) AS tmp(title, description, icon, type, url)
WHERE NOT EXISTS (SELECT 1 FROM sys_report LIMIT 1);

-- 插入3条系统设置（仅在sys_settings表为空时）
INSERT INTO sys_settings (user_id, dark_mode, email_notify, desktop_notify, language, timezone, bio)
SELECT * FROM (VALUES ROW(1, 0, 1, 1, 'zh-CN', 'Asia/Shanghai', '系统管理员，负责平台维护和用户支持'),
                      ROW(2, 1, 1, 0, 'zh-CN', 'Asia/Shanghai', '产品经理，专注于用户体验和功能设计'),
                      ROW(3, 0, 1, 1, 'en-US', 'America/New_York', 'Full-stack developer, passionate about clean code')) AS tmp(user_id, dark_mode, email_notify, desktop_notify, language, timezone, bio)
WHERE NOT EXISTS (SELECT 1 FROM sys_settings LIMIT 1);
