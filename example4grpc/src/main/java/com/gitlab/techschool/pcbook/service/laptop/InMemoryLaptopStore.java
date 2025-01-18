package com.gitlab.techschool.pcbook.service.laptop;

import com.gitlab.techschool.pcbook.pb.Filter;
import com.gitlab.techschool.pcbook.pb.Laptop;
import com.gitlab.techschool.pcbook.pb.Memory;
import com.gitlab.techschool.pcbook.service.exception.AlreadyExistsException;
import io.grpc.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public class InMemoryLaptopStore implements LaptopStore {

    private static final Logger logger = Logger.getLogger(InMemoryLaptopStore.class.getName());

    private final ConcurrentHashMap<String, Laptop> store = new ConcurrentHashMap<>();

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 在保存的时候, 接受的参数最好是实体类, 而不是protobuf创建的实体类
     * 这两种类做好分割
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    @Override
    public void save(Laptop laptop) {
        if (store.containsKey(laptop.getId())) {
            throw new AlreadyExistsException("laptop id already exists");
        }

        // 深拷贝
        Laptop toStore = laptop.toBuilder().build();
        store.put(toStore.getId(), toStore);
    }

    @Override
    public Laptop findById(String id) {
        if (!store.containsKey(id)) {
            return null;
        }

        // 深拷贝
        return store.get(id).toBuilder().build();
    }

    @Override
    public void search(Context ctx, Filter filter, LaptopStream stream) {
        for (Map.Entry<String, Laptop> entry : store.entrySet()) {

            // 如果执行请求超时, 那么直接返回
            if (ctx.isCancelled()) {
                logger.info("context is cancelled");
                return;
            }

            // try {
            //     TimeUnit.SECONDS.sleep(1);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }

            // deep copy
            Laptop laptop = entry.getValue().toBuilder().build();

            // 判断是否符合filter的条件
            if (isQualified(filter, laptop)) {
                stream.send(laptop);
            }
        }
    }

    private boolean isQualified(Filter filter, Laptop laptop) {
        if (laptop.getPriceUsd() > filter.getMaxPriceUsd()) {
            return false;
        }

        if (laptop.getCpu().getNumberCores() < filter.getMinCpuCores()) {
            return false;
        }

        if (laptop.getCpu().getMinGhz() < filter.getMinCpuGhz()) {
            return false;
        }

        if (toBit(laptop.getRam()) < toBit(filter.getMinRam())) {
            return false;
        }

        return true;
    }

    private long toBit(Memory memory) {
        long value = memory.getValue();

        return switch (memory.getUnit()) {
            case BIT -> value;
            case BYTE -> value << 3;
            case KILOBYTE -> value << 13;
            case MEGABYTE -> value << 23;
            case GIGABYTE -> value << 33;
            case TERABYTE -> value << 43;
            default -> 0;
        };
    }
}
