package com.tiger.flume;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.channel.AbstractChannelSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 实现一个ChannelSelector, 实现将event发送给所有channel的功能
 */
public class CustomChannelSelector extends AbstractChannelSelector{

    private final List<Channel> requiredChannels = new ArrayList<>();
    private final List<Channel> optionalChannels = new ArrayList<>();

    /**
     * 根据event返回该event需要发送到的required channel
     * 如果event发送到required channel失败, 将会导致事务失败
     */
    @Override
    public List<Channel> getRequiredChannels(Event event) {
        return requiredChannels;
    }

    /**
     * 根据event返回该event需要发送到的optional channel
     * 如果event发送到optional channel失败, 失败将会被忽略
     */
    @Override
    public List<Channel> getOptionalChannels(Event event) {
        return optionalChannels;
    }

    /**
     * 从context中获取配置文件中的配置
     * 根据这里的逻辑, 配置文件应该这样配置
     * a1.sources = r1
     * a1.channels = c1 c2 c3 c4
     * a1.sources.r1.selector.type = com.tiger.flume.CustomChannelSelector
     * a1.sources.r1.selector.optional = c1 c2
     * a1.sources.r1.selector.required = c3 c4
     */
    @Override
    public void configure(Context context) {
        Map<String, Channel> channelNameMap = getChannelNameMap();

        String optionalList = context.getString("optional");
        if (optionalList != null && !"".equals(optionalList)) {
            String[] channels = optionalList.split(" ");
            Arrays.asList(channels).forEach(channel -> {
                optionalChannels.add(channelNameMap.get(channel));
            });
        }

        String requiredList = context.getString("required");
        if (requiredList != null && !"".equals(requiredList)) {
            String[] channels = requiredList.split(" ");
            Arrays.asList(channels).forEach(channel -> {
                requiredChannels.add(channelNameMap.get(channel));
            });
        }
    }
}
