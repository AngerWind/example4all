package com.tiger.chat.bean;


import com.tiger.chat.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       BaseMessage.java</p>
 * <p>@PackageName:     com.freddy.chat.bean</p>
 * <b>
 * <p>@Description:     消息基类</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:02</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {

    protected String msgId;       // 消息id
    protected int msgType;        // 消息类型
    protected int msgContentType; // 消息内容乐行
    protected String fromId;      // 发送者id
    protected String toId;        // 接收者id
    protected long timestamp;     // 消息时间戳
    protected int statusReport;   // 消息状态报告
    protected String extend;      // 扩展字段，以key/value形式存放json
    protected String content;     // 消息内容

}
