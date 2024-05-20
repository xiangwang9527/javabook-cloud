package cn.javabook.chapter11.aio.aio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;

public class AioServer {
    private AsynchronousServerSocketChannel serverChannel;

    static class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
        private final AsynchronousServerSocketChannel serverChannel;
        private final CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        public ServerCompletionHandler(AsynchronousServerSocketChannel serverChannel) {
            this.serverChannel = serverChannel;
        }

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
            serverChannel.accept(null, this);

            try {
                // 回写
                String request = input.readLine();
                result.write(ByteBuffer.wrap(request.getBytes()));

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (result.read(buffer).get() != -1) {
                    buffer.flip();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    request = charBuffer.toString().trim();
                    System.out.println("from client: " + request);
//                    ByteBuffer outBuffer = ByteBuffer.wrap("request received".getBytes());
//                    // 阻塞
//                    result.write(outBuffer).get();
                    if (buffer.hasRemaining()) {
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }

                    // 回写
                    request = input.readLine();
                    result.write(ByteBuffer.wrap(request.getBytes()));
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            serverChannel.accept(null, this);
        }
    }

    public void init() throws IOException {
        this.serverChannel = AsynchronousServerSocketChannel.open();
        if (serverChannel.isOpen()) {
            serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverChannel.bind(new InetSocketAddress(8080));
        } else {
            throw new RuntimeException("channel not opened!");
        }
    }

    public void start() throws InterruptedException {
        System.out.println("server started");
        this.serverChannel.accept(null, new ServerCompletionHandler(serverChannel));
        while (true) {
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AioServer server = new AioServer();
        server.init();
        server.start();
    }
}
