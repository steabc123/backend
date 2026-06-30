-- ============================================
-- 水果店管理系统数据库
-- 包含5张表、3个外键、完整性约束
-- ============================================

-- 删除已存在的表（按依赖关系逆序）
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS fruits;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS suppliers;

-- ============================================
-- 1. 顾客表 (customers)
-- ============================================
CREATE TABLE customers (
                           id INT PRIMARY KEY AUTO_INCREMENT COMMENT '顾客ID',
                           name VARCHAR(50) NOT NULL COMMENT '姓名',
                           phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
                           gender ENUM('男', '女') NOT NULL DEFAULT '男' COMMENT '性别',
                           address VARCHAR(200) COMMENT '地址',
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                           CONSTRAINT chk_gender CHECK (gender IN ('男', '女'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='顾客表';

-- 插入顾客数据（7行）
INSERT INTO customers (name, phone, gender, address, created_at) VALUES
                                                                     ('张三', '13800138001', '男', '北京市朝阳区建国路100号', '2025-01-15 10:30:00'),
                                                                     ('李四', '13800138002', '女', '上海市浦东新区陆家嘴200号', '2024-02-20 14:20:00'),
                                                                     ('王五', '13800138003', '男', '广州市天河区体育西路300号', '2023-03-10 09:15:00'),
                                                                     ('赵敏', '13800138004', '女', '深圳市南山区科技园400号', '2024-04-05 16:45:00'),
                                                                     ('陈伟', '13800138005', '男', '杭州市西湖区文三路500号', '2025-05-12 11:30:00'),
                                                                     ('刘芳', '13800138006', '女', '成都市武侯区人民南路600号', '2024-06-18 13:20:00'),
                                                                     ('孙强', '13800138007', '男', '南京市鼓楼区中山北路700号', '2025-07-22 15:50:00');

-- ============================================
-- 2. 供应商表 (suppliers)
-- ============================================
CREATE TABLE suppliers (
                           id INT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
                           company_name VARCHAR(100) NOT NULL COMMENT '公司名称',
                           contact_person VARCHAR(50) NOT NULL COMMENT '联系人',
                           phone VARCHAR(20) NOT NULL COMMENT '联系电话',
                           email VARCHAR(100) COMMENT '邮箱',
                           address VARCHAR(200) COMMENT '公司地址',
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '合作开始时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 插入供应商数据（7行）
INSERT INTO suppliers (company_name, contact_person, phone, email, address, created_at) VALUES
                                                                                            ('山东烟台苹果基地', '马经理', '0535-8888001', 'ytapple@supplier.com', '山东省烟台市栖霞市', '2025-06-01 09:00:00'),
                                                                                            ('海南热带水果合作社', '林总', '0898-8888002', 'hainan@supplier.com', '海南省海口市琼山区', '2024-07-15 10:30:00'),
                                                                                            ('新疆哈密瓜种植园', '艾力江', '0991-8888003', 'xinjiang@supplier.com', '新疆维吾尔自治区吐鲁番市', '2023-08-20 14:00:00'),
                                                                                            ('广西百色芒果农场', '黄老板', '0776-8888004', 'guangxi@supplier.com', '广西壮族自治区百色市', '2025-09-10 11:20:00'),
                                                                                            ('陕西洛川苹果公司', '周主任', '0911-8888005', 'luochuan@supplier.com', '陕西省延安市洛川县', '2024-10-05 15:45:00'),
                                                                                            ('福建漳州柑橘基地', '吴经理', '0596-8888006', 'zhangzhou@supplier.com', '福建省漳州市龙海市', '2023-11-12 09:30:00'),
                                                                                            ('云南玉溪葡萄庄园', '杨总', '0877-8888007', 'yuxi@supplier.com', '云南省玉溪市红塔区', '2024-12-20 16:00:00');

-- ============================================
-- 3. 水果表 (fruits)
-- ============================================
CREATE TABLE fruits (
                        id INT PRIMARY KEY AUTO_INCREMENT COMMENT '水果ID',
                        name VARCHAR(50) NOT NULL COMMENT '水果名称',
                        category VARCHAR(30) NOT NULL COMMENT '分类',
                        price DECIMAL(10, 2) NOT NULL COMMENT '单价(元/斤)',
                        stock_quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量(斤)',
                        supplier_id INT COMMENT '供应商ID',
                        origin VARCHAR(100) COMMENT '产地',
                        shelf_life_days INT COMMENT '保质期(天)',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
                        FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
                        CONSTRAINT chk_price CHECK (price > 0),
                        CONSTRAINT chk_stock CHECK (stock_quantity >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水果表';

-- 插入水果数据（7行）
INSERT INTO fruits (name, category, price, stock_quantity, supplier_id, origin, shelf_life_days, created_at) VALUES
                                                                                                                 ('红富士苹果', '苹果类', 8.50, 500, 1, '山东烟台', 30, '2026-01-10 08:00:00'),
                                                                                                                 ('金煌芒果', '热带水果', 12.80, 300, 4, '广西百色', 15, '2026-01-12 09:30:00'),
                                                                                                                 ('巨峰葡萄', '葡萄类', 15.00, 200, 7, '云南玉溪', 10, '2026-01-15 10:00:00'),
                                                                                                                 ('脐橙', '柑橘类', 6.50, 400, 6, '福建漳州', 25, '2026-01-18 11:20:00'),
                                                                                                                 ('哈密瓜', '瓜类', 9.90, 150, 3, '新疆吐鲁番', 20, '2026-01-20 14:00:00'),
                                                                                                                 ('香蕉', '热带水果', 5.80, 600, 2, '海南海口', 12, '2026-01-22 08:30:00'),
                                                                                                                 ('水晶梨', '梨类', 7.20, 350, 5, '陕西洛川', 28, '2026-01-25 09:00:00');

-- ============================================
-- 4. 订单表 (orders)
-- ============================================
CREATE TABLE orders (
                        id INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
                        order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
                        customer_id INT NOT NULL COMMENT '顾客ID',
                        total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
                        status ENUM('待支付', '已支付', '配送中', '已完成', '已取消') NOT NULL DEFAULT '待支付' COMMENT '订单状态',
                        payment_method ENUM('微信支付', '支付宝', '银行卡', '现金') COMMENT '支付方式',
                        delivery_address VARCHAR(200) COMMENT '配送地址',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
                        CONSTRAINT chk_total CHECK (total_amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 插入订单数据（7行）
INSERT INTO orders (order_no, customer_id, total_amount, status, payment_method, delivery_address, created_at) VALUES
                                                                                                                   ('ORD20240125001', 1, 85.00, '已完成', '微信支付', '北京市朝阳区建国路100号', '2026-02-25 10:30:00'),
                                                                                                                   ('ORD20240126002', 2, 128.00, '已完成', '支付宝', '上海市浦东新区陆家嘴200号', '2026-02-26 14:20:00'),
                                                                                                                   ('ORD20240127003', 3, 150.00, '配送中', '微信支付', '广州市天河区体育西路300号', '2026-03-27 09:15:00'),
                                                                                                                   ('ORD20240128004', 4, 99.00, '已支付', '银行卡', '深圳市南山区科技园400号', '2026-04-28 16:45:00'),
                                                                                                                   ('ORD20240129005', 5, 65.00, '待支付', NULL, '杭州市西湖区文三路500号', '2026-03-29 11:30:00'),
                                                                                                                   ('ORD20240130006', 6, 172.00, '已完成', '支付宝', '成都市武侯区人民南路600号', '2026-03-30 13:20:00'),
                                                                                                                   ('ORD20240131007', 7, 58.00, '已取消', NULL, '南京市鼓楼区中山北路700号', '2026-03-31 15:50:00');

-- ============================================
-- 5. 订单明细表 (order_items)
-- ============================================
CREATE TABLE order_items (
                             id INT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
                             order_id INT NOT NULL COMMENT '订单ID',
                             fruit_id INT NOT NULL COMMENT '水果ID',
                             quantity DECIMAL(10, 2) NOT NULL COMMENT '购买数量(斤)',
                             unit_price DECIMAL(10, 2) NOT NULL COMMENT '购买时单价',
                             subtotal DECIMAL(10, 2) NOT NULL COMMENT '小计金额',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             FOREIGN KEY (fruit_id) REFERENCES fruits(id) ON DELETE RESTRICT,
                             CONSTRAINT chk_quantity CHECK (quantity > 0),
                             CONSTRAINT chk_unit_price CHECK (unit_price > 0),
                             CONSTRAINT chk_subtotal CHECK (subtotal >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 插入订单明细数据（7行）
INSERT INTO order_items (order_id, fruit_id, quantity, unit_price, subtotal, created_at) VALUES
                                                                                             (1, 1, 10.00, 8.50, 85.00, '2026-02-25 10:30:00'),
                                                                                             (2, 2, 10.00, 12.80, 128.00, '2026-02-26 14:20:00'),
                                                                                             (3, 3, 10.00, 15.00, 150.00, '2026-03-27 09:15:00'),
                                                                                             (4, 5, 10.00, 9.90, 99.00, '2026-04-28 16:45:00'),
                                                                                             (5, 4, 10.00, 6.50, 65.00, '2026-03-29 11:30:00'),
                                                                                             (6, 6, 20.00, 5.80, 116.00, '2026-03-30 13:20:00'),
                                                                                             (6, 7, 7.78, 7.20, 56.00, '2026-03-31 13:25:00');

-- ============================================
-- 查询验证
-- ============================================
-- 查看所有表结构
SHOW TABLES;

-- 查看各表记录数
SELECT 'customers' AS table_name, COUNT(*) AS record_count FROM customers
UNION ALL
SELECT 'suppliers', COUNT(*) FROM suppliers
UNION ALL
SELECT 'fruits', COUNT(*) FROM fruits
UNION ALL
SELECT 'orders', COUNT(*) FROM orders
UNION ALL
SELECT 'order_items', COUNT(*) FROM order_items;

-- 查看外键关系
SELECT
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = DATABASE()
  AND REFERENCED_TABLE_NAME IS NOT NULL;
