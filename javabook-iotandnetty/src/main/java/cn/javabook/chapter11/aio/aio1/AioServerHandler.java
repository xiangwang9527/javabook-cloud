package cn.javabook.chapter11.aio.aio1;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * AIO服务端CompletionHandler
 *
 */
public class AioServerHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private AsynchronousServerSocketChannel serverChannel;

    public AioServerHandler(AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel channel, Void attachment) {
        // 处理下一次的client连接，类似链式调用
        serverChannel.accept(attachment, this);
        // 执行业务逻辑
        read(channel);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        serverChannel.accept(attachment, this);
    }

    /**
     * 读取client发送的消息打印到控制台
     * AIO中OS已经帮助我们完成了read的IO操作，所以我们直接拿到了读取的结果
     *
     */
    private void read(AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 从client读取数据
        // 在调用channel.read()之前操作系统已经完成了I/O操作
        // 只需要用一个缓冲区来存放读取的内容即可
        channel.read(
                buffer,   // 用于数据中转缓冲区
                buffer,   // 用于存储client发送的数据的缓冲区
                new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        // 切换模式
                        attachment.flip();
                        // 读取client发送的数据
                        System.out.println("from client: " + new String(attachment.array(), StandardCharsets.UTF_8).trim());
                        // 向client写入数据
                        write(channel);
                    }
                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                    }
                });
    }

    private void write(AsynchronousSocketChannel channel) {
        // 向client发送数据，clientChannel.write()是一个异步调用，该方法执行后会通知
        // OS执行写操作，并立即返回
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.write(buffer);
    }
}
