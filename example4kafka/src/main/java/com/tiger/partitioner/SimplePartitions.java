package com.tiger.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Tiger.Shen
 * @date 2020/8/27 21:56
 */
public class SimplePartitions implements Partitioner {
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(topic);
        if (partitions.size() > 0) {
            return key.toString().hashCode() % cluster.availablePartitionsForTopic(topic).size();
        } else {
            return 0;
        }

    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}
