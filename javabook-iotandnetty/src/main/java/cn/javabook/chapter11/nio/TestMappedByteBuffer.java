package cn.javabook.chapter11.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中的MappedByteBuffer
 *
 */
public class TestMappedByteBuffer {
    public static void main(String[] args) {
        useFileChannel();
        useMappedByteBuffer();
    }

    // ByteBuffer读取大文件
    public static void useFileChannel() {
        try{
            FileInputStream fis = new FileInputStream("/testfile1");
            FileChannel channel = fis.getChannel();
            long start = System.currentTimeMillis();
            ByteBuffer buff = ByteBuffer.allocate((int) channel.size());
            buff.clear();
            channel.read(buff);
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            fis.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MappedByteBuffer读取大文件
    public static void useMappedByteBuffer() {
        try{
            FileInputStream fis = new FileInputStream("/testfile1");
            FileChannel channel = fis.getChannel();
            long start = System.currentTimeMillis();
            MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            fis.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
