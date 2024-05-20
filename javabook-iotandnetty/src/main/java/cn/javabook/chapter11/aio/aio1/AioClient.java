package cn.javabook.chapter11.aio.aio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * AIO客户端
 *
 */
public class AioClient {
    private AsynchronousSocketChannel channel;

    public AioClient(String host, int port) {
        try {
            // 初始化
            channel = AsynchronousSocketChannel.open();
            if (channel.isOpen()) {
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 1024);
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 1024);
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            }
            channel.connect(new InetSocketAddress(8080), null, new AioClientHandler(channel));
            // 阻塞程序，防止自动退出
            while(true) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 启动客户端
        new AioClient("localhost", 8080);
    }
}
