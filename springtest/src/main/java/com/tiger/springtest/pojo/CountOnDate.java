package com.tiger.springtest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title CountOnDate
 * @date 2021/1/26 15:18
 * @description 用于统计单位时间内某项数据的个数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountOnDate {

    /**
     * 时间戳， 毫秒
     */
    private Long date;
    /**
     *  数量
     */
    private Long count;
}
