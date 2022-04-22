package com.tiger.jdbc;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title XXX
 * @date 2022/4/21 14:27
 * @description
 */
public class XXX {

    @SneakyThrows
    @Test
    public void test() {
        File root = new File("C:\\Users\\Tiger.Shen\\Desktop\\hive转kudu");
        File parseRoot = new File("C:\\Users\\Tiger.Shen\\Desktop\\hive转kudu\\kudu建表");
        if (!parseRoot.exists()) {
            parseRoot.mkdir();
        } else {
            Arrays.stream(parseRoot.listFiles()).forEach(File::delete);
        }
        File[] files = root.listFiles();
        File metaDirector = null;
        for (File file : files) {
            if (file.getName().equals("表结构元信息")) {
                metaDirector = file;
            }
        }
        File[] sqlFiles = metaDirector.listFiles();
        for (File sqlFile : sqlFiles){
            FileReader fileReader = new FileReader(sqlFile);
            char[] buffer = new char[1024];
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while ((i = fileReader.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, i);
            }
            fileReader.close();
            String sqlTest = sb.toString();

            File hasIdFile = new File(parseRoot.getAbsolutePath() + "\\" + sqlFile.getName().replace("sql", "has_id.sql"));
            if (!hasIdFile.exists()) {
                hasIdFile.createNewFile();
            }
            FileWriter hasIdFileWriter = new FileWriter(hasIdFile);

            File noIdFile = new File(parseRoot.getAbsolutePath() + "\\" + sqlFile.getName().replace("sql", "no_id.sql"));
            if (!noIdFile.exists()) {
                noIdFile.createNewFile();
            }
            FileWriter noIdFileWriter = new FileWriter(noIdFile);


            String[] split = sqlTest.split("\\n\\s*\\n");

            List<String> sqlList = Arrays.stream(split).map(sql -> {
                return sql.split("\\n\\)\\n")[0];
            }).collect(Collectors.toList());

            for (String sql : sqlList) {
                sql = sql.replace("DROP TABLE", "DROP TABLE IF EXISTS");
                sql = sql.replace("VARCHAR(255)", "string");

                if (sql.split("\n")[2].trim().startsWith("id ")) {
                    sql = sql + ", \n  primary key (`id`) \n)\npartition by hash partitions 4\nstored as kudu\nTBLPROPERTIES ('kudu.num_tablet_replicas' = '1');\n\n";
                    hasIdFileWriter.write(sql);
                } else {
                    sql += ")\npartition by hash partitions 4\nstored as kudu\nTBLPROPERTIES ('kudu.num_tablet_replicas' = '1');\n\n";
                    noIdFileWriter.write(sql);
                }
            }
            hasIdFileWriter.flush();
            noIdFileWriter.flush();

            hasIdFileWriter.close();
            noIdFileWriter.close();
        }

    }


    @SneakyThrows
    @Test
    public void test1() {
        String msg = "DROP TABLE dm_boss.rpt_portfolio_deck_1;\n" +
                "CREATE TABLE dm_boss.rpt_portfolio_deck_1 (\n" +
                "  stat_date STRING,\n" +
                "  undue_prin_amt DECIMAL(38,4),\n" +
                "  undue_prin_writeoff_amt DECIMAL(38,4),\n" +
                "  l_loan_cnt BIGINT,\n" +
                "  l_loan_amt DECIMAL(38,4),\n" +
                "  m1_amt DECIMAL(38,4),\n" +
                "  m2_amt DECIMAL(38,4),\n" +
                "  m3_amt DECIMAL(38,4),\n" +
                "  m3m_amt DECIMAL(38,4),\n" +
                "  aply_cnt BIGINT,\n" +
                "  risk_aprv_adopt_cnt BIGINT,\n" +
                "  risk_aprv_adopt_amt DECIMAL(38,4),\n" +
                "  risk_aprv_cnt BIGINT,\n" +
                "  risk_aprv_amt DECIMAL(38,4),\n" +
                "  fdp4_amt_ratio DECIMAL(38,6),\n" +
                "  fdp4_cnt_ratio DOUBLE,\n" +
                "  dpd30_jiemian DECIMAL(38,6),\n" +
                "  dpd30_jiemian_mob3 DECIMAL(38,6),\n" +
                "  load_time TIMESTAMP\n" +
                ")\n" +
                "STORED AS TEXTFILE\n" +
                "LOCATION 'hdfs://nameservice6/user/hive/warehouse/dm_boss.db/rpt_portfolio_deck_1'\n" +
                "TBLPROPERTIES ('STATS_GENERATED'='TASK', 'impala.lastComputeStatsTime'='1647121509', 'numRows'='93', 'totalSize'='21190')";
        String[] split = msg.split("^\\)");
        System.out.println("hello");
    }


}
