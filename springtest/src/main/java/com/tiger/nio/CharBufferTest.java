package com.tiger.nio;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title CharBufferTest
 * @date 2021/11/16 11:21
 * @description
 */
public class CharBufferTest {

    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(2);
        int position = buffer.position();
        buffer.put('a');
        int position1 = buffer.position();
        buffer.put("c");
        int position2 = buffer.position();
        System.out.println("" + position + position1 + position2);

        buffer.flip(); // 切换模式， 开始是写， 现在切换成读
        System.out.println(buffer.position());
        System.out.println(buffer.get());
        System.out.println(buffer.position());
        System.out.println(buffer.get());
        System.out.println(buffer.position());

        buffer.rewind(); // 重新读， 将position重置为0

        buffer.compact(); // 清除已读的数据，其实就是调整position和limit的位置

        buffer.clear(); // 清除buffer中的所有数据， 转换为写模式， 其实就是调整position和limit的位置

        buffer.mark(); // 标记一个位置
        buffer.reset(); // 将position设置为mark的位置


        CharBuffer ac = CharBuffer.wrap("ac");
        System.out.println(buffer.equals(ac)); // 相等， 先比较类型，然后比较内容
        System.out.println(buffer.compareTo(ac)); // 比较，按字典比较

    }

    /**
     * Scatter 分散器， 将channel中的数据读取到多个buffer中。先读入第一个buf中，读满下一个。
     * Gather 聚合器， 将多个buffer中的数据写入到channel中。 先将第一个buf的数据写入
     * 这对于固定格式的内容很有帮助。
     */
    @Test
    public void test() {
        ByteBuffer title = ByteBuffer.allocate(10);
        ByteBuffer content = ByteBuffer.allocate(1024);

        ByteBuffer[] buffers = {title, content};


        try (RandomAccessFile file = new RandomAccessFile("C:\\Users\\Tiger.Shen\\Desktop\\Test.java", "rw");
            FileChannel channel = file.getChannel();) {
            // 分散
            channel.read(buffers);

            // 聚集
            channel.write(buffers);
        } catch (Exception e) {

        }
    }
}
