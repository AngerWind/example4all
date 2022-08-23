package com.tiger.nio.chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    private volatile boolean isRunning = true;
    private Selector selector;
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;

    public void startClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("localhost", 10086));

            while (isRunning && socketChannel.isOpen()) {

                selector.select();
                // 返回selector已经选中的key
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isConnectable()) {
                        while (!socketChannel.finishConnect()) {
                            System.out.println("连接中");
                        }
                        selectionKey.interestOps(SelectionKey.OP_READ);
                        // 连接完成, 启动另外一个线程将控制台的输入发送到server
                        new Thread(new InputThread()).start();
                    }
                    if (selectionKey.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        int len = 0;
                        // 捕获异常，因为在服务端关闭后会发送FIN报文，会触发read事件，但连接已关闭,此时read()会产生异常
                        try {
                            if (socketChannel.isOpen() && (len = socketChannel.read(byteBuffer)) > 0) {
                                System.out.println(new String(byteBuffer.array(), 0, len));
                            }
                        } catch (IOException e) {
                            System.out.println("服务器异常，请联系客服人员!正在关闭客户端.........");
                            shutdown();
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().startClient();
    }

    public synchronized void shutdown() {
        try {
            if (isRunning) {
                selectionKey.cancel();
                socketChannel.close();
                selector.close();
                isRunning = false;
            }
        } catch (IOException e) {
            System.out.println("关闭客户端失败...");
        }
    }

    private class InputThread implements Runnable {

        @Override
        public void run() {
            System.out.println("请输入数据...");
            Scanner scanner = new Scanner(System.in);
            while (isRunning && socketChannel.isOpen()) {
                String inputStr = scanner.nextLine();
                // 关闭客户端
                if ("shutdown".equals(inputStr)) {
                    shutdown();
                }
                // 将消息发送到服务器
                // 不用注册写事件，只有当写入量大，或写需要争用时，才考虑注册写事件
                try {
                    socketChannel.write(StandardCharsets.UTF_8.encode(inputStr));
                } catch (IOException e) {
                    System.out.println("服务器异常，请联系客服人员!正在关闭客户端.........");
                    shutdown();
                }
            }
        }
    }
}
