package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

public class ProductInventoryCacheRefreshRequest implements Request{
    private final Integer productId;
    
    private final ProductInventoryService productInventoryService;

    private Boolean isRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId, ProductInventoryService productInventoryService, Boolean isRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.isRefresh = isRefresh;
    }

    @Override
    public void process() {
        // 先查询数据库，然后将查询出来的数据塞入缓存中
        final ProductInventory productInventoryCache = productInventoryService.findProductInventory(productId);
        System.out.println("数据库查询成功");
        productInventoryService.setProductInventoryCache(productInventoryCache);
        System.out.println("更新" + productId + "缓存成功");
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public Boolean isForceRefresh() {
        return isRefresh;
    }
}
