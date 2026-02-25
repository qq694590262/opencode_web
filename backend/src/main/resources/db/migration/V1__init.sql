-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) COMMENT '姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    department VARCHAR(50) COMMENT '部门',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色',
    status INT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    user_count INT DEFAULT 0 COMMENT '用户数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 部门表
CREATE TABLE IF NOT EXISTS sys_department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    name VARCHAR(50) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 项目表
CREATE TABLE IF NOT EXISTS sys_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目ID',
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    description VARCHAR(500) COMMENT '项目描述',
    icon VARCHAR(50) COMMENT '项目图标',
    color VARCHAR(20) COMMENT '主题颜色',
    member_count INT DEFAULT 0 COMMENT '成员数量',
    start_date DATETIME COMMENT '开始日期',
    progress INT DEFAULT 0 COMMENT '进度',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 任务表
CREATE TABLE IF NOT EXISTS sys_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
    title VARCHAR(100) NOT NULL COMMENT '任务标题',
    description VARCHAR(500) COMMENT '任务描述',
    priority VARCHAR(20) DEFAULT 'medium' COMMENT '优先级',
    status VARCHAR(20) DEFAULT 'todo' COMMENT '状态',
    assignee VARCHAR(50) COMMENT '负责人',
    project_id BIGINT COMMENT '项目ID',
    due_date DATETIME COMMENT '截止日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 文档表
CREATE TABLE IF NOT EXISTS sys_document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文档ID',
    name VARCHAR(100) NOT NULL COMMENT '文档名称',
    description VARCHAR(500) COMMENT '文档描述',
    icon VARCHAR(50) COMMENT '文档图标',
    size VARCHAR(20) COMMENT '文件大小',
    type VARCHAR(20) COMMENT '文件类型',
    url VARCHAR(255) COMMENT '文件URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档表';

-- 日志表
CREATE TABLE IF NOT EXISTS sys_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    username VARCHAR(50) COMMENT '操作用户',
    operation VARCHAR(200) COMMENT '操作描述',
    module VARCHAR(50) COMMENT '操作模块',
    ip VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    method VARCHAR(100) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    status INT DEFAULT 1 COMMENT '状态',
    error_msg TEXT COMMENT '错误信息',
    duration BIGINT COMMENT '耗时',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

-- 知识库分类表
CREATE TABLE IF NOT EXISTS sys_wiki_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述',
    icon VARCHAR(50) COMMENT '分类图标',
    article_count INT DEFAULT 0 COMMENT '文章数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库分类表';

-- 知识库文章表
CREATE TABLE IF NOT EXISTS sys_wiki_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    title VARCHAR(200) NOT NULL COMMENT '文章标题',
    summary VARCHAR(500) COMMENT '文章摘要',
    content TEXT COMMENT '文章内容',
    author VARCHAR(50) COMMENT '作者',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    INDEX idx_category (category_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文章表';

-- 设置表
CREATE TABLE IF NOT EXISTS sys_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '设置ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    dark_mode TINYINT DEFAULT 0 COMMENT '深色模式',
    email_notify TINYINT DEFAULT 1 COMMENT '邮件通知',
    desktop_notify TINYINT DEFAULT 0 COMMENT '桌面通知',
    language VARCHAR(20) DEFAULT 'zh-CN' COMMENT '语言',
    timezone VARCHAR(50) DEFAULT 'Asia/Shanghai' COMMENT '时区',
    bio VARCHAR(500) COMMENT '个人简介',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设置表';

-- 日程表
CREATE TABLE IF NOT EXISTS sys_calendar (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日程ID',
    title VARCHAR(100) NOT NULL COMMENT '日程标题',
    description VARCHAR(500) COMMENT '日程描述',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    user_id BIGINT COMMENT '用户ID',
    type VARCHAR(20) DEFAULT 'event' COMMENT '类型',
    color VARCHAR(20) DEFAULT '#0ea5e9' COMMENT '颜色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程表';

-- 报表表
CREATE TABLE IF NOT EXISTS sys_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '报表ID',
    title VARCHAR(100) NOT NULL COMMENT '报表标题',
    description VARCHAR(255) COMMENT '报表描述',
    icon VARCHAR(50) COMMENT '报表图标',
    type VARCHAR(50) COMMENT '报表类型',
    url VARCHAR(255) COMMENT '报表URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS sys_system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    system_name VARCHAR(100) DEFAULT 'OpenCode 企业管理系统' COMMENT '系统名称',
    copyright VARCHAR(255) DEFAULT '© 2024 OpenCode' COMMENT '版权信息',
    login_timeout INT DEFAULT 30 COMMENT '登录超时',
    password_expire INT DEFAULT 90 COMMENT '密码过期天数',
    max_login_fail INT DEFAULT 5 COMMENT '最大登录失败次数',
    max_devices INT DEFAULT 3 COMMENT '最大设备数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 便签表
CREATE TABLE IF NOT EXISTS sys_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '便签ID',
    title VARCHAR(100) COMMENT '便签标题',
    content TEXT COMMENT '便签内容',
    color VARCHAR(20) DEFAULT '#fef3c7' COMMENT '便签颜色',
    position INT DEFAULT 0 COMMENT '位置顺序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='便签表';

-- 待办事项表
CREATE TABLE IF NOT EXISTS sys_todo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '待办ID',
    title VARCHAR(100) NOT NULL COMMENT '待办标题',
    content VARCHAR(500) COMMENT '待办描述',
    priority INT DEFAULT 1 COMMENT '优先级',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态',
    due_date DATETIME COMMENT '截止日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';

-- 插入默认系统配置
INSERT INTO sys_system_config (system_name, copyright, login_timeout, password_expire, max_login_fail, max_devices) VALUES ('OpenCode 企业管理系统', '© 2024 OpenCode', 30, 90, 5, 3);

-- 插入用户
INSERT INTO sys_user (username, password, name, email, phone, department, role, status) VALUES ('admin', 'password', '系统管理员', 'admin@opencode.com', '13800138000', '技术部', 'ADMIN', 1), ('zhangsan', 'password', '张三', 'zhangsan@opencode.com', '13800138001', '产品部', 'USER', 1), ('lisi', 'password', '李四', 'lisi@opencode.com', '13800138002', '开发部', 'EDITOR', 1), ('wangwu', 'password', '王五', 'wangwu@opencode.com', '13800138003', '测试部', 'USER', 1), ('zhaoliu', 'password', '赵六', 'zhaoliu@opencode.com', '13800138004', '运维部', 'USER', 1), ('qianqi', 'password', '钱七', 'qianqi@opencode.com', '13800138005', '设计部', 'USER', 1);

-- 插入角色
INSERT INTO sys_role (name, code, description, user_count) VALUES ('超级管理员', 'SUPER_ADMIN', '拥有系统所有权限', 1), ('系统管理员', 'ADMIN', '管理系统配置和用户权限', 3), ('产品经理', 'PRODUCT_MANAGER', '负责产品规划', 5), ('开发工程师', 'DEVELOPER', '负责系统开发', 25), ('测试工程师', 'TESTER', '负责系统测试', 12), ('运维工程师', 'OPERATOR', '负责系统运维', 8), ('内容编辑', 'EDITOR', '负责内容编辑', 15), ('普通用户', 'USER', '普通访问权限', 150);

-- 插入部门
INSERT INTO sys_department (name, code, parent_id, leader, phone, sort, status) VALUES ('总公司', 'HQ', 0, '张三', '13800138000', 0, 1), ('技术部', 'TECH', 1, '李四', '13800138001', 1, 1), ('市场部', 'MARKET', 1, '王五', '13800138002', 2, 1), ('财务部', 'FIN', 1, '赵六', '13800138003', 3, 1), ('人事部', 'HR', 1, '钱七', '13800138004', 4, 1);

-- 插入项目
INSERT INTO sys_project (name, description, icon, color, member_count, progress, status, start_date) VALUES ('企业管理系统V2.0', '全面升级企业内部管理系统', '💼', '#0ea5e9', 12, 75, 'active', '2024-01-15'), ('电商平台重构', '基于微服务架构重构电商平台', '🛒', '#34d399', 18, 60, 'active', '2024-02-01'), ('移动端APP开发', '开发iOS和Android双平台APP', '📱', '#f472b6', 8, 30, 'active', '2024-03-01');

-- 插入任务
INSERT INTO sys_task (title, description, priority, status, assignee, project_id, due_date) VALUES ('完成用户模块API开发', '开发用户注册登录接口', 'high', 'done', '张三', 1, '2024-02-15'), ('设计系统首页UI', '设计响应式首页', 'high', 'done', '李四', 1, '2024-02-10'), ('编写接口文档', '使用Swagger编写API文档', 'medium', 'inprogress', '王五', 1, '2024-03-01');

-- 插入文档
INSERT INTO sys_document (name, description, icon, size, type, url) VALUES ('需求规格说明书.docx', '详细描述功能需求', '📕', '5.2 MB', 'docx', '/docs/requirements.docx'), ('系统架构设计.pdf', '微服务架构设计', '📗', '8.5 MB', 'pdf', '/docs/architecture.pdf');

-- 插入知识库分类
INSERT INTO sys_wiki_category (name, description, icon, article_count) VALUES ('新手入门', '系统使用入门指南', '🚀', 25), ('前端技术', 'Vue3 React等前端文档', '💻', 48), ('后端技术', 'Spring Boot MySQL等文档', '⚙️', 52);

-- 插入日程
INSERT INTO sys_calendar (title, description, start_time, end_time, user_id, type, color) VALUES ('周一晨会', '部门周例会', DATE_ADD(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 2 HOUR), 1, 'meeting', '#0ea5e9'), ('项目评审', '项目进度评审', DATE_ADD(NOW(), INTERVAL 3 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR), 2, 'review', '#f59e0b');

-- 插入报表
INSERT INTO sys_report (title, description, icon, type, url) VALUES ('销售报表', '月度销售统计', '📊', 'sales', '/reports/sales.pdf'), ('用户分析', '用户增长分析', '👥', 'user', '/reports/user.pdf');

-- 插入设置
INSERT INTO sys_settings (user_id, dark_mode, email_notify, desktop_notify, language, timezone, bio) VALUES (1, 0, 1, 1, 'zh-CN', 'Asia/Shanghai', '系统管理员'), (2, 1, 1, 0, 'zh-CN', 'Asia/Shanghai', '产品经理');
