package com.tiger.chat.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       Head.java</p>
 * <p>@PackageName:     com.freddy.chat.bean</p>
 * <b>
 * <p>@Description:     消息头</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:00</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Head {

    private String msgId;
    private int msgType;
    private int msgContentType;
    private String fromId;
    private String toId;
    private long timestamp;
    private int statusReport;
    private String extend;

}
