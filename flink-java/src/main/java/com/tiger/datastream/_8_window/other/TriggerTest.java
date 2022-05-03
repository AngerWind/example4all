package com.tiger.datastream._8_window.other;

import com.google.common.collect.Lists;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.Window;

import java.util.List;
import java.util.Objects;

public class TriggerTest {

    private static class CustomCountTrigger <W extends Window> extends Trigger<Object, W> {
        private Long maxCount;
        private ListStateDescriptor<String> stateDescriptor = new ListStateDescriptor<>("UidState", String.class);

        public CustomCountTrigger(Long maxCount) {
            this.maxCount = maxCount;
        }

        @Override
        public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx) throws Exception {
            // 获取uid信息
            Tuple3<String, String, Long> uidTuple = (Tuple3<String, String, Long>) element;
            String key = uidTuple.f0;
            String uid = uidTuple.f1;
            // 获取状态
            ListState<String> uidState = ctx.getPartitionedState(stateDescriptor);
            // 更新状态
            Iterable<String> iterable = uidState.get();
            List<String> uidList = Lists.newArrayList();
            if (!Objects.equals(iterable, null)) {
                uidList = Lists.newArrayList(iterable);
            }
            boolean isContains = uidList.contains(uid);
            if (!isContains) {
                uidList.add(uid);
                uidState.update(uidList);
            }

            // 大于等于3个用户触发计算
            if (uidList.size() >= maxCount) {
                uidState.clear();
                return TriggerResult.FIRE;
            }
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
            return TriggerResult.CONTINUE;
        }

        @Override
        public void clear(W window, TriggerContext ctx) throws Exception {
            ctx.getPartitionedState(stateDescriptor).clear();
        }

        public static <W extends Window> CustomCountTrigger<W> of(long maxCount) {
            return new CustomCountTrigger<>(maxCount);
        }
    }
}
