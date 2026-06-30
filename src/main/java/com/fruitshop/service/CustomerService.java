package com.fruitshop.service;

import com.fruitshop.dao.CustomerDao;
import com.fruitshop.entity.Customer;

import java.util.Date;
import java.util.List;

/**
 * 顾客业务逻辑层
 */
public class CustomerService {
    private CustomerDao customerDao = new CustomerDao();

    /**
     * 顾客登录
     */
    public Customer login(String name, String password) {
        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        Customer customer = customerDao.findByNameAndPassword(name, password);
        return customer;
    }

    /**
     * 顾客注册
     */
    public boolean register(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        // 检查用户名是否已存在
        Customer existing = customerDao.findByName(customer.getName());
        if (existing != null) {
            throw new RuntimeException("该用户名已被注册");
        }
        // 检查手机号是否已存在
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            Customer existingByPhone = customerDao.findByPhone(customer.getPhone());
            if (existingByPhone != null) {
                throw new RuntimeException("该手机号已被注册");
            }
        }
        // 设置默认值
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(new Date());
        }
        if (customer.getRole() == null || customer.getRole().isEmpty()) {
            customer.setRole("customer");
        }
        if (customer.getGender() == null || customer.getGender().isEmpty()) {
            customer.setGender("男");
        }
        int rows = customerDao.insert(customer);
        return rows > 0;
    }

    /**
     * 根据ID查询顾客
     */
    public Customer getCustomerById(Integer id) {
        return customerDao.findById(id);
    }

    /**
     * 根据手机号查询顾客
     */
    public Customer getCustomerByPhone(String phone) {
        return customerDao.findByPhone(phone);
    }

    /**
     * 查询所有顾客
     */
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    /**
     * 添加顾客
     */
    public boolean addCustomer(Customer customer) {
        // 检查手机号是否已存在
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            Customer existing = customerDao.findByPhone(customer.getPhone());
            if (existing != null) {
                throw new RuntimeException("该手机号已被注册");
            }
        }
        
        // 设置默认值
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(new Date());
        }
        if (customer.getGender() == null || customer.getGender().isEmpty()) {
            customer.setGender("男");
        }
        
        int rows = customerDao.insert(customer);
        return rows > 0;
    }

    /**
     * 更新顾客信息
     */
    public boolean updateCustomer(Customer customer) {
        if (customer.getId() == null) {
            throw new RuntimeException("顾客ID不能为空");
        }
        
        // 获取原有顾客信息
        Customer existing = customerDao.findById(customer.getId());
        if (existing == null) {
            throw new RuntimeException("顾客不存在");
        }
        
        System.out.println("原有顾客信息: id=" + existing.getId() + ", name=" + existing.getName() + ", phone=" + existing.getPhone());
        System.out.println("新顾客信息: id=" + customer.getId() + ", name=" + customer.getName() + ", phone=" + customer.getPhone());
        
        // 合并数据：如果新值为null或空，则使用原有值
        String newName = customer.getName();
        if (newName == null || newName.trim().isEmpty()) {
            customer.setName(existing.getName());
            System.out.println("姓名为空，保持原姓名: " + existing.getName());
        } else {
            System.out.println("更新姓名为: " + newName);
        }
        
        String newPhone = customer.getPhone();
        if (newPhone == null || newPhone.trim().isEmpty()) {
            customer.setPhone(existing.getPhone());
            System.out.println("手机号为空，保持原手机号: " + existing.getPhone());
        } else {
            // 只有当手机号发生变化时才检查唯一性
            if (!newPhone.equals(existing.getPhone())) {
                Customer existingByPhone = customerDao.findByPhone(newPhone);
                if (existingByPhone != null) {
                    throw new RuntimeException("该手机号已被其他顾客使用");
                }
                System.out.println("手机号已变更，新手机号可用: " + newPhone);
            } else {
                System.out.println("手机号未变更: " + newPhone);
            }
        }
        
        String newGender = customer.getGender();
        if (newGender == null || newGender.trim().isEmpty()) {
            customer.setGender(existing.getGender());
            System.out.println("性别为空，保持原性别: " + existing.getGender());
        } else {
            System.out.println("更新性别为: " + newGender);
        }
        
        String newAddress = customer.getAddress();
        if (newAddress == null || newAddress.trim().isEmpty()) {
            customer.setAddress(existing.getAddress());
            System.out.println("地址为空，保持原地址: " + existing.getAddress());
        } else {
            System.out.println("更新地址为: " + newAddress);
        }
        
        // 处理密码
        String newPassword = customer.getPassword();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            customer.setPassword(existing.getPassword());
            System.out.println("密码为空，保持原密码");
        } else {
            System.out.println("更新密码为新值");
        }
        
        System.out.println("准备执行数据库更新...");
        int rows = customerDao.update(customer);
        System.out.println("数据库更新返回行数: " + rows);
        return rows > 0;
    }

    /**
     * 删除顾客
     */
    public boolean deleteCustomer(Integer id) {
        if (id == null) {
            throw new RuntimeException("顾客ID不能为空");
        }
        
        int rows = customerDao.delete(id);
        return rows > 0;
    }
}
