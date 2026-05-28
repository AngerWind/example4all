package com.tiger.chat.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelContainer {

    private ChannelContainer() {}

    private static final Map<String, NettyChannel> CHANNELS = new ConcurrentHashMap<>();


    public static void saveChannel(NettyChannel channel) {
        if (channel == null) {
            return;
        }
        CHANNELS.put(channel.getUserId(), channel);
    }

    public static NettyChannel removeChannelIfConnectNoActive(Channel channel) {
        if (channel == null) {
            return null;
        }

        String channelId = channel.id().toString();

        return removeChannelIfConnectNoActive(channelId);
    }

    public static NettyChannel removeChannelIfConnectNoActive(String channelId) {
        if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
            return CHANNELS.remove(channelId);
        }

        return null;
    }

    public static String getUserIdByChannel(Channel channel) {
        return getUserIdByChannel(channel.id().toString());
    }

    public static String getUserIdByChannel(String channelId) {
        if (CHANNELS.containsKey(channelId)) {
            return CHANNELS.get(channelId).getUserId();
        }

        return null;
    }

    public static NettyChannel getActiveChannelByUserId(String userId) {
        for (Map.Entry<String, NettyChannel> entry : CHANNELS.entrySet()) {
            if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
