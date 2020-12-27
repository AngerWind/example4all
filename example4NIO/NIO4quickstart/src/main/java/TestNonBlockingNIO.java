import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * @author Tiger.Shen
 * @date 2020/8/8 11:30
 */
public class TestNonBlockingNIO {

    @Test
    public void client() throws IOException {
        // 1. 获取通道
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 9889));

        // 2. 切换成非阻塞模式
        socketChannel.configureBlocking(false);

        // 3. 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 4. 发送数据给服务器
        byteBuffer.put(new Date().toString().getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        // 5. 关闭通道
        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2. 切换到非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 3. 绑定连接
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9889));

        // 4. 获取选择器
        Selector selector = Selector.open();

        // 5. 将通道注册到选择器上, 并选择监听的事件类型
        //    若监听不止一个事件，则可以使用 | 连接符操作
        //    例： SelectionKey.OP_WRITE | SelectionKey.OP_READ
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6. 轮询式的获取选择器上准备就绪的事件
        while (selector.select() > 0){
            
        }
    }
}
