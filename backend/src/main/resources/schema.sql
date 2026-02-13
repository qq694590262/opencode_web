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

-- ============================================
-- 插入测试数据（每个表10条以上）
-- ============================================

-- 插入12个用户（1个管理员 + 11个普通用户）
INSERT INTO sys_user (username, password, name, email, phone, department, role, status) VALUES
('admin', 'password', '系统管理员', 'admin@opencode.com', '13800138000', '技术部', 'ADMIN', 1),
('zhangsan', 'password', '张三', 'zhangsan@opencode.com', '13800138001', '产品部', 'USER', 1),
('lisi', 'password', '李四', 'lisi@opencode.com', '13800138002', '开发部', 'EDITOR', 1),
('wangwu', 'password', '王五', 'wangwu@opencode.com', '13800138003', '测试部', 'USER', 1),
('zhaoliu', 'password', '赵六', 'zhaoliu@opencode.com', '13800138004', '运维部', 'USER', 1),
('qianqi', 'password', '钱七', 'qianqi@opencode.com', '13800138005', '设计部', 'USER', 1),
('sunba', 'password', '孙八', 'sunba@opencode.com', '13800138006', '市场部', 'USER', 0),
('zhoujiu', 'password', '周九', 'zhoujiu@opencode.com', '13800138007', '销售部', 'USER', 1),
('wushi', 'password', '吴十', 'wushi@opencode.com', '13800138008', '客服部', 'USER', 1),
('zhengshi', 'password', '郑十一', 'zhengshi@opencode.com', '13800138009', '财务部', 'USER', 1),
('dongfang', 'password', '董方', 'dongfang@opencode.com', '13800138010', '人事部', 'USER', 1),
('ximen', 'password', '西门', 'ximen@opencode.com', '13800138011', '行政部', 'USER', 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入8个角色
INSERT INTO sys_role (name, code, description, user_count) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有系统所有权限，可管理所有模块', 1),
('系统管理员', 'ADMIN', '管理系统配置和用户权限', 3),
('产品经理', 'PRODUCT_MANAGER', '负责产品规划和需求管理', 5),
('开发工程师', 'DEVELOPER', '负责系统开发和维护', 25),
('测试工程师', 'TESTER', '负责系统测试和质量保证', 12),
('运维工程师', 'OPERATOR', '负责系统部署和运维', 8),
('内容编辑', 'EDITOR', '负责内容编辑和发布', 15),
('普通用户', 'USER', '普通访问权限', 150)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入12个项目
INSERT INTO sys_project (name, description, icon, color, member_count, progress, status, start_date) VALUES
('企业管理系统V2.0', '全面升级企业内部管理系统，包含OA、CRM、ERP模块', '💼', '#0ea5e9', 12, 75, 'active', '2024-01-15'),
('电商平台重构', '基于微服务架构重构电商平台，提升性能和扩展性', '🛒', '#34d399', 18, 60, 'active', '2024-02-01'),
('移动端APP开发', '开发iOS和Android双平台APP，支持离线功能', '📱', '#f472b6', 8, 30, 'active', '2024-03-01'),
('安全审计系统', '建立完整的安全审计体系，包含日志分析和异常检测', '🔒', '#fbbf24', 5, 100, 'done', '2023-11-01'),
('数据可视化平台', '构建企业级数据可视化平台，支持多数据源接入', '📊', '#8b5cf6', 10, 45, 'active', '2024-02-15'),
('智能客服系统', '基于AI的智能客服系统，支持自然语言处理', '🤖', '#06b6d4', 6, 20, 'pending', '2024-04-01'),
('供应链管理系统', '优化供应链管理流程，实现全程可追溯', '📦', '#f59e0b', 14, 80, 'active', '2023-12-01'),
('人力资源系统', '数字化人力资源管理，包含招聘、绩效、培训模块', '👥', '#ec4899', 7, 55, 'active', '2024-01-20'),
('财务管理系统', '企业财务一体化管理，支持多账套和报表分析', '💰', '#10b981', 9, 90, 'active', '2023-10-15'),
('知识管理系统', '企业知识沉淀和共享平台，支持全文检索', '📚', '#6366f1', 4, 100, 'done', '2023-09-01'),
('物联网平台', '连接和管理IoT设备，支持实时监控和预警', '🌐', '#14b8a6', 11, 35, 'active', '2024-02-20'),
('区块链存证系统', '基于区块链的数据存证和溯源系统', '⛓️', '#64748b', 3, 15, 'pending', '2024-05-01')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入15个任务
INSERT INTO sys_task (title, description, priority, status, assignee, project_id, due_date) VALUES
('完成用户模块API开发', '开发用户注册、登录、权限管理相关接口', 'high', 'done', '张三', 1, '2024-02-15'),
('设计系统首页UI', '设计响应式首页，包含数据看板和快捷入口', 'high', 'done', '李四', 1, '2024-02-10'),
('编写接口文档', '使用Swagger编写完整的API文档', 'medium', 'inprogress', '王五', 1, '2024-03-01'),
('数据库性能优化', '优化慢查询，添加索引，提升查询效率', 'high', 'todo', '赵六', 2, '2024-03-15'),
('单元测试覆盖率提升', '将核心模块测试覆盖率提升到80%以上', 'medium', 'inprogress', '钱七', 2, '2024-03-20'),
('iOS首页开发', '开发iOS端首页，实现下拉刷新和上拉加载', 'high', 'inprogress', '孙八', 3, '2024-04-01'),
('Android适配测试', '在主流Android设备上进行适配测试', 'medium', 'todo', '周九', 3, '2024-04-15'),
('安全漏洞修复', '修复渗透测试发现的安全漏洞', 'high', 'done', '吴十', 4, '2024-01-30'),
('日志分析模块', '开发日志收集和分析模块', 'low', 'done', '郑十一', 4, '2024-01-20'),
('数据大屏开发', '开发实时数据可视化大屏', 'high', 'inprogress', '董方', 5, '2024-03-30'),
('客服机器人训练', '训练NLP模型，提升问答准确率', 'medium', 'todo', '西门', 6, '2024-05-15'),
('供应商管理模块', '开发供应商准入和评价模块', 'medium', 'inprogress', '张三', 7, '2024-03-10'),
('员工绩效考核', '开发绩效考核打分和统计功能', 'low', 'todo', '李四', 8, '2024-04-01'),
('财务报表导出', '支持PDF和Excel格式的财务报表导出', 'medium', 'done', '王五', 9, '2024-02-28'),
('知识库分类整理', '对现有知识文档进行分类整理', 'low', 'done', '赵六', 10, '2024-01-15');

-- 插入12个文档
INSERT INTO sys_document (name, description, icon, size, type, url) VALUES
('企业管理系统需求规格说明书V2.0.docx', '详细描述V2.0版本的功能需求和技术要求', '📕', '5.2 MB', 'docx', '/docs/requirements_v2.docx'),
('系统架构设计文档.pdf', '微服务架构设计，包含服务拆分和接口定义', '📗', '8.5 MB', 'pdf', '/docs/architecture.pdf'),
('API接口文档V1.0.md', 'RESTful API接口详细说明，包含请求响应示例', '📘', '1.8 MB', 'md', '/docs/api_v1.md'),
('测试用例全集.xlsx', '功能测试、性能测试、安全测试用例汇总', '📙', '2.1 MB', 'xlsx', '/docs/test_cases.xlsx'),
('用户操作手册.pdf', '面向终端用户的详细操作指南', '📓', '12.4 MB', 'pdf', '/docs/user_manual.pdf'),
('数据库设计文档.pdm', 'PowerDesigner数据库设计模型文件', '📔', '3.6 MB', 'pdm', '/docs/database_design.pdm'),
('项目进度计划.mpp', 'Microsoft Project项目进度管理文件', '📊', '1.2 MB', 'mpp', '/docs/schedule.mpp'),
('代码规范文档.md', '团队代码编写规范和最佳实践', '📄', '856 KB', 'md', '/docs/coding_standards.md'),
('部署运维手册.pdf', '系统部署、监控、故障处理指南', '🔧', '4.3 MB', 'pdf', '/docs/operations.pdf'),
('安全审计报告.pdf', '第三方安全公司出具的安全审计报告', '🛡️', '2.8 MB', 'pdf', '/docs/security_audit.pdf'),
('培训教材.pptx', '新员工入职培训使用的PPT教材', '🎓', '15.6 MB', 'pptx', '/docs/training.pptx'),
('会议纪要-2024-03-01.docx', '3月1日项目周会会议纪要和待办事项', '📝', '456 KB', 'docx', '/docs/meeting_20240301.docx');

-- 插入15条操作日志
INSERT INTO sys_log (username, operation, ip, method, params, status, duration) VALUES
('admin', '登录系统', '192.168.1.100', 'POST', '{"username":"admin"}', 1, 120),
('zhangsan', '创建新项目-企业管理系统V2.0', '192.168.1.101', 'POST', '{"name":"企业管理系统V2.0"}', 1, 350),
('lisi', '编辑用户信息-修改手机号', '192.168.1.102', 'PUT', '{"userId":3,"phone":"13800138002"}', 1, 180),
('wangwu', '删除任务-完成首页设计', '192.168.1.103', 'DELETE', '{"taskId":2}', 1, 95),
('zhaoliu', '导出用户列表Excel', '192.168.1.104', 'GET', '{"format":"excel"}', 1, 2500),
('qianqi', '上传文档-设计规范V2.0.pdf', '192.168.1.105', 'POST', '{"filename":"设计规范V2.0.pdf","size":"5.2MB"}', 1, 3200),
('sunba', '修改个人设置-开启深色模式', '192.168.1.106', 'PUT', '{"darkMode":true}', 1, 85),
('zhoujiu', '创建日程-客户拜访', '192.168.1.107', 'POST', '{"title":"客户拜访","time":"2024-03-15 14:00"}', 1, 150),
('wushi', '生成报表-月度销售统计', '192.168.1.108', 'GET', '{"type":"sales","month":"2024-02"}', 1, 5600),
('zhengshi', '审批通过-请假申请', '192.168.1.109', 'PUT', '{"leaveId":15,"status":"approved"}', 1, 200),
('dongfang', '发布知识库文章-Vue3最佳实践', '192.168.1.110', 'POST', '{"title":"Vue3最佳实践","category":"技术文档"}', 1, 420),
('ximen', '修改项目进度-电商平台重构', '192.168.1.111', 'PUT', '{"projectId":2,"progress":75}', 1, 175),
('admin', '系统配置-修改邮件服务器', '192.168.1.100', 'PUT', '{"smtp":"smtp.opencode.com","port":587}', 1, 280),
('zhangsan', '批量导入用户-10人', '192.168.1.101', 'POST', '{"count":10,"department":"开发部"}', 1, 1200),
('lisi', '数据库备份-全量备份', '192.168.1.102', 'POST', '{"type":"full","size":"2.5GB"}', 1, 45000);

-- 插入10个知识库分类
INSERT INTO sys_wiki_category (name, description, icon, article_count) VALUES
('新手入门', '系统使用入门指南和常见问题解答', '🚀', 25),
('前端技术', 'Vue3、React、CSS等前端开发技术文档', '💻', 48),
('后端技术', 'Spring Boot、MySQL、Redis等后端技术文档', '⚙️', 52),
('产品文档', '产品需求文档、原型设计、用户手册', '📖', 35),
('运维指南', '系统部署、监控告警、故障处理手册', '🔧', 28),
('测试文档', '测试用例、测试报告、自动化测试脚本', '🧪', 31),
('常见问题', 'FAQ汇总，包含使用问题和解决方案', '📋', 65),
('最佳实践', '团队经验总结和最佳实践案例', '💡', 42),
('API文档', '接口定义、调用示例、错误码说明', '🔌', 38),
('设计规范', 'UI设计规范、图标库、配色方案', '🎨', 22)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 插入12个日程
INSERT INTO sys_calendar (title, description, start_time, end_time, user_id, type, color) VALUES
('周一晨会', '部门周例会，汇报上周工作和本周计划', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), 1, 'meeting', '#0ea5e9'),
('项目评审-电商平台', '电商平台重构项目进度评审', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR), 2, 'review', '#f59e0b'),
('技术分享-Vue3新特性', '前端团队技术分享会', DATE_ADD(NOW(), INTERVAL 5 HOUR), DATE_ADD(NOW(), INTERVAL 6 HOUR), 3, 'share', '#8b5cf6'),
('客户拜访-ABC公司', '与ABC公司讨论合作事宜', DATE_ADD(NOW(), INTERVAL 26 HOUR), DATE_ADD(NOW(), INTERVAL 28 HOUR), 4, 'event', '#10b981'),
('产品需求评审', '新功能需求讨论和评估', DATE_ADD(NOW(), INTERVAL 27 HOUR), DATE_ADD(NOW(), INTERVAL 28 HOUR), 5, 'meeting', '#ec4899'),
('代码审查', '核心模块代码审查会议', DATE_ADD(NOW(), INTERVAL 50 HOUR), DATE_ADD(NOW(), INTERVAL 51 HOUR), 6, 'review', '#06b6d4'),
('团队建设活动', '季度团建聚餐', DATE_ADD(NOW(), INTERVAL 74 HOUR), DATE_ADD(NOW(), INTERVAL 78 HOUR), 1, 'event', '#f472b6'),
('安全培训', '信息安全意识培训', DATE_ADD(NOW(), INTERVAL 75 HOUR), DATE_ADD(NOW(), INTERVAL 77 HOUR), 7, 'training', '#ef4444'),
('项目启动会-智能客服', '智能客服项目正式启动', DATE_ADD(NOW(), INTERVAL 98 HOUR), DATE_ADD(NOW(), INTERVAL 99 HOUR), 2, 'meeting', '#6366f1'),
('季度总结会', 'Q1季度工作总结和规划', DATE_ADD(NOW(), INTERVAL 122 HOUR), DATE_ADD(NOW(), INTERVAL 124 HOUR), 1, 'meeting', '#14b8a6'),
('架构评审', '系统架构方案评审', DATE_ADD(NOW(), INTERVAL 123 HOUR), DATE_ADD(NOW(), INTERVAL 125 HOUR), 3, 'review', '#64748b'),
('上线发布会', '企业管理系统V2.0上线发布', DATE_ADD(NOW(), INTERVAL 146 HOUR), DATE_ADD(NOW(), INTERVAL 148 HOUR), 1, 'event', '#84cc16');

-- 插入10个报表
INSERT INTO sys_report (title, description, icon, type, url) VALUES
('2024年3月销售报表', '包含各区域销售额、增长率、TOP10客户统计', '📊', 'sales', '/reports/sales_202403.pdf'),
('用户增长分析报告', '新用户注册、活跃留存、用户画像分析', '👥', 'user', '/reports/user_growth_202403.pdf'),
('月度财务报表', '收入支出明细、利润分析、现金流报表', '💰', 'finance', '/reports/finance_202403.pdf'),
('库存周转报表', '库存周转率、滞销品分析、补货建议', '📦', 'inventory', '/reports/inventory_202403.pdf'),
('团队绩效排名', '各部门KPI完成情况和个人绩效排名', '🎯', 'performance', '/reports/performance_202403.pdf'),
('网站流量分析', 'PV/UV统计、流量来源、页面跳出率分析', '📱', 'traffic', '/reports/traffic_202403.pdf'),
('项目进度总览', '所有项目进度甘特图和里程碑完成情况', '📅', 'project', '/reports/project_progress_202403.pdf'),
('系统运行监控', '服务器负载、接口响应时间、错误率统计', '💻', 'system', '/reports/system_monitor_202403.pdf'),
('客户满意度调查', '客户反馈统计、满意度评分、改进建议', '😊', 'customer', '/reports/satisfaction_202403.pdf'),
('代码质量报告', '代码覆盖率、Bug统计、技术债务分析', '🔍', 'quality', '/reports/code_quality_202403.pdf')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- 插入3条系统设置（对应前3个用户）
INSERT INTO sys_settings (user_id, dark_mode, email_notify, desktop_notify, language, timezone, bio) VALUES
(1, 0, 1, 1, 'zh-CN', 'Asia/Shanghai', '系统管理员，负责平台维护和用户支持'),
(2, 1, 1, 0, 'zh-CN', 'Asia/Shanghai', '产品经理，专注于用户体验和功能设计'),
(3, 0, 1, 1, 'en-US', 'America/New_York', 'Full-stack developer, passionate about clean code')
ON DUPLICATE KEY UPDATE bio = VALUES(bio);
