# 水果店管理系统 - 后端API

##  项目结构

```
src/main/
├── java/com/fruitshop/
│   ├── entity/          # 实体类
│   │   ├── Customer.java    # 顾客实体
│   │   ├── Supplier.java    # 供应商实体
│   │   ├── Fruit.java       # 水果实体
│   │   ├── Order.java       # 订单实体
│   │   └── OrderItem.java   # 订单明细实体
│   ├── dao/             # 数据访问层
│   │   ├── CustomerDao.java
│   │   ├── SupplierDao.java
│   │   ├── FruitDao.java
│   │   ├── OrderDao.java
│   │   └── OrderItemDao.java
│   ├── service/         # 业务逻辑层
│   │   ├── CustomerService.java
│   │   ├── SupplierService.java
│   │   ├── FruitService.java
│   │   └── OrderService.java
│   ├── servlet/         # 控制器层（Servlet）
│   │   ├── CustomerServlet.java
│   │   ├── SupplierServlet.java
│   │   ├── FruitServlet.java
│   │   └── OrderServlet.java
│   └── util/            # 工具类
│       ├── DBUtil.java      # 数据库连接工具
│       ├── JsonUtil.java    # JSON处理工具
│       └── ResponseUtil.java # 响应工具
└── resources/
    └── c3p0-config.xml  # C3P0连接池配置
```

## 🔧 技术栈

- **Java**: 22
- **构建工具**: Maven
- **Web框架**: Servlet 4.0 + @WebServlet注解
- **数据库**: MySQL 8.0
- **连接池**: C3P0 0.9.5.5
- **JSON处理**: Jackson 2.15.2
- **编码**: UTF-8

##  前置要求

1. JDK 22+
2. Maven 3.6+
3. MySQL 8.0+
4. Tomcat 9.0+ 或其他Servlet容器

## 🚀 快速开始

### 1. 创建数据库

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE fruitshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入SQL脚本
USE fruitshop;
source E:/sjkkcsj/backend/fruit_store.sql;
```

### 2. 修改数据库配置

编辑 `src/main/resources/c3p0-config.xml`，修改数据库用户名和密码：

```xml
<property name="user">root</property>
<property name="password">你的密码</property>
```

### 3. 编译项目

```bash
mvn clean package
```

### 4. 部署到Tomcat

将生成的 `target/backend.war` 部署到Tomcat的 `webapps` 目录，或使用IDE直接运行。

##  API接口文档

### 顾客管理 `/api/customer/*`

#### 查询所有顾客
```
GET /api/customer/list
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据ID查询顾客
```
GET /api/customer/{id}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 根据手机号查询顾客
```
GET /api/customer/phone/{phone}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 添加顾客
```
POST /api/customer
Body: { "name": "张三", "phone": "13800138001", "gender": "男", "address": "..." }
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 更新顾客信息
```
PUT /api/customer/{id}
Body: { "name": "李四", "phone": "13800138002", ... }
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 删除顾客
```
DELETE /api/customer/{id}
Response: { "code": 200, "message": "success", "data": "删除成功" }
```

---

### 水果管理 `/api/fruit/*`

#### 查询所有水果
```
GET /api/fruit/list
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据分类查询水果
```
GET /api/fruit/category/{category}
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据ID查询水果
```
GET /api/fruit/{id}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 添加水果
```
POST /api/fruit
Body: { 
  "name": "红富士苹果", 
  "category": "苹果类", 
  "price": 8.50, 
  "stockQuantity": 500,
  "supplierId": 1,
  "origin": "山东烟台",
  "shelfLifeDays": 30
}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 更新水果信息
```
PUT /api/fruit/{id}
Body: { "name": "...", "price": ..., "stockQuantity": ... }
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 删除水果
```
DELETE /api/fruit/{id}
Response: { "code": 200, "message": "success", "data": "删除成功" }
```

---

### 订单管理 `/api/order/*`

#### 查询所有订单
```
GET /api/order/list
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据顾客ID查询订单
```
GET /api/order/customer/{customerId}
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据状态查询订单
```
GET /api/order/status/{status}
# status: 待支付/已支付/配送中/已完成/已取消
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 查询订单明细
```
GET /api/order/{orderId}/items
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 创建订单
```
POST /api/order
Body: {
  "order": {
    "orderNo": "ORD20260101001",
    "customerId": 1,
    "paymentMethod": "微信支付",
    "deliveryAddress": "北京市朝阳区..."
  },
  "items": [
    {
      "fruitId": 1,
      "quantity": 5.00
    },
    {
      "fruitId": 2,
      "quantity": 3.00
    }
  ]
}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 更新订单状态
```
PUT /api/order/{orderId}
Body: { "status": "已支付" }
Response: { "code": 200, "message": "success", "data": "订单状态更新成功" }
```

#### 删除订单
```
DELETE /api/order/{orderId}
Response: { "code": 200, "message": "success", "data": "删除成功" }
```

---

### 供应商管理 `/api/supplier/*`

#### 查询所有供应商
```
GET /api/supplier/list
Response: { "code": 200, "message": "success", "data": [...] }
```

#### 根据ID查询供应商
```
GET /api/supplier/{id}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 添加供应商
```
POST /api/supplier
Body: {
  "companyName": "山东烟台苹果基地",
  "contactPerson": "马经理",
  "phone": "0535-8888001",
  "email": "ytapple@supplier.com",
  "address": "山东省烟台市栖霞市"
}
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 更新供应商信息
```
PUT /api/supplier/{id}
Body: { "companyName": "...", "phone": "..." }
Response: { "code": 200, "message": "success", "data": {...} }
```

#### 删除供应商
```
DELETE /api/supplier/{id}
Response: { "code": 200, "message": "success", "data": "删除成功" }
```

---

## 📊 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {...}
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "参数格式错误"
}
```

或

```json
{
  "code": 500,
  "message": "服务器内部错误: ..."
}
```

## ️ 注意事项

1. **数据库端口**: 默认使用3307端口，如需修改请更新 `c3p0-config.xml`
2. **字符编码**: 所有请求和响应均使用UTF-8编码
3. **外键约束**: 删除顾客前需确保没有关联订单
4. **库存管理**: 创建订单时会自动扣减库存，删除订单不会恢复库存
5. **订单状态**: 支持的状态有：待支付、已支付、配送中、已完成、已取消

## 🧪 测试示例

### 使用curl测试

```bash
# 查询所有水果
curl http://localhost:8080/backend/api/fruit/list

# 添加顾客
curl -X POST http://localhost:8080/backend/api/customer \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试用户",
    "phone": "13900139000",
    "gender": "女",
    "address": "测试地址"
  }'

# 创建订单
curl -X POST http://localhost:8080/backend/api/order \
  -H "Content-Type: application/json" \
  -d '{
    "order": {
      "orderNo": "ORD20260101001",
      "customerId": 1,
      "paymentMethod": "微信支付",
      "deliveryAddress": "测试配送地址"
    },
    "items": [
      {
        "fruitId": 1,
        "quantity": 5.00
      }
    ]
  }'
```

##  开发说明

- 所有Servlet使用 `@WebServlet` 注解配置URL映射
- 使用C3P0连接池管理数据库连接
- 采用三层架构：DAO → Service → Servlet
- 统一使用JSON格式进行数据交互
- 异常通过RuntimeException抛出，在Servlet层统一捕获处理

## 🛠️ 常见问题

### Q: 启动后无法连接数据库？
A: 检查以下几点：
1. MySQL服务是否启动
2. 数据库端口是否正确（默认3307）
3. 用户名密码是否正确
4. 数据库是否存在且已导入SQL脚本

### Q: 中文显示乱码？
A: 确保：
1. 数据库字符集为utf8mb4
2. c3p0-config.xml中配置了characterEncoding=utf8
3. Servlet中设置了request.setCharacterEncoding("UTF-8")

### Q: 404错误？
A: 检查：
1. URL路径是否正确
2. Tomcat是否正确部署
3. web.xml是否需要配置（使用@WebServlet可省略）

## 📄 License

MIT License
