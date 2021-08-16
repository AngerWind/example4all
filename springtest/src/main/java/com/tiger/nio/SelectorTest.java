package com.tiger.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

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
        SelectionKey selectionKey = socketChannel.register(selector, OP_ACCEPT | OP_READ | OP_WRITE);
        // selectionKey.is
    }

}
