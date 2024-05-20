package cn.javabook.chapter11.aio.aio1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * AIO客户端CompletionHandler
 *
 */
public class AioClientHandler implements CompletionHandler<Void, AioClient> {
    private AsynchronousSocketChannel channel;

    public AioClientHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Void result, AioClient attachment) {
        try {
            read(channel);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AioClient attachment) {
        exc.printStackTrace();
    }

    private void read(AsynchronousSocketChannel channel) throws ExecutionException, InterruptedException {
//        System.out.println("enter your message to server : ");
//        Scanner s = new Scanner(System.in);
//        String line = s.nextLine();
//        write(line);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        while (-1 != channel.read(buffer).get()) {
//            buffer.flip();
//            System.out.println("from server: " + new String(buffer.array(), StandardCharsets.UTF_8));
//        }
        System.out.println("enter your message to server : ");
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        write(line);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(channel.read(buffer).get() != -1){
                buffer.flip();
                System.out.println("from server: " + new String(buffer.array(), StandardCharsets.UTF_8));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String line) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(line.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        channel.write(buffer);
    }
}
