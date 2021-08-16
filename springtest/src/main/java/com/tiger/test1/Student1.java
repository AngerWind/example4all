package com.tiger.test1;

import lombok.Data;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Student1
 * @date 2021/3/8 18:40
 * @description
 */
@Data
@AllArgsConstructor
public class Student1 {
    private String seconds;

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Student1.class);
        IOException error = new IOException("error");
        logger.error("taskId: {}, hahah:{}, error:{}", 123, "world", error);
    }
}
