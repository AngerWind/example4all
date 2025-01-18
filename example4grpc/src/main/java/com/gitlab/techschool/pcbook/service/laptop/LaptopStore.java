package com.gitlab.techschool.pcbook.service.laptop;

import com.gitlab.techschool.pcbook.pb.Filter;
import com.gitlab.techschool.pcbook.pb.Laptop;
import io.grpc.Context;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public interface LaptopStore {

    void save(Laptop laptop);

    Laptop findById(String id);

    void search(Context ctx, Filter filter, LaptopStream stream);
}
