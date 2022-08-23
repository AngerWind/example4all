package com.tiger.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SockChannel
 * @date 2021/11/17 17:00
 * @description
 */
public class SocketChannelTest {

    @Test
    @SneakyThrows
    public void server() {
        try (
            // 服务初始化
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            // 创建一个selector
            Selector selector = Selector.open();
            ) {
            // 设置为非阻塞
            serverSocket.configureBlocking(false);
            // 绑定端口
            serverSocket.bind(new InetSocketAddress("localhost", 9999));
            // 将serverSocket注册到selector中, 并设置感兴趣的事件key
            // 注册OP_ACCEPT事件（即监听该事件，如果有客户端发来连接请求，则该键在select()后被选中）
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端开启了");
            // 轮询服务
            while (true) {
                // 当selector中注册的socket的感兴趣的事件发生时返回, 否则堵塞
                selector.select();
                // 获取所有就绪的key
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                // 处理已选择键集事件
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    // 已经处理了的事件, 需要将selectioKey从已就绪的key中删除掉, 防止事件被重复处理
                    it.remove();
                    // 处理连接请求
                    if (key.isAcceptable()) {
                        // 处理请求
                        SocketChannel socketChannel = serverSocket.accept();
                        socketChannel.configureBlocking(false);
                        // 注册read，监听客户端发送的消息
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // keys为所有键，除掉serverSocket注册的键就是已连接socketChannel的数量
                        String message = "连接成功 你是当前第" + (selector.keys().size() - 1) + "个用户";
                        // 向客户端发送消息
                        socketChannel.write(ByteBuffer.wrap(message.getBytes()));
                        InetSocketAddress address = (InetSocketAddress)socketChannel.getRemoteAddress();
                        new ChatThread(key).start();
                    }

                    if (key.isReadable()) {
                        // 根据对应的key获取到channel
                        SocketChannel socket = (SocketChannel)key.channel();
                        InetSocketAddress address = (InetSocketAddress)socket.getRemoteAddress();
                        ByteBuffer bf = ByteBuffer.allocate(1024 * 4);
                        int len = 0;
                        // 捕获异常，因为在客户端关闭后会发送FIN报文，会触发read事件，但连接已关闭,此时read()会产生异常
                        try {
                            // 从channel中读取数据
                            while ((len = socket.read(bf)) > 0) {
                                bf.flip();
                                System.out.println(new String(bf.array(), 0, len));
                                bf.clear();
                            }
                        } catch (IOException e) {
                            // 客户端关闭了
                            key.cancel();
                            socket.close();
                            System.out.println("客戶端已断开");
                            continue;
                        }
                    }

                    // 控制台监听到有输入，注册OP_WRITE,然后将消息附在attachment中
                    if (key.isWritable()) {
                        // 发送消息给服务端
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.write((ByteBuffer)key.attachment());
                            /*
                                已处理完此次输入，但OP_WRITE只要当前通道输出方向没有被占用
                                就会准备就绪，select()不会阻塞（但我们需要控制台触发,在没有输入时
                                select()需要阻塞），因此改为监听OP_READ事件，该事件只有在socket
                                有输入时select()才会返回。
                            */
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器异常，即将关闭..........");
        }
    }

    @Test
    @SneakyThrows
    public void client() {
        try (
            // 初始化客户端
            SocketChannel socketChannel = SocketChannel.open();
            Selector selector = Selector.open();
            ) {

            socketChannel.configureBlocking(false);
            // 注册连接事件
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
            // 发起连接
            socketChannel.connect(new InetSocketAddress("localhost", 9999));
            // 开启控制台输入监听
            new ChatThread(selectionKey).start();
            // 轮询处理
            while (true) {
                if (socketChannel.isOpen()) {
                    // 在注册的键中选择已准备就绪的事件
                    selector.select();
                    // 已选择键集
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    // 处理准备就绪的事件
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // 删除当前键，避免重复消费
                        iterator.remove();
                        // 连接
                        if (key.isConnectable()) {
                            // 在非阻塞模式下connect也是非阻塞的，所以要确保连接已经建立完成
                            while (!socketChannel.finishConnect()) {
                                System.out.println("连接中");
                            }
                            // 将感兴趣的事件修改为可读
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        // 控制台监听到有输入，注册OP_WRITE,然后将消息附在attachment中
                        if (key.isWritable()) {
                            // 发送消息给服务端
                            socketChannel.write((ByteBuffer)key.attachment());
                            /*
                                已处理完此次输入，但OP_WRITE只要当前通道输出方向没有被占用
                                就会准备就绪，select()不会阻塞（但我们需要控制台触发,在没有输入时
                                select()需要阻塞），因此改为监听OP_READ事件，该事件只有在socket
                                有输入时select()才会返回。
                            */
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        // 处理输入事件
                        if (key.isReadable()) {

                            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                            int len = 0;
                            // 捕获异常，因为在服务端关闭后会发送FIN报文，会触发read事件，但连接已关闭,此时read()会产生异常
                            try {
                                if (socketChannel.isOpen() && (len = socketChannel.read(byteBuffer)) > 0) {
                                    System.out.println(new String(byteBuffer.array(), 0, len));
                                }
                            } catch (IOException e) {
                                System.out.println("服务器异常，请联系客服人员!正在关闭客户端.........");
                                key.cancel();
                                socketChannel.close();
                                selector.close();
                            }
                        }
                    }
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("客户端异常，请重启！");
        }
    }

    public static class ChatThread extends Thread {

        private Selector selector;
        private SocketChannel socketChannel;
        private SelectionKey selectionKey;

        public ChatThread(SelectionKey key) {
            super();
            this.selectionKey = key;
            this.selector = key.selector();
            this.socketChannel = (SocketChannel)key.channel();
        }

        @Override
        public void run() {
            try {
                // 等待连接建立
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine() && socketChannel.isOpen()) {
                String s = scanner.nextLine();
                try {
                    // 用户已输入，注册写事件，将输入的消息发送给客户端
                    selectionKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    selectionKey.attach(ByteBuffer.wrap(s.getBytes()));
                    // 唤醒之前因为监听OP_READ而阻塞的select()
                    selector.wakeup();
                } catch (CancelledKeyException e) {
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
