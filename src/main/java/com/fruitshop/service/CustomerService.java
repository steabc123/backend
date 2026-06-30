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
        
        // 检查手机号是否被其他顾客使用
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            Customer existing = customerDao.findByPhone(customer.getPhone());
            if (existing != null && !existing.getId().equals(customer.getId())) {
                throw new RuntimeException("该手机号已被其他顾客使用");
            }
        }
        
        int rows = customerDao.update(customer);
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
