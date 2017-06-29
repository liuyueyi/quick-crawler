package com.quick.hui.crawler.core.test.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yihui on 2017/6/29.
 */
public class LockTest {

    private static List<Lock> lock = new ArrayList<>();
    static {
        lock.add(new ReentrantLock());
        lock.add(new ReentrantLock());
    }

    private static int i = 0;

    public static class MyThread extends Thread {
        private int index;

        public MyThread(String str, int index) {
            super(str);
            this.index = index % 2;
        }


        public void run() {
            lock.get(index).lock();
            System.out.println(Thread.currentThread().getName() + " 上锁");
            try {
                i++;
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + " 释放锁");
                lock.get(index).unlock();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for(int t  =0 ;t <100; t++) {
            MyThread thread1 = new MyThread("test1+" + t, t);
            MyThread thread2 = new MyThread("test2+" + t, t);
            MyThread thread3 = new MyThread("test3+" + t, t);

            thread1.start();
            thread2.start();
            thread3.start();

        }
    }
}
