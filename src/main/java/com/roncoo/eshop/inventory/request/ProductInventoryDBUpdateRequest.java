package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

public class ProductInventoryDBUpdateRequest implements Request{
    private final ProductInventory productInventory;

    private final ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        // 先删除缓存，在修改数据库
        productInventoryService.removeProductInventoryCache(productInventory);
        System.out.println("删除缓存成功");
        // 手动模拟删除缓存后数据库处理10s
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productInventoryService.updateProductInventory(productInventory);
        System.out.println("更新" + productInventory.getProductId() + "数据库中数据完成");
    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public Boolean isForceRefresh() {
        return false;
    }
}
