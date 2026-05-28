package com.tiger.chat.server;

import com.tiger.im.protobuf.MessageProtobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerDemo {

    public static void main(String[] args) {

        //boss线程监听端口，worker线程负责数据读写
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置线程池
            bootstrap.group(boss, worker);

            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);

            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(2)); // 在消息头加上两个字节的消息长度
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
                            0, 2, 0, 2)); // 从消息的头两个字节获取消息的长度
                    pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance())); // 将消息编码为protobuf
                    pipeline.addLast(new ProtobufEncoder()); // 将protobuf解码为消息
                    //处理类
                    pipeline.addLast(new ServerHandler());
                }
            });

            //设置TCP参数
            //1.设置全连接队列的大小
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //维持链接的活跃，清除死链接(SocketChannel的设置)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //关闭延迟发送
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            //绑定端口
            ChannelFuture future = bootstrap.bind(8855).sync();
            System.out.println("server start ...... ");

            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            //优雅退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

