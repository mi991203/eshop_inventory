package com.roncoo.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RequestQueue {
    private final List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    private final Map<Integer, Boolean> flagMap = new ConcurrentHashMap<>(16);

    /**
     * 静态内部类的方式保证单例
     */
    private static class Singleton {
        private static RequestQueue requestQueue;

        static {
            requestQueue = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return requestQueue;
        }
    }

    /**
     * 根据JVM机制保证多线程调用中单例的唯一实例
     *
     * @return 请求队列
     */
    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 向队列集合中添加队列
     *
     * @param queue 待添加的队列
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        queues.add(queue);
    }

    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize() {
        return queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }
}