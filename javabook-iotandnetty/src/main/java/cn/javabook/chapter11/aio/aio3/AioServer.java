package cn.javabook.chapter11.aio.aio3;

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
    public void start() throws InterruptedException, IOException {
        AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open();
        if (channel.isOpen()) {
            // socket接受缓冲区recbuf大小
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            // 端口重用，防止进程意外终止，未释放端口，重启时失败
            // 因为直接杀进程，没有显式关闭套接字来释放端口，会等待一段时间后才可以重新use这个关口
            // 解决办法就是用SO_REUSEADDR
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.bind(new InetSocketAddress(8080));
        } else {
            throw new RuntimeException("channel not opened!");
        }
        // 处理client连接
        channel.accept(null, new AioServerHandler(channel));
        System.out.println("server started");
        // 阻塞主进程
        for(;;) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AioServer server = new AioServer();
        server.start();
    }
}
