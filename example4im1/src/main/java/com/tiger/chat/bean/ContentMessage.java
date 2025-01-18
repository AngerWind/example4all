package com.tiger.chat.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       ContentMessage.java</p>
 * <p>@PackageName:     com.freddy.chat.bean</p>
 * <b>
 * <p>@Description:     内容消息，包含单聊消息及群聊消息</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:06</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentMessage extends BaseMessage {

    protected boolean isRead;
    protected boolean isPlaying;
    protected boolean isLoading;


    public ContentMessage(String msgId, int msgType, int msgContentType, String fromId, String toId,
                          long timestamp, int statusReport, String extend, String content) {
        this.msgId = msgId;
        this.msgType = msgType;
        this.msgContentType = msgContentType;
        this.fromId = fromId;
        this.toId = toId;
        this.timestamp = timestamp;
        this.statusReport = statusReport;
        this.extend = extend;
        this.content = content;
    }

}

