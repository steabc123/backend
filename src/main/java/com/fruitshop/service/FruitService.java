package com.fruitshop.service;

import com.fruitshop.dao.FruitDao;
import com.fruitshop.entity.Fruit;

import java.util.Date;
import java.util.List;

/**
 * 水果业务逻辑层
 */
public class FruitService {
    private FruitDao fruitDao = new FruitDao();

    /**
     * 根据ID查询水果
     */
    public Fruit getFruitById(Integer id) {
        return fruitDao.findById(id);
    }

    /**
     * 查询所有水果
     */
    public List<Fruit> getAllFruits() {
        return fruitDao.findAll();
    }

    /**
     * 根据分类查询水果
     */
    public List<Fruit> getFruitsByCategory(String category) {
        return fruitDao.findByCategory(category);
    }

    /**
     * 添加水果
     */
    public boolean addFruit(Fruit fruit) {
        // 设置默认值
        if (fruit.getCreatedAt() == null) {
            fruit.setCreatedAt(new Date());
        }
        if (fruit.getStockQuantity() == null) {
            fruit.setStockQuantity(0);
        }
        
        int rows = fruitDao.insert(fruit);
        return rows > 0;
    }

    /**
     * 更新水果信息
     */
    public boolean updateFruit(Fruit fruit) {
        if (fruit.getId() == null) {
            throw new RuntimeException("水果ID不能为空");
        }
        
        int rows = fruitDao.update(fruit);
        return rows > 0;
    }

    /**
     * 删除水果
     */
    public boolean deleteFruit(Integer id) {
        if (id == null) {
            throw new RuntimeException("水果ID不能为空");
        }
        
        int rows = fruitDao.delete(id);
        return rows > 0;
    }

    /**
     * 更新库存
     */
    public boolean updateStock(Integer id, Integer quantity) {
        if (id == null || quantity == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (quantity < 0) {
            throw new RuntimeException("库存数量不能为负数");
        }
        
        int rows = fruitDao.updateStock(id, quantity);
        return rows > 0;
    }
}
