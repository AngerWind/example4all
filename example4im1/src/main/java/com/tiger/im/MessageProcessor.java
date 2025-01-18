package com.tiger.im;


import com.tiger.chat.bean.AppMessage;
import com.tiger.chat.bean.BaseMessage;
import com.tiger.chat.bean.ContentMessage;
import com.tiger.chat.utils.CThreadPoolExecutor;
import com.tiger.im.handler.IMessageHandler;
import com.tiger.im.handler.MessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 03:27</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class MessageProcessor implements IMessageProcessor {

    private MessageProcessor() {

    }

    private static class MessageProcessorInstance {
        private static final IMessageProcessor INSTANCE = new MessageProcessor();
    }

    public static IMessageProcessor getInstance() {
        return MessageProcessorInstance.INSTANCE;
    }

    /**
     * 接收消息
     * @param message
     */
    @Override
    public void receiveMsg(final AppMessage message) {
        IMessageHandler messageHandler = MessageHandlerFactory.getHandlerByMsgType(message.getHead().getMsgType());
        if (messageHandler != null) {
            messageHandler.execute(message);
        } else {
            log.error("未找到消息处理handler，msgType=" + message.getHead().getMsgType());
        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(final AppMessage message) {
        boolean isActive = IMSClientBootstrap.getInstance().isActive();
        if (isActive) {
            IMSClientBootstrap.getInstance().sendMessage(MessageBuilder.getProtoBufMessageBuilderByAppMessage(message).build());
        } else {
            log.error("发送消息失败");
        }
    }

    /**
     * 发送消息
     */
    @Override
    public void sendMsg(ContentMessage message) {
        this.sendMsg(MessageBuilder.buildAppMessage(message));
    }

    /**
     * 发送消息
     */
    @Override
    public void sendMsg(BaseMessage message) {
        this.sendMsg(MessageBuilder.buildAppMessage(message));
    }
}
