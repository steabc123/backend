package com.fruitshop.service;

import com.fruitshop.dao.SupplierDao;
import com.fruitshop.entity.Supplier;

import java.util.Date;
import java.util.List;

/**
 * 供应商业务逻辑层
 */
public class SupplierService {
    private SupplierDao supplierDao = new SupplierDao();

    /**
     * 根据ID查询供应商
     */
    public Supplier getSupplierById(Integer id) {
        return supplierDao.findById(id);
    }

    /**
     * 查询所有供应商
     */
    public List<Supplier> getAllSuppliers() {
        return supplierDao.findAll();
    }

    /**
     * 添加供应商
     */
    public boolean addSupplier(Supplier supplier) {
        // 设置默认值
        if (supplier.getCreatedAt() == null) {
            supplier.setCreatedAt(new Date());
        }
        
        int rows = supplierDao.insert(supplier);
        return rows > 0;
    }

    /**
     * 更新供应商信息
     */
    public boolean updateSupplier(Supplier supplier) {
        if (supplier.getId() == null) {
            throw new RuntimeException("供应商ID不能为空");
        }
        
        int rows = supplierDao.update(supplier);
        return rows > 0;
    }

    /**
     * 删除供应商
     */
    public boolean deleteSupplier(Integer id) {
        if (id == null) {
            throw new RuntimeException("供应商ID不能为空");
        }
        
        int rows = supplierDao.delete(id);
        return rows > 0;
    }
}
