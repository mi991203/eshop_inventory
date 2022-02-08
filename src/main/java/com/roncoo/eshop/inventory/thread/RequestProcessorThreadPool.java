package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestProcessorThreadPool {
    /**
     * 固定线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public RequestProcessorThreadPool() {
        final RequestQueue queues = RequestQueue.getInstance();
        // 开启10个任务
        for (int i = 0; i < 10; i++) {
            // 每个阻塞队列中容量给100
            final ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            queues.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    private static class Singleton {
        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }
    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init() {
        getInstance();
    }
}
