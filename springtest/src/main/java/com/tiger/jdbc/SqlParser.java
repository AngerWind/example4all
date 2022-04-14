package com.tiger.jdbc;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SqlParser
 * @date 2022/4/7 16:51
 * @description
 */
public class SqlParser {

    public static void main(String[] args) {

        String sql = "-- -----------------------------------------------------------------------------\n" +
                "-- 功能：生成区域表\n" +
                "-- 作者：Steven.liu\n" +
                "-- 参数：无\n" +
                "-- 时间：2022-03-25\n" +
                "-- NAME: dw_edw.p_dim_area.sql\n" +
                "-- -----------------------------------------------------------------------------\n" +
                "-- 修改         时间        功能\n" +
                "-- -----------------------------------------------------------------------------\n" +
                "-- 修改  district_code   字段类型  INT\n" +
                "-- -----------------------------------------------------------------------------\n" +
                "-- DROP   TABLE IF EXISTS dw_edw.dim_area;\n" +
                "-- CREATE TABLE dw_edw.dim_area AS\n" +
                "-- 生成区域表区域数据\n" +
                "-- district_code在业务库arears表，本身就是业务主键，是唯一值\n" +
                "-- @@ 截断表，插入数据，\n" +
                "TRUNCATE TABLE dw_edw.dim_area   ;\n" +
                "INSERT INTO \t  dw_edw.dim_area\n" +
                "SELECT \tCAST(district_code AS INT)\t\t\t\t\t            AS district_code\t--hello\n" +
                "\t\t,district_name                                  \t\tAS district_name \n" +
                "\t\t,CAST(city_code AS INT)                                 AS city_code     \n" +
                "\t\t,city_name                                      \t\tAS city_name     \n" +
                "\t\t,city_level                                     \t\tAS city_level    \n" +
                "\t\t,CASE WHEN city_level=1   THEN '一线城市'\n" +
                "\t\t\t  WHEN city_level=2   THEN '二线城市'\n" +
                "\t\t\t  WHEN city_level=2.1 THEN '二.一线城市'\n" +
                "\t\t\t  WHEN city_level=2.2 THEN '二.二线城市'\n" +
                "\t\t\t  WHEN city_level=3   THEN '三线城市'\n" +
                "\t\t\t  WHEN city_level=4   THEN '四线城市'\n" +
                "\t\t\t  WHEN city_level=5   THEN '五线城市'\n" +
                "\t\t\t  WHEN city_level=6   THEN '六线城市'\n" +
                "\t\t ELSE CONCAT(CAST(city_level AS STRING),'城市') END \tAS city_level_desc\n" +
                "\t\t,CAST(province_code AS INT)                             AS province_code\n" +
                "\t\t,province_name                                  \t\tAS province_name\n" +
                "\t\t,CASE \tWHEN SUBSTR(province_name,1, 6) IN ('上海','江苏','江西','浙江','湖北','湖南') \t\t\t\t\tTHEN '东'\n" +
                "\t\t\t\tWHEN SUBSTR(province_name,1, 6) IN ('云南','广东','广西','海南','福建','贵州','四川','重庆') \tTHEN '南'\n" +
                "\t\t\t\tWHEN SUBSTR(province_name,1, 6) IN ('青海','新疆','内蒙','西藏') \t\t\t\t\t\t\t\tTHEN '西' \n" +
                "\t\t\t\tWHEN SUBSTR(province_name,1, 6) IN ('北京','吉林','天津','山东','河北','辽宁','黑龙') \t\t\tTHEN '北'\n" +
                "\t\t\t\tWHEN SUBSTR(province_name,1, 6) IN ('宁夏','安徽','山西','河南','甘肃','陕西') \t\t\t\t\tTHEN '中'\n" +
                "\t\t ELSE '未知' END \t\t\t\t\t\t\t\t\t\tAS area_position\n" +
                "\t\t,CURRENT_TIMESTAMP()\t  \t\t\t\t\t\t\t\tAS etl_load_at\n" +
                "FROM  dw_std.areas a";

        // 格式化输出, 缺省大写格式
        String result = SQLUtils.format(sql, DbType.postgresql);
        System.out.println(result);

        // 解析出的独立语句的个数
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, DbType.postgresql);
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);
            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            stmt.accept(visitor);
            Set<TableStat.Column> groupby_col = visitor.getGroupByColumns();
            //
            for (Iterator iterator = groupby_col.iterator(); iterator.hasNext();) {
                TableStat.Column column = (TableStat.Column)iterator.next();
                System.out.println("[GROUP]" + column.toString());
            }
            // 获取表名称
            System.out.println("table names:");
            Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
            for (Iterator iterator = tabmap.keySet().iterator(); iterator.hasNext();) {
                TableStat.Name name = (TableStat.Name)iterator.next();
                System.out.println(name.toString() + " - " + tabmap.get(name).toString());
            }
            // System.out.println("Tables : " + visitor.getCurrentTable());
            // 获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            // 获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }

    }

//    @Test
//    public void json() {
//        String json = "{\n" +
//                "                \"processDefinitionId\":4083,\n" +
//                "                \"autoUpdate\":false,\n" +
//                "                \"referenceType\":1\n" +
//                "            }";
//
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        String processDefinitionId = jsonObject.getString("processDefinitionId");
//        System.out.println(processDefinitionId);
//    }

}
