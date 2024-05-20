package cn.javabook.chapter11.aio.aio3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;

/**
 * AIO客户端CompletionHandler
 *
 */
public class AioClientHandler implements CompletionHandler<Void, AioClient> {
    private final AsynchronousSocketChannel channel;
    private final CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public AioClientHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void failed(Throwable exc, AioClient attachment) {
        throw new RuntimeException("channel not opened!");
    }

    @Override
    public void completed(Void result, AioClient attachment) {
        System.out.println("send message to server: ");
        try {
            // 将输入内容写到buffer
            String line = input.readLine();
            channel.write(ByteBuffer.wrap(line.getBytes()));
            // 在操作系统中的Java本地方法native已经把数据写到了buffer中
            // 这里只需要一个缓冲区能接收就行了
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer).get() != -1) {
                buffer.flip();
                System.out.println("from server: " + decoder.decode(buffer).toString());
                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    buffer.clear();
                }
                // 将输入内容写到buffer
                line = input.readLine();
                channel.write(ByteBuffer.wrap(line.getBytes()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
