package com.tiger.chat.server;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NettyChannel {

        private String userId;
        private Channel channel;

        public NettyChannel(String userId, Channel channel) {
            this.userId = userId;
            this.channel = channel;
        }

        public boolean isActive() {
            return channel.isActive();
        }
    }