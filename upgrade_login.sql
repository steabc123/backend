-- ============================================
-- 水果店管理系统 - 用户登录功能升级脚本
-- ============================================

-- 1. 修改customers表，添加登录字段
ALTER TABLE customers 
ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT '123456' COMMENT '密码（默认123456）',
ADD COLUMN role ENUM('customer', 'admin') NOT NULL DEFAULT 'customer' COMMENT '角色：customer-顾客，admin-管理员';

-- 2. 为现有用户设置默认密码
UPDATE customers SET password = '123456', role = 'customer';

-- 3. 插入管理员账号 (name: root, password: root)
INSERT INTO customers (name, phone, gender, address, password, role, created_at) 
VALUES ('root', 'root', '男', '系统管理', 'root', 'admin', NOW());

-- 4. 添加索引优化查询性能
CREATE INDEX idx_name ON customers(name);
CREATE INDEX idx_role ON customers(role);

-- 5. 验证数据
SELECT id, name, password, role FROM customers;
