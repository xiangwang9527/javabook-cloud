package cn.javabook.chapter11.aio.aio3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;

/**
 * AIO服务端CompletionHandler
 *
 */
public class AioServerHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel serverChannel;
    private final CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public AioServerHandler(AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        // 处理下一次的client连接
        serverChannel.accept(null, this);
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        // 处理下一次的client连接，类似链式调用
        serverChannel.accept(null, this);

        try {
            // 将输入内容写到buffer
            String line = input.readLine();
            result.write(ByteBuffer.wrap(line.getBytes()));
            // 在操作系统中的Java本地方法native已经把数据写到了buffer中
            // 这里只需要一个缓冲区能接收就行了
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (result.read(buffer).get() != -1) {
                buffer.flip();
                System.out.println("from client: " + decoder.decode(buffer).toString());
                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    buffer.clear();
                }
                // 将输入内容写到buffer
                line = input.readLine();
                result.write(ByteBuffer.wrap(line.getBytes()));
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
    }
}
