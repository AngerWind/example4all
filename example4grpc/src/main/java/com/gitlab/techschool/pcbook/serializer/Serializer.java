package com.gitlab.techschool.pcbook.serializer;

import com.gitlab.techschool.pcbook.pb.Laptop;
import com.gitlab.techschool.pcbook.pb.Laptop.Builder;
import com.gitlab.techschool.pcbook.sample.Generator;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.util.JsonFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/17
 * @description
 */
public class Serializer {

    // 将 Laptop 实例写入二进制文件
    public void writeBinaryFile(Laptop laptop, String filename) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filename);
        laptop.writeTo(outputStream);
        outputStream.close();
    }

    // 从二进制文件中读取 Laptop 实例
    public Laptop readBinaryFile(String filename) throws IOException {
        FileInputStream inputStream = new FileInputStream(filename);
        Laptop laptop = Laptop.parseFrom(inputStream);
        inputStream.close();
        return laptop;
    }

    // 将 Laptop 实例写入 JSON 文件
    public void writeJsonFile(Laptop laptop, String filename) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields(new HashSet<>(Laptop.getDescriptor().getFields()))
                .preservingProtoFieldNames();

        // 转换为json字符串
        String jsonString = printer.print(laptop);

        // 写入文件
        FileOutputStream outputStream = new FileOutputStream(filename);
        outputStream.write(jsonString.getBytes());
        outputStream.close();
    }

    public Laptop readJsonFile(String filename) throws IOException {
        JsonFormat.Parser parser = JsonFormat.parser();
        Builder builder = Laptop.newBuilder();
        parser.merge(new InputStreamReader(new FileInputStream(filename)), builder);

        return builder.build();
    }

    public static void main(String[] args) {
        Laptop laptop = new Generator().NewLaptopWithRandomUUID();
        Serializer serializer = new Serializer();

        try {
            serializer.writeBinaryFile(laptop, "laptop.bin");
            Laptop laptop1 = serializer.readBinaryFile("laptop.bin");
            assert laptop.equals(laptop1);

            serializer.writeJsonFile(laptop, "laptop.json");
            Laptop laptop2 = serializer.readJsonFile("laptop.json");
            assert laptop.equals(laptop2);
        } catch (IOException ignored) {

        }
    }
}
