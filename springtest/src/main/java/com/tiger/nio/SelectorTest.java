package com.tiger.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

import static java.nio.channels.SelectionKey.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SelectorTest
 * @date 2021/7/29 9:53
 * @description
 */
public class SelectorTest {

    @Test
    @SneakyThrows
    public void test() {
        Selector selector = Selector.open();

        ServerSocket serverSocket = new ServerSocket(9999);
        ServerSocketChannel socketChannel = serverSocket.getChannel();
        socketChannel.configureBlocking(false);

        SelectionKey selectionKey = socketChannel.register(selector, OP_ACCEPT | OP_READ | OP_WRITE | OP_CONNECT);

        /**
         * attchment, 可以通过attchment识别特定的通道， 或者将channel相关联的对象添加到attchment中， 方便获取。
         */
        // 注册的时候关联
        // SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);

        // 注册后关联
        selectionKey.attach(ByteBuffer.allocate(1024));

        /**
         * selector对channel感兴趣的操作
         */
        // SelectionKey 就是做了位运算, 每一个操作代表一位。
        int interests = selectionKey.interestOps();
        boolean isInterestedInAccept = SelectionKey.OP_ACCEPT == (interests & SelectionKey.OP_ACCEPT);
        boolean isInterestedInConnect = SelectionKey.OP_CONNECT == (interests & SelectionKey.OP_CONNECT);
        boolean isInterestedInRead = SelectionKey.OP_READ == (interests & SelectionKey.OP_READ);
        boolean isInterestedInWrite = SelectionKey.OP_WRITE == (interests & SelectionKey.OP_WRITE);

        /**
         * channel准备好的操作
         */
        int readySet = selectionKey.readyOps(); // 通过readySet可以使用与上面相同的办法判断某个操作是否准备好。
        selectionKey.isAcceptable();
        selectionKey.isConnectable();
        selectionKey.isReadable();
        selectionKey.isWritable();

        /**
         * 通过SelectionKey获取channel和selector
         */
        SelectableChannel selectableChannel = selectionKey.channel();
        Selector selector1 = selectionKey.selector();

    }

    @Test
    @SneakyThrows
    public void test2() {

        try (Selector selector = Selector.open();
            ServerSocket serverSocket = new ServerSocket(9999);
            ServerSocketChannel socketChannel = serverSocket.getChannel();) {

            socketChannel.configureBlocking(false);
            socketChannel.register(selector, OP_ACCEPT | OP_READ | OP_WRITE | OP_CONNECT, ByteBuffer.allocate(1048));

            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(key -> {

                ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                ByteBuffer buffer = (ByteBuffer) key.attachment();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.

                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.

                } else if (key.isReadable()) {
                    // a channel is ready for reading

                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }

                selectionKeys.remove(key);
            });
        }

    }

}
