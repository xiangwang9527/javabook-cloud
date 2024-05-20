package cn.javabook.chapter11.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * NIO中的Channel
 *
 */
public class TestPipe {
    public static void main(String[] args) throws IOException {
        // 打开管道
        Pipe pipe = Pipe.open();

        // 将Buffer数据写入到管道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("ByteBuffer".getBytes());
        // 切换到写模式
        buffer.flip();
        sinkChannel.write(buffer);

        // 从管道读取数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buffer = ByteBuffer.allocate(32);
        sourceChannel.read(buffer);
        System.out.println(new String(buffer.array()));

        // 关闭管道
        sinkChannel.close();
        sourceChannel.close();
    }
}
