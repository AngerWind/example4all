package com.tiger.chat;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tiger.chat.bean.SingleMessage;
import com.tiger.chat.event.CEventCenter;
import com.tiger.chat.event.Events;
import com.tiger.chat.event.I_CEventListener;
import com.tiger.chat.utils.CThreadPoolExecutor;
import com.tiger.im.IMSConnectStatusListener;
import com.tiger.im.IMSEventListener;
import com.tiger.im.MessageProcessor;
import com.tiger.im.MessageType;
import com.tiger.im.interf.IMSClientInterface;
import com.tiger.im.netty.NettyTcpClient;

public class ClientDemo implements I_CEventListener {



    String userId = "100002";
    String token = "token_" + userId;
    IMSClientInterface imsClient;


    // 当前处理的消息类型
    private static final String[] EVENTS = {
            Events.CHAT_SINGLE_MESSAGE
    };

    protected void setUp() {

        List<InetSocketAddress> serverList = new ArrayList<>();
        serverList.add(InetSocketAddress.createUnresolved("192.168.0.102", 8855));

        // 设置app的状态
        imsClient = new NettyTcpClient();


        // init
        imsClient.init(serverList, new IMSEventListener(userId, token), new IMSConnectStatusListener());

        // 将自己注册到消息中心中, 并接受CHAT_SINGLE_MESSAGE类型的消息
        CEventCenter.registerEventListener(this, EVENTS);


    }


    public void sendMsg(String content) {
        SingleMessage message = new SingleMessage();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType(MessageType.SINGLE_CHAT.getMsgType());
        message.setMsgContentType(MessageType.MessageContentType.TEXT.getMsgContentType());
        message.setFromId(userId);
        message.setToId("100001");
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(content);

        // 发送消息
        MessageProcessor.getInstance().sendMsg(message);

    }

    protected void stop() {
        CEventCenter.unregisterEventListener(this, EVENTS);
    }

    // 当前设备接受到消息, 展示消息
    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CHAT_SINGLE_MESSAGE: {
                final SingleMessage message = (SingleMessage) obj;
                CThreadPoolExecutor.runOnMainThread(new Runnable() {

                    @Override
                    public void run() {
                        // todo 展示消息到首页
                    }
                });
                break;
            }

            default:
                break;
        }
    }
}
