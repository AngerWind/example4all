package com.tiger.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title FileChannelTest
 * @date 2021/7/28 15:48
 * @description
 */
public class FileChannelTest {

    @SneakyThrows
    @Test
    public void test(){
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\Tiger.Shen\\Desktop\\Test.java", "rw");
        FileChannel channel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[1024];

        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = channel.read(buffer)) != -1) {
            System.out.println("read " + len + "bytes");
            buffer.flip();
            buffer.get(bytes, 0, len);
            sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
            buffer.clear();

        }
        System.out.println(sb.toString());
        channel.close();
        file.close();
    }
}
