package cn.javabook.chapter11.nio;

import java.nio.ByteBuffer;

/**
 * NIO中的Buffer
 *
 */
public class TestBuffer {
    public static void main(String[] args) {
        // 分配JVM间接缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(32);
        System.out.println("buffer初始状态: " + buffer);
        // 将position设回8
        buffer.position(8);
        System.out.println("buffer设置后状态: " + buffer);

        System.out.println("测试reset ======================>>>");
        // clear()方法，position将被设回0，limit被设置成capacity的值
        buffer.clear();
        System.out.println("buffer clear后状态: " + buffer);
        // 设置这个缓冲区的位置
        buffer.position(5);
        // 将此缓冲区的标记设置5
        // 如果没有buffer.mark();这句话会报错
        buffer.mark();
        buffer.position(10);
        System.out.println("reset前状态: " + buffer);
        // 将此缓冲区的位置重置为先前标记的位置（buffer.position(5)）
        buffer.reset();
        System.out.println("reset后状态: " + buffer);

        System.out.println("测试get ======================>>>");
        buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'x').put((byte) 'i').put((byte) 'a').put((byte) 'n').put((byte) 'g');
        System.out.println("flip前状态: " + buffer);
        // 转换为读模式
        buffer.flip();
        System.out.println("get前状态: " + buffer);
        System.out.println((char) buffer.get());
        System.out.println("get后状态: " + buffer);

        System.out.println("测试put ======================>>>");
        ByteBuffer pb = ByteBuffer.allocate(32);
        System.out.println("put前状态: " + pb + ", put前数据: " + new String(pb.array()));
        System.out.println("put后状态: " + pb.put((byte) 'w') + ", put后数据: " + new String(pb.array()));
        System.out.println(pb.put(3, (byte) '3'));
        // put(3, (byte) '3')并不改变position的位置，但put((byte) '3')会
        System.out.println("put(3, '3')后状态: " + pb + ", 数据: " + new String(pb.array()));
        // 这里的buffer是 xiang[pos=1 lim=5 cap=32]
        System.out.println("buffer叠加前状态: " + buffer + ", buffer叠加前数据: " + new String(buffer.array()));
        // buffer.put(pb);会抛异常BufferOverflowException
        pb.put(buffer);
        // 叠加后数据是wiang                           ，因为buffer的position=1
        System.out.println("put(buffer)后bb状态: " + pb + ", buffer叠加后数据: " + new String(pb.array()));

        // 重新读取buffer中所有数据
        System.out.println("测试rewind ======================>>>");
        buffer.clear();
        buffer.position(10);
        System.out.println("buffer当前状态: " + buffer);
        // 返回此缓冲区的限制
        buffer.limit(15);
        System.out.println("limit后状态: " + buffer);
        // 把position设为0，mark设为-1，不改变limit的值
        buffer.rewind();
        System.out.println("rewind后状态: " + buffer);

        // 将所有未读的数据拷贝到Buffer起始处，然后将position设到最后一个未读元素正后面
        System.out.println("测试compact ======================>>>");
        buffer.clear();
        buffer.put("abcd".getBytes());
        System.out.println("compact前状态: " + buffer);
        System.out.println(new String(buffer.array()));
        // limit=position;position=0;mark=-1; 翻转，也就是让flip之后的position到limit这块区域变成之前的0到position这块
        // 翻转就是将一个处于存数据状态的缓冲区变为一个处于准备取数据的状态，或者相反
        buffer.flip();
        System.out.println("flip后状态: " + buffer);
        // get()方法：相对读，从position位置读取一个byte，并将position+1，为下次读写作准备
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println("三次调用get后: " + buffer);
        System.out.println(new String(buffer.array()));
        // 把从position到limit中的内容移到0到limit-position的区域内
        // position和limit的取值也分别变成limit-position、capacity
        // 如果先将positon设置到limit，再compact，那么相当于clear()
        buffer.compact();
        System.out.println("compact后状态: " + buffer);
        System.out.println(new String(buffer.array()));
    }
}
