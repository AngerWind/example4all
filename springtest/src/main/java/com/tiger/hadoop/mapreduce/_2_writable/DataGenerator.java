package com.tiger.hadoop.mapreduce._2_writable;

import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title DataGenerator
 * @date 2022/3/3 17:36
 * @description
 */
public class DataGenerator {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Tiger.Shen\\Desktop\\aa.txt";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file)) {
            while (file.length() < 50 << 20) {
                long phone = RandomUtils.nextLong(0, 2 << 11);
                String ip = String.format("%d.%d.%d.%d", RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256));
                long up = RandomUtils.nextInt(0, 1000);
                long down = RandomUtils.nextInt(0, 1000);
                long code = RandomUtils.nextInt(0, 1000);

                writer.append(String.format("%011d    %-15s    %-3d    %-3d    %-3d%n", phone, ip, up, down, code));
            }
            writer.flush();
        }

    }
}
