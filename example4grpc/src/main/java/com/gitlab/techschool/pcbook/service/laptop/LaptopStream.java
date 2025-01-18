package com.gitlab.techschool.pcbook.service.laptop;

import com.gitlab.techschool.pcbook.pb.Laptop;
import io.grpc.StatusRuntimeException;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public interface LaptopStream {

    void send(Laptop laptop);

}
