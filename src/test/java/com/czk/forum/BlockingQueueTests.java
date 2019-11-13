package com.czk.forum;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}
class Producer implements Runnable {
    // 交给阻塞队列
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                // 循环 100 次, 缓存在队列中
                Thread.sleep(20);
                queue.put(i);
                // 打印出当前谁在生产
                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        // 消费者不停的消费
        try {
            while (true) {
                Thread.sleep((long) (Math.random() * 1000));
                int i = queue.take();
                System.out.println(Thread.currentThread().getName() + "消费:" + i + " size=" + queue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}