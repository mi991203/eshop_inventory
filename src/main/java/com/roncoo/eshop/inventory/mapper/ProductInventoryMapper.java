package com.roncoo.eshop.inventory.mapper;

import com.roncoo.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

public interface ProductInventoryMapper {
    /**
     * 更新库存数量
     * @param productInventory 商品库存信息
     */
    void updateProductInventory(@Param("productInventory") ProductInventory productInventory);

    /**
     * 查询商品库存
     * @param productId 商品ID
     * @return 商品库存信息
     */
    ProductInventory findProductInventory(@Param("productId") Integer productId);



}
