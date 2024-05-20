package cn.javabook.chapter11.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * NIO中的Selector
 *
 */
public class TestSelector {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel channel1 = ServerSocketChannel.open();
        channel1.socket().bind(new InetSocketAddress("127.0.0.1", 9527));
        channel1.configureBlocking(false);
        ServerSocketChannel channel2 = ServerSocketChannel.open();
        channel2.socket().bind(new InetSocketAddress("127.0.0.1", 9528));
        channel2.configureBlocking(false);

        // 创建一个Selector对象
        Selector selector = Selector.open();
        // 按照字面意思理解，应该是这样的：selector.register(channel, event);
        // 但其实是这样的：channel.register(selector, SelectionKey.OP_READ);
        // 四种监听事件：
        // OP_CONNECT（连接就绪）
        // OP_ACCEPT（接收就绪）
        // OP_READ（读就绪）
        // OP_WRITE（写就绪）
        // 注册Channel到Selector，事件一旦被触发，监听随之结束
        SelectionKey key1 = channel1.register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey key2 = channel2.register(selector, SelectionKey.OP_ACCEPT);

        // 模版代码：在编写程序时，大多数时间都是在模板代码中添加相应的业务代码
        while(true) {
            int readyNum = selector.select();
            if (readyNum == 0) {
                continue;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            // 轮询
            for (SelectionKey key : selectedKeys) {
                Channel channel = key.channel();
                if (key.isConnectable()) {
                    if (channel == channel1) {
                        System.out.println("channel1连接就绪");
                    }
                    if (channel == channel2) {
                        System.out.println("channel2连接就绪");
                    }
                } else if (key.isAcceptable()) {
                    if (channel == channel1) {
                        System.out.println("channel1接收就绪");
                    }
                    if (channel == channel2) {
                        System.out.println("channel2接收就绪");
                    }
                }
                // 触发后删除，这里不删
                // it.remove();
            }
        }
    }
}
