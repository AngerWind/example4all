package com.tiger.chat.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiger.im.protobuf.MessageProtobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = ServerHandler.class.getSimpleName();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("ServerHandler channelActive()" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("ServerHandler channelInactive()");

        // 用户断开连接后，移除channel
        ChannelContainer.removeChannelIfConnectNoActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg message = (MessageProtobuf.Msg)msg;
        System.out.println("收到来自客户端的消息：" + message);
        int msgType = message.getHead().getMsgType();
        switch (msgType) {
            // 登录消息
            case 1001 -> {
                String fromId = message.getHead().getFromId();
                JSONObject jsonObj = JSON.parseObject(message.getHead().getExtend());

                // todo 这里应该带用户名和密码来, 校验正确之后颁发一个token, 每次连接的时候都校验这个token
                String token = jsonObj.getString("token");
                JSONObject resp = new JSONObject();

                // token正确
                if (token.equals("token_" + fromId)) {
                    resp.put("status", 1);
                    // 保存userId和对应的chanel到ChannelContainer中
                    ChannelContainer.saveChannel(new NettyChannel(fromId, ctx.channel()));
                } else {
                    // token错误
                    resp.put("status", -1);
                    // todo 应该关闭连接
                    ChannelContainer.removeChannelIfConnectNoActive(ctx.channel());
                }


                message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build())
                    .build();
                ChannelContainer.getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
            }

            // 心跳消息
            case 1002 -> {
                // 收到心跳消息，原样返回
                String fromId = message.getHead().getFromId();
                ChannelContainer.getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
            }
            case 2001 -> {
                // 收到2001或3001消息，返回给客户端消息发送状态报告
                String fromId = message.getHead().getFromId();
                MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
                MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
                sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
                sentReportHeadBuilder.setMsgType(1010);
                sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
                sentReportHeadBuilder.setStatusReport(1);
                sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
                ChannelContainer.getActiveChannelByUserId(fromId).getChannel()
                    .writeAndFlush(sentReportMsgBuilder.build());

                // 同时转发消息到接收方
                String toId = message.getHead().getToId();
                ChannelContainer.getActiveChannelByUserId(toId).getChannel().writeAndFlush(message);
            }
            case 3001 -> {
                // todo 群聊，自己实现吧，toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
            }
            default -> {
            }
        }
    }

}
