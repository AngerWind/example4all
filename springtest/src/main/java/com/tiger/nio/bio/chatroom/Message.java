package com.tiger.nio.bio.chatroom;

import java.io.Serializable;

import static com.tiger.nio.bio.chatroom.Message.FinalValue.MSG_SYSTEM;

public class Message implements Serializable {
    public int type = MSG_SYSTEM;
    public String message;

    public Message() { }

    public Message(String message) {
        this.message = message;
    }

    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }

    public static final class FinalValue {
        public static final int MSG_SYSTEM = 0;
        public static final int MSG_GROUP = 1; // 群发
        public static final int MSG_PRIVATE = 2; // 私聊
        public static final int MSG_ONLINE = 3; // 查看在线用户
    }
}
