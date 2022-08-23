package com.tiger.nio.bio.chatroom;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.tiger.nio.bio.chatroom.Message.FinalValue.*;

@Slf4j
public class Server {
    private static CopyOnWriteArrayList<UserThread> userList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("-----Server-----");
        // 1、指定端口 使用ServerSocket创建服务器
        ServerSocket server = new ServerSocket(6666);
        int userCount = 0;
        while (true) {
            // 2、阻塞式等待连接 accept
            Socket client = server.accept();
            UserThread user = new UserThread(client);
            userList.add(user);
            new Thread(user, "用户" + userCount).start();
            userCount++;
            log.debug("当前在线用户：" + userList.toString());
        }
    }

    /**
     * 管理用户信息
     */
    private static class UserThread implements Runnable {
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Socket client;
        private boolean isRunning;
        private String name;

        public UserThread(Socket client) {
            this.client = client;
            try {
                ois = new ObjectInputStream(client.getInputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
                isRunning = true;
                // 获取名称
                this.name = receive().message;
                this.send(new Message(MSG_SYSTEM, "欢迎你的到来"));
                sendOthers(new Message(MSG_SYSTEM, this.name + "已上线"));
            } catch (Exception e) {
                release();
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                Message msg = receive();
                if (msg != null) {
                    sendOthers(msg);
                }
            }
        }

        /**
         * 接收消息
         */
        private Message receive() {
            Message msg = null;
            try {
                msg = (Message)ois.readObject();
            } catch (Exception e) {
                release();
            }
            return msg;
        }

        /**
         * 发送消息
         */
        private void send(Message msg) {
            try {
                oos.writeObject(msg);
                oos.flush();
            } catch (Exception e) {
                release();
            }
        }

        /**
         * 群聊：获取自己的消息，发给其他人 私聊: 约定数据格式: @xxx:msg
         */
        private void sendOthers(Message msg) {
            if (msg.type == MSG_PRIVATE) { // 私聊
                String[] s = msg.message.split("_");
                for (UserThread other : userList) {
                    if (other.name.equals(s[0])) {// 目标
                        other.send(new Message("来自" + this.name + "的消息:" + s[1]));
                        break;
                    } else {
                        log.debug("该用户不存在!!!");
                    }
                }
            } else if (msg.type == MSG_ONLINE) {
                for (UserThread other : userList) {
                    if (other == this) {
                        other.send(new Message("当前在线用户: " + userList.toString()));
                        break;
                    }
                }
            } else {
                for (UserThread other : userList) {
                    if (other == this) { // 自己
                        continue;
                    }
                    if (msg.type == MSG_GROUP) {
                        log.debug("转发群聊消息:" + msg.message + " -->" + other);
                        other.send(new Message(this.name + "对所有人说:" + msg.message));// 群聊消息
                    } else if (msg.type == MSG_SYSTEM){
                        other.send(msg); // 系统消息
                    }
                }
            }
        }

        /**
         * 释放资源
         */
        private void release() {
            // 心跳
            this.isRunning = false;
            Utils.close(ois, oos, client);
            // 退出
            userList.remove(this);
            sendOthers(new Message(this.name + "已下线"));
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
