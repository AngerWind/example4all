package com.tiger.dubbo.quickstart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Tiger.Shen
 * @date 2020/7/25 18:17
 */
@Data
@AllArgsConstructor
public class Address implements Serializable {

    private String userAddress;

    private Integer id;
}
