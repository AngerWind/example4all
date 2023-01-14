package com.tiger.spark._2_rdd.operator


/**
 * 相关联的文件在input/user_visit_action.csv
 * 文件中各个字段含义如下
 * 编号   字段名称              字段类型      字段含义
 * 1      date                String      用户点击行为的日期
 * 2      user_id             Long        用户的 ID
 * 3      session_id          String      Session 的 ID
 * 4      page_id             Long        某个页面的 ID
 * 5      action_time         String      动作的时间点
 * 6      search_keyword      String      用户搜索的关键词
 * 7      click_category_id   Long        某一个商品品类的ID
 * 8      click_product_id    Long        某一个商品的ID
 * 9      order_category_ids  String      一次订单中所有品类的 D集合
 * 10     order_product_ids   String      一次订单中所有商品的ID集合
 * 11     pay_category_ids    String      一次支付中所有品类的ID集合
 * 12     pay_product_ids     String      一次支付中所有商品的ID集合
 * 13     city_id             Long        城市id
 *
 *
 * 主要包含用户的 4 种行为： 搜索，点击，下单，支付。 数据规则如下：
 * ➢ 每一行数据表示用户的一次行为，这个行为只能是 4 种行为的一种
 * ➢ 数据文件中每行数据采用逗号分隔数据
 * ➢ 如果搜索关键字为空,表示数据不是搜索数据
 * ➢ 如果点击的品类 ID 和产品 ID 为-1，表示数据不是点击数据
 * ➢ 针对于下单行为，一次可以下单多个商品，所以品类 ID 和产品 ID 可以是多个， id 之
 * 间采用逗号分隔，如果本次不是下单行为，则数据采用 null 表示
 * ➢ 支付行为和下单行为类似
 */
package object practice {

}
