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

-- 插入默认管理员用户
INSERT INTO sys_user (username, password, name, email, role, status) 
VALUES ('admin', 'password', '管理员', 'admin@company.com', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE name = '管理员';

-- 插入示例用户
INSERT INTO sys_user (username, password, name, email, role, status) VALUES
('zhangsan', 'password', '张三', 'zhangsan@company.com', 'USER', 1),
('lisi', 'password', '李四', 'lisi@company.com', 'EDITOR', 1),
('wangwu', 'password', '王五', 'wangwu@company.com', 'USER', 0),
('zhaoliu', 'password', '赵六', 'zhaoliu@company.com', 'USER', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入示例角色
INSERT INTO sys_role (name, code, description, user_count) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 2),
('管理员', 'ADMIN', '管理所有模块和用户', 5),
('编辑', 'EDITOR', '内容编辑和发布权限', 15),
('访客', 'VISITOR', '只读访问权限', 128)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入示例项目
INSERT INTO sys_project (name, description, icon, color, member_count, progress, status) VALUES
('企业管理系统', '企业内部管理系统开发', '💼', '#0ea5e9', 8, 75, 'active'),
('电商平台', 'B2C 电商网站', '🛒', '#34d399', 12, 90, 'active'),
('移动端APP', 'iOS/Android 应用', '📱', '#f472b6', 5, 30, 'pending'),
('安全审计系统', '系统安全检测工具', '🔒', '#fbbf24', 3, 100, 'done')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入示例任务
INSERT INTO sys_task (title, description, priority, status, assignee) VALUES
('完成首页设计', '完成登录页和仪表盘设计稿', 'high', 'todo', '张三'),
('API 接口对接', '对接用户管理模块接口', 'medium', 'inprogress', '李四'),
('数据库优化', '优化查询性能', 'high', 'done', '王五'),
('单元测试', '编写核心模块测试用例', 'low', 'inprogress', '赵六');

-- 插入示例文档
INSERT INTO sys_document (name, description, icon, size, type) VALUES
('项目需求文档.docx', '企业管理系统需求说明', '📕', '2.3 MB', 'docx'),
('技术方案.pdf', '系统架构设计文档', '📗', '5.1 MB', 'pdf'),
('API接口文档.md', '接口定义与说明', '📘', '1.2 MB', 'md'),
('测试用例.xlsx', '功能测试用例', '📙', '856 KB', 'xlsx');

-- 插入示例日志
INSERT INTO sys_log (username, operation, ip, method) VALUES
('张三', '登录系统', '192.168.1.101', 'POST'),
('李四', '编辑用户资料', '192.168.1.102', 'PUT'),
('王五', '导出报表', '192.168.1.103', 'GET'),
('赵六', '删除文档', '192.168.1.104', 'DELETE');

-- 插入知识库分类
INSERT INTO sys_wiki_category (name, description, icon, article_count) VALUES
('新手入门', '快速上手指南', '🚀', 12),
('技术文档', '开发技术文档', '💻', 28),
('产品知识', '产品功能介绍', '📖', 15),
('运维指南', '系统运维手册', '🔧', 8),
('常见问题', 'FAQ 汇总', '📋', 20),
('最佳实践', '经验与技巧', '💡', 16)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入日程数据
INSERT INTO sys_calendar (title, description, start_time, type) VALUES
('晨会', '部门周例会', DATE_ADD(NOW(), INTERVAL 1 HOUR), 'meeting'),
('项目评审', '企业管理系统评审', DATE_ADD(NOW(), INTERVAL 3 HOUR), 'review'),
('技术分享', 'Vue3 最佳实践', DATE_ADD(NOW(), INTERVAL 5 HOUR), 'share');

-- 插入报表数据
INSERT INTO sys_report (title, description, icon, type) VALUES
('销售报表', '月度销售数据汇总', '📊', 'sales'),
('用户报表', '用户增长与留存分析', '👥', 'user'),
('财务报表', '收入支出明细', '💰', 'finance'),
('库存报表', '库存周转情况', '📦', 'inventory'),
('业绩报表', '团队业绩排名', '🎯', 'performance'),
('流量报表', '网站访问分析', '📱', 'traffic')
ON DUPLICATE KEY UPDATE title = VALUES(title);
