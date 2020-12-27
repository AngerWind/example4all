import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Tiger.Shen
 * @date 2020/8/3 23:33
 *
 * 测试网络io并且返回应答
 */

public class TestBlocking2 {

    @Test
    public void client() throws IOException {
        SocketChannel socketChannel =
                SocketChannel.open(new InetSocketAddress("127.0.0.1", 10086));

        FileChannel fileChannel =
                FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        while(fileChannel.read(buf) != -1){
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }
        // 告诉对面已经写完了
        socketChannel.shutdownOutput();

        //接受客户端的反馈
        int len = 0;
        while ((len = socketChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

        fileChannel.close();
        socketChannel.close();
    }

    @Test
    public void serve() throws IOException {
        ServerSocketChannel serverSocketChannel =
                ServerSocketChannel.open();

        FileChannel fileChannel =
                FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        serverSocketChannel.bind(new InetSocketAddress(10086));

        SocketChannel socketChannel = serverSocketChannel.accept();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (socketChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        // 发送反馈给客户端
        byteBuffer.put("服务端接受数据成功".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        fileChannel.close();
        socketChannel.close();
        serverSocketChannel.close();

    }
}
