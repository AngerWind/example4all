package com.tiger.format;

import java.text.MessageFormat;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MessageFormatTest
 * @date 2021/6/3 20:34
 * @description
 */
public class MessageFormatTest {

    public static void main(String[] args) {
        // 单引号作为转义字符， 要输出单引号要使用两个单引号
        String format = MessageFormat.format("''{0}''", 1);
        System.out.println(format);

        System.out.println(MessageFormat.format("'{'{0}'}'", "X-rapido"));
        System.out.println(MessageFormat.format("'{''''}'", 1));
        System.out.println(MessageFormat.format("'{'{0}}", 2));
    }
}
