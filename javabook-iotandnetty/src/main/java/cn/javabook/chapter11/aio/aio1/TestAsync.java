package cn.javabook.chapter11.aio.aio1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 测试异步执行方式
 *
 */
public class TestAsync {
    public static void main(String[] args) {
        // 执行一个异步方法
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
            System.out.println("run end ...");
            return System.currentTimeMillis();
        });

        // main主线程会先执行到这一步
        long time = 0;
        try {
            time = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("time = " + time);
    }
}
