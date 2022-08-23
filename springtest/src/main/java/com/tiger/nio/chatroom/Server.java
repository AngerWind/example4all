package com.tiger.nio.chatroom;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {

    private ServerSocketChannel serverSocket;
    private Selector selector;

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try {
            // 服务初始化
            serverSocket = ServerSocketChannel.open();
            // 创建一个selector
            selector = Selector.open();
            // 设置为非阻塞
            serverSocket.configureBlocking(false);
            // 绑定端口
            serverSocket.bind(new InetSocketAddress("localhost", 10086));
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
                        processAccept(selector, key);
                    }

                    if (key.isReadable()) {
                        // 根据对应的key获取到channel
                        SocketChannel socket = (SocketChannel)key.channel();
                        ByteBuffer bf = ByteBuffer.allocate(3);
                        CharBuffer charBuffer = bf.asCharBuffer();
                        StringBuilder stringBuilder = new StringBuilder();
                        int len = 0;
                        // 捕获异常，因为在客户端关闭后会发送FIN报文，会触发read事件，但连接已关闭,此时read()会产生异常
                        try {
                            // 从channel中读取数据
                            while ((len = socket.read(bf)) > 0) {
                                // 切换到读模式
                                bf.flip();
                                // 这里会有问题, 应该等读取玩所有的字节后转换为utf-8, 这样读一次转换一次会导致乱码
                                // 现在不知道怎么解决
                                stringBuilder.append(StandardCharsets.UTF_8.decode(bf));
                                bf.clear();
                            }
                            String msg = stringBuilder.toString();
                            if (StringUtils.isNotBlank(msg)) {
                                // 发送消息给其他的客户端
                                // 遍历selector中所有的selectorKey, 并获取对应的channel
                                for (SelectionKey selectionKey : selector.keys()) {
                                    SelectableChannel selectableChannel = selectionKey.channel();
                                    // 排除掉ServerSocketChannel和当前自身socket
                                    if (selectableChannel instanceof SocketChannel && !selectableChannel.equals(socket)) {
                                        ((SocketChannel) selectableChannel).write(StandardCharsets.UTF_8.encode(msg));
                                    }
                                }
                            }
                        } catch (IOException e) {
                            // 客户端关闭了
                            key.cancel();
                            socket.close();
                            System.out.println("客戶端已断开");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器异常，即将关闭..........");
        }
    }

    private void processAccept(Selector selector, SelectionKey key) throws IOException {
        // 从key中获取的ServerSocketChannel就是this.serverSocket
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        // 注册read，监听客户端发送的消息
        socketChannel.register(selector, SelectionKey.OP_READ);
        // keys为所有键，除掉serverSocket注册的键就是已连接socketChannel的数量
        String message = "连接成功 你是当前第" + (selector.keys().size() - 1) + "个用户";
        // 向客户端发送消息
        socketChannel.write(ByteBuffer.wrap(message.getBytes()));
    }
}
