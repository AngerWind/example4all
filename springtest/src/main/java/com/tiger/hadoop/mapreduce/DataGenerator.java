package com.tiger.hadoop.mapreduce;

import cn.hutool.core.util.RandomUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Shen
 * @version v1.0
 * @Title DataGenerator
 * @date 2022/2/28 21:34
 * @description
 */
public class DataGenerator {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\qq.txt");
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
        if (file.isFile()) {
            FileWriter fileWriter = new FileWriter(file);
            long id = 0L;
            while (file.length() < 200 * 1024 * 1024) {
                Long phone = RandomUtil.randomLong(2 << 12);
                String ip = String.format("%s.%s.%s.%s", RandomUtil.randomInt(256),RandomUtil.randomInt(256),
                        RandomUtil.randomInt(256),RandomUtil.randomInt(256));
                Long up = RandomUtil.randomLong(1000);
                Long down = RandomUtil.randomLong(1000);
                int code = RandomUtil.randomInt(100);
                fileWriter.append(String.format("%011d    %-15s    %-3d    %-3d    %-2d\n", phone, ip, up, down, code));
            }
            fileWriter.flush();
        }
    }
}
