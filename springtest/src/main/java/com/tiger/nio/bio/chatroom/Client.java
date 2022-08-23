package com.tiger.nio.bio.chatroom;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

import static com.tiger.nio.bio.chatroom.Message.FinalValue.*;

@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {
        System.out
            .println("tips: \n1. 直接发送消息会发给当前的所有用户 \n" + "2. @用户名:消息  会私发给你要发送的用户 \n" + "3. 输入  查询在线用户  会显示当前的在线用户");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("请输入用户名:");
        String name = br.readLine();
        // 1、建立连接: 使用Socket创建客户端 +服务的地址和端口
        Socket client = new Socket("localhost", 6666);
        // 2、客户端发送消息
        new Thread(new Send(client, name), "send-thread").start();
        // 3、客户端接收消息
        new Thread(new Receive(client), "receive-thread").start();
    }

    /**
     * 负责从socket的输入流中不断读取数据并打印
     */
    @Slf4j
    public static class Receive implements Runnable {
        private ObjectInputStream ois;
        private Socket client;
        private boolean isRunning;

        public Receive(Socket client) {
            this.client = client;
            this.isRunning = true;
            try {
                ois = new ObjectInputStream(client.getInputStream());
            } catch (Exception e) {
                release();
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    // 不断从输入流中读取数据并打印
                    Message msg = (Message)ois.readObject();
                    log.debug(msg.message);
                } catch (Exception e) {
                    release();
                }
            }
        }

        /**
         * 释放资源
         */
        private void release() {
            this.isRunning = false;
            Utils.close(ois, client);
        }
    }

    /**
     * 负责不断从控制台读取消息, 并发送到server端
     */
    @Slf4j
    public static class Send implements Runnable {
        private BufferedReader console;
        private ObjectOutputStream oos;
        private Socket client;
        private boolean isRunning;
        private String name;

        public Send(Socket client, String name) {
            this.client = client;
            console = new BufferedReader(new InputStreamReader(System.in));
            this.isRunning = true;
            this.name = name;
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                // 发送名称
                send(new Message(MSG_SYSTEM, this.name));
            } catch (Exception e) {
                this.release();
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                // 从控制台读取消息
                String msgStr = getStrFromConsole();
                Message msg;
                boolean isPrivate = msgStr.startsWith("@");
                if (isPrivate) {
                    int idx = msgStr.indexOf(":");
                    String targetName = msgStr.substring(1, idx);
                    msgStr = msgStr.substring(idx + 1);
                    msg = new Message(MSG_PRIVATE, targetName + "_" + msgStr);
                } else if ("查询在线用户".equals(msgStr)) {
                    msg = new Message(MSG_ONLINE, "请求在线人数");
                } else {
                    msg = new Message(MSG_GROUP, msgStr);
                }
                send(msg);
            }
        }

        // 发送消息
        private void send(Message msg) {
            try {
                oos.writeObject(msg);
                oos.flush();
            } catch (Exception e) {
                release();
            }
        }

        /**
         * 从控制台获取消息
         *
         * @return
         */
        private String getStrFromConsole() {
            try {
                return console.readLine();
            } catch (Exception e) {
                log.debug("控制台写入出现了一些问题");
            }
            return "";
        }

        // 释放资源
        private void release() {
            this.isRunning = false;
            Utils.close(oos, client);
        }
    }
}
