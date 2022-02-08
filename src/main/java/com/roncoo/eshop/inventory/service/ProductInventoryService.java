package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.ProductInventory;

/**
 * 商品库存相关service接口
 */
public interface ProductInventoryService {
    /**
     * 更新商品库存
     * @param productInventory 商品库存信息
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商品ID删除对应缓存中的数据
     * @param productInventory 商品库存信息
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品ID查询库存缓存
     * @param productId 商品ID
     * @return 商品详情相关信息
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 将商品库存信息添加到缓存
     * @param productInventory 商品库存信息
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 获取缓存中商品库存信息
     * @param productId 商品ID
     * @return 商品库存信息
     */
    ProductInventory getProductInventoryCache(Integer productId);

}
