package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.inventory.dao.RedisDAO;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("productInventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Resource
    private RedisDAO redisDAO;

    @Resource
    private ProductInventoryMapper productInventoryMapper;


    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.delete(key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        System.out.println("向redis中塞入值");
        redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));
        System.out.println("向redis中塞入值success");
    }

    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        String key = "product:inventory:" + productId;
        final String result = redisDAO.get(key);
        try {
            return new ProductInventory(productId, Long.parseLong(result));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
