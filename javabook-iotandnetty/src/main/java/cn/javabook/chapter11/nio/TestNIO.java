package cn.javabook.chapter11.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中的Channel和Buffer
 *
 */
public class TestNIO {
    public static void main(String[] args) throws IOException {
        // 传统I/O
        long start = System.currentTimeMillis();
        FileInputStream fis1 = new FileInputStream("/testfile1");
        FileOutputStream fos1 = new FileOutputStream("/testfile2");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis1), 1024);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                fos1.write(line.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis1.close();
            fos1.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("传统IO耗时：" + (end - start) + " 毫秒");

        // 改进的I/O
        start = System.currentTimeMillis();
        FileInputStream fis2 = new FileInputStream("/testfile1");
        FileOutputStream fos2 = new FileOutputStream("/testfile3");
        try {
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis2.read(b)) != -1) {
                fos2.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis2.close();
            fos2.close();
        }
        end = System.currentTimeMillis();
        System.out.println("改进的IO耗时：" + (end - start) + " 毫秒");

        // NIO
        start = System.currentTimeMillis();
        FileChannel fis3 = new FileInputStream("/testfile1").getChannel();
        FileChannel fos3 = new FileOutputStream("/testfile4").getChannel();
        try {
            ByteBuffer bytedata = ByteBuffer.allocate(1024);
            while (fis3.read(bytedata) != -1) {
                // 读写交叉进行
                bytedata.flip();
                fos3.write(bytedata);
                bytedata.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis3.close();
            fos3.close();
        }
        end = System.currentTimeMillis();
        System.out.println("NIO耗时：" + (end - start) + " 毫秒");
//
//        // Scattering reads
//        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
//        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
//        ByteBuffer[] bufferArray1 = { buffer1, buffer2 };
//        FileChannel channel1 = new FileInputStream("/Users/bear/home/work/testfile1").getChannel();
//        channel1.read(bufferArray1);
//
//        // Gathering writes
//        ByteBuffer buffer3 = ByteBuffer.allocate(1024);
//        ByteBuffer buffer4 = ByteBuffer.allocate(1024);
//        ByteBuffer[] bufferArray2 = { buffer1, buffer2 };
//        FileChannel channel2 = new FileInputStream("/Users/bear/home/work/testfile1").getChannel();
//        channel2.write(bufferArray2);
    }
}
