package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

public class RequestProcessorThread implements Callable<Boolean> {
    /**
     * 线程监控自己的缓存队列
     */
    private ArrayBlockingQueue<Request> arrayBlockingQueue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    @Override
    public Boolean call() {
        try {
            while (true) {
                // 对无用的更新缓存请求进行过滤
                final Request request = arrayBlockingQueue.take();
                final Boolean forceRefresh = request.isForceRefresh();
                if (!forceRefresh) {
                    // 如果不是强制刷新，那么进行无效“更新Cache请求”过滤
                    final Map<Integer, Boolean> flagMap = RequestQueue.getInstance().getFlagMap();
                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        flagMap.put(request.getProductId(), true);
                    } else if (request instanceof ProductInventoryCacheRefreshRequest) {
                        // 第一次缓存更新请求，那么refreshFlag赋值为false
                        final Boolean refreshFlag = flagMap.putIfAbsent(request.getProductId(), false);
                        if (refreshFlag != null && refreshFlag) {
                            // 是更新数据库后的更新缓存请求
                            flagMap.put(request.getProductId(), false);
                        }
                        if (refreshFlag != null && !refreshFlag) {
                            // 这一层判断是为了解决，当mysql数据库数据更改的过程中，又有将数据库中的数据更新到缓存的请求，这种情况下，在更新数据库后覆盖更新到缓存的请求全部pass掉
                            continue;
                        }
                    }
                }
                System.out.println("===========日志===========: 工作线程处理请求，商品id=" + request.getProductId());
                request.process();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
