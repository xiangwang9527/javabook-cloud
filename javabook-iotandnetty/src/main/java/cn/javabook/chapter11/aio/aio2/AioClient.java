package cn.javabook.chapter11.aio.aio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AioClient {
    static class ClientCompletionHandler implements CompletionHandler<Void, Void> {
        private final AsynchronousSocketChannel channel;
        private final CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        public ClientCompletionHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Void result, Void attachment) {
            System.out.println("send message to server: ");
            try {
                // 回写
                String request = input.readLine();
                channel.write(ByteBuffer.wrap(request.getBytes()));

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (channel.read(buffer).get() != -1) {
                    buffer.flip();
                    CharBuffer charBufferr = decoder.decode(buffer);
                    System.out.println("from server: " + charBufferr.toString());
                    if (buffer.hasRemaining()) {
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }
                    // 回写
                    request = input.readLine();
                    channel.write(ByteBuffer.wrap(request.getBytes()));
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            throw new RuntimeException("channel not opened!");
        }
    }

    public void start() throws IOException, InterruptedException {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        if (channel.isOpen()) {
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress(8080), null, new ClientCompletionHandler(channel));
            while (true) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } else {
            throw new RuntimeException("Channel not opened!");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AioClient client = new AioClient();
        client.start();
    }
}
