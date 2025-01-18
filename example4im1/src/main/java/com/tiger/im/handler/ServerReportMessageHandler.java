package com.tiger.im.handler;


import com.tiger.chat.bean.AppMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       ServerReportMessageHandler.java</p>
 * <p>@PackageName:     com.freddy.chat.im.handler</p>
 * <b>
 * <p>@Description:     服务端返回的消息发送状态报告</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/22 19:16</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class ServerReportMessageHandler extends AbstractMessageHandler {

    @Override
    protected void action(AppMessage message) {
        log.debug( "收到消息状态报告，message=" + message);
    }
}
