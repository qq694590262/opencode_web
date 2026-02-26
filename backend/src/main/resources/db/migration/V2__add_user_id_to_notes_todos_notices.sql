-- Add user_id to note table for user-specific notes
ALTER TABLE sys_note ADD COLUMN user_id BIGINT DEFAULT 1 COMMENT '用户ID';

-- Add user_id to todo table for user-specific todos
ALTER TABLE sys_todo ADD COLUMN user_id BIGINT DEFAULT 1 COMMENT '用户ID';

-- Create notice table if not exists (for user-specific notices)
CREATE TABLE IF NOT EXISTS sys_notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    title VARCHAR(100) NOT NULL COMMENT '公告标题',
    content TEXT COMMENT '公告内容',
    type VARCHAR(20) DEFAULT 'info' COMMENT '公告类型',
    publisher VARCHAR(50) COMMENT '发布人',
    user_id BIGINT DEFAULT 0 COMMENT '目标用户ID,0表示全部用户',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读',
    views INT DEFAULT 0 COMMENT '阅读量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '删除标记',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- Insert sample notices
INSERT INTO sys_notice (title, content, type, publisher, user_id) VALUES 
('系统升级通知', '为了提供更好的服务，系统将于2024年1月15日凌晨2:00-4:00进行升级维护，届时部分功能可能无法使用，请提前做好工作安排，感谢您的理解与支持！', 'warning', '管理员', 0),
('新功能上线公告', '本次更新新增了部门管理、消息中心等功能，优化了用户体验，欢迎大家试用并提出宝贵意见！', 'success', '管理员', 0),
('关于加强账号安全的通知', '为保障账号安全，建议定期修改密码，密码长度不少于8位，包含字母、数字和特殊字符。如发现异常登录，请及时联系管理员。', 'danger', '安全中心', 0),
('春节放假安排', '根据国家法定假日安排，春节期间放假时间为2月9日至2月15日，放假期间如有紧急事务，请联系值班人员。', 'info', '行政部', 0);
