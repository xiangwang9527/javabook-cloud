package cn.javabook.chapter11.aio.aio3;

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
    public void start() throws IOException, InterruptedException {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        if (channel.isOpen()) {
            // socket接收缓冲区recbuf大小
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            // socket发送缓冲区recbuf大小
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            // 保持长连接状态
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            // 连接到服务端
            channel.connect(new InetSocketAddress(8080), null, new AioClientHandler(channel));
            // 阻塞主进程
            for(;;) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } else {
            throw new RuntimeException("Channel not opened!");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new AioClient().start();
    }
}
