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
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = channel.read(buffer)) != -1) {
            // System.out.println("read " + len + "bytes");
            buffer.flip();
            sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
            buffer.clear();
        }
        System.out.println(sb.toString());
        channel.close();
        file.close();
    }

    @Test
    @SneakyThrows
    public void transform(){

        // 源channel传输到目的channel
        try (RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
            FileChannel      fromChannel = fromFile.getChannel();

            RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
            FileChannel toChannel = toFile.getChannel()) {

            long position = 0;
            long count = fromChannel.size();

            fromChannel.transferTo(position, count, toChannel); // transformTo
            toChannel.transferFrom(fromChannel, position, count); // transformFrom
        }

    }
}
