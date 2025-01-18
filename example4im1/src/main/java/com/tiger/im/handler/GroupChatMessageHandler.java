package com.tiger.im.handler;


import com.tiger.chat.bean.AppMessage;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       GroupChatMessageHandler.java</p>
 * <p>@PackageName:     com.freddy.chat.im.handler</p>
 * <b>
 * <p>@Description:     类描述</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 03:43</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class GroupChatMessageHandler extends AbstractMessageHandler {


    @Override
    protected void action(AppMessage message) {
        log.debug( "收到群聊消息，message=" + message);
    }
}
