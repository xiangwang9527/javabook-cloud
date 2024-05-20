package cn.javabook.chapter11.aio.aio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * AIO服务端
 *
 */
public class AioServer {
    private AsynchronousServerSocketChannel channel;

    public AioServer(int port) {
        System.out.println("server starting at port " + port);
        try {
            // 初始化
            channel = AsynchronousServerSocketChannel.open();
            if (channel.isOpen()) {
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 1024);
                channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                channel.bind(new InetSocketAddress(port));
            }
            // 监听客户端连接，需要在处理逻辑中再次调用accept用于开启下一次的监听
            // 类似于链式调用
            channel.accept(null, new AioServerHandler(channel));
            // 阻塞程序，防止自动退出
            while(true) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 启动服务端
        new AioServer(8080);
    }
}
