package com.tiger.sql;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SqlTest
 * @date 2021/8/12 19:36
 * @description
 */

/**
 * CREATE TABLE `dag_project` (
 *   `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
 *   `app_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '应用id',
 *   `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用id',
 *   `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目名称',
 *   `code` bigint DEFAULT NULL COMMENT '唯一编码(由雪花算法生成)',
 *   `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目描述',
 *   `user_id` int DEFAULT NULL COMMENT '所属用户',
 *   `type` tinyint NOT NULL DEFAULT '0' COMMENT '0.离线项目，1实时项目',
 *   `flag` tinyint DEFAULT '1' COMMENT '是否可用  1 可用,0 不可用',
 *   `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 *   `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='escheduler项目表';
 */

/**
 * CREATE TABLE `dag_process_definition` (
 *   `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
 *   `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用id',
 *   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程定义名称',
 *   `code` bigint DEFAULT NULL COMMENT '唯一编码(由雪花算法生成)',
 *   `version` int DEFAULT NULL COMMENT '流程定义版本',
 *   `release_state` tinyint DEFAULT NULL COMMENT '流程定义的发布状态：0 未上线  1已上线',
 *   `project_id` int DEFAULT NULL COMMENT '项目id',
 *   `user_id` int DEFAULT NULL COMMENT '流程定义所属用户id',
 *   `process_definition_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '流程定义json串',
 *   `process_definition_json_template` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
 *   `desc` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '流程定义描述',
 *   `global_params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '全局参数',
 *   `flag` tinyint DEFAULT NULL COMMENT '流程是否可用\r\n：0 不可用\r\n，1 可用',
 *   `locations` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '节点坐标信息',
 *   `connects` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '节点连线信息',
 *   `receivers` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '收件人',
 *   `receivers_cc` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '抄送人',
 *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 *   `timeout` int DEFAULT '0' COMMENT '超时时间',
 *   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
 *   `project_code` bigint DEFAULT NULL COMMENT 'project code',
 *   `is_major` tinyint(1) DEFAULT '1' COMMENT '是否主定义流程: 1=是 0=不是',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=32087 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='escheduler流程定义表';
 */

/**
 * CREATE TABLE `dag_process_instance` (
 *   `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
 *   `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '应用id',
 *   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程实例名称',
 *   `process_definition_id` int DEFAULT NULL COMMENT '流程定义id',
 *   `state` tinyint DEFAULT NULL COMMENT '流程实例状态：0 提交成功,1 正在运行,2 准备暂停,3 暂停,4 准备停止,5 停止,6 失败,7 成功,8 需要容错,9 kill,10
 *   等待线程,11 等待依赖完成',
 *   `start_time` datetime DEFAULT NULL COMMENT '流程实例开始时间',
 *   `end_time` datetime DEFAULT NULL COMMENT '流程实例结束时间',
 *   `run_times` int DEFAULT NULL COMMENT '流程实例运行次数',
 *   `command_type` tinyint DEFAULT NULL COMMENT '命令类型：0 启动工作流,1 从当前节点开始执行,2 恢复被容错的工作流,3 恢复暂停流程,4 从失败节点开始执行,5 补数,6
 *   调度,7 重跑,8 暂停,9 停止,10 恢复等待线程',
 *   `global_params` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '全局参数（固化流程定义的参数）',
 *   `flag` tinyint DEFAULT '1' COMMENT '是否可用，1 可用，0不可用',
 *   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 *   `is_sub_process` int DEFAULT '0' COMMENT '是否是子工作流 1 是，0 不是',
 *   `timeout` int DEFAULT '0' COMMENT '超时时间',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=2271133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='escheduler流程实例表';
 */

public class SqlTest {

    private static final Random RANDOM = new Random();
    private static Connection connection;

    @Before
    public void before() {
        String url =
            "jdbc:mysql://localhost:3306/tenma_test?useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true&generateSimpleParameterMetadata=true&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "871403165";
        try {
            connection = (Connection)DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static final MessageFormat MESSAGE_FORMAT = new MessageFormat(
        "(''{0}'', ''{1}'', {2, number,#}, ''{3}'', {4},{5}, {6}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");


    @Test
    public void insertProject() throws Exception {


        Statement statement = connection.createStatement();

        StringBuilder sb = new StringBuilder(500000);
        sb.append("INSERT INTO dag_project(app_id, name, code, `desc`, user_id, `type`, flag, create_time, update_time) VALUES\n");

        for (int i = 0; i < 100; i++) {
            sb.append(
                MESSAGE_FORMAT.format(new Object[] {appId(), name(), code(), desc(), userId(), type(), projectFlag()}));
            if (i == 99) {
                sb.append(";");
            } else {
                sb.append(",");
            }
        }
        System.out.println(sb.toString());
        statement.executeUpdate(sb.toString());
        statement.close();
    }

    @After
    public void after() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // try {
        //     System.out.println(OBJECT_MAPPER.writeValueAsString(projectCode));
        // } catch (JsonProcessingException e) {
        //     e.printStackTrace();
        // }
    }

    @SneakyThrows
    public void insertDefinition() {

        final MessageFormat messageFormat = new MessageFormat("(''{0}'', ''{1}'', {2, number, ####################}, 0, {3}, {4}, {5}, ''{6}'', " +
                "{7}, ''{8}'', ''{9}'', 0, {10})");
        Statement statement = connection.createStatement();
        for (int j = 0; j < 10; j++) {
            StringBuilder sb = new StringBuilder(500000);
            sb.append("INSERT INTO definition" +
                    "(app_id, name, code, version, release_state, project_id, user_id, " +
                    "global_params, flag, locations, connects," +
                    "timeout, is_major) VALUES");
            for (int i = 0; i < 1000; i++) {
                sb.append(
                        messageFormat.format(new Object[] {appId(), name(), code(), releaseState(),
                                random100()+1, random100()+1, // project_id, userid
                                randomAlphanumeric(), // global_param
                                projectFlag(), // flag
                                randomAlphanumeric(), // location
                                randomAlphanumeric(), // connects
                                major()
                        }));
                if (i == 999) {
                    sb.append(";");
                } else {
                    sb.append(",");
                }
            }
            statement.executeUpdate(sb.toString());
        }
        statement.close();

    }

    @SneakyThrows
    @Test
    public void insertInstance() {

        final MessageFormat messageFormat = new MessageFormat("(''{0}'', ''{1}'', " +
                "{2, number, ###################}, {3}, ''{4, date, yyyy-MM-dd HH:mm:ss.SSS }'', ''{5, date, yyyy-MM-dd HH:mm:ss.SSS}''," +
                "{6}, {7}, ''{8}'', {9}, {10}, {11})");
        Statement statement = connection.createStatement();
        for (int j = 0,  count = 0; j < 1000; j++) {
            StringBuilder sb = new StringBuilder(500000);
            sb.append("INSERT INTO instance\n" +
                    "(app_id, name, process_definition_id, state, start_time, end_time, run_times, command_type, " +
                    "global_params, flag, is_sub_process, timeout) VALUES");
            for (int i = 0; i < 2000; i++) {
                Date[] dates = startEndTime();
                sb.append(
                        messageFormat.format(new Object[] {appId(), name(), RANDOM.nextInt(10000)+1,
                                state(),
                                dates[0], dates[1],
                                RANDOM.nextInt(5) + 1, //run times
                                RANDOM.nextInt(11), // command types
                                randomAlphanumeric(), // global params
                                projectFlag(), // flags
                                subProcess(), RANDOM.nextInt(6)
                        }));
                if (i == 1999) {
                    sb.append(";");
                } else {
                    sb.append(",");
                }

            }
            count += 2000;
            System.out.println(count);
            statement.executeUpdate(sb.toString());
        }
        statement.close();
    }

    @Test
    public void insertDefAndIns(){
        insertDefinition();
        System.out.println("end definition");
        insertInstance();
    }

    private static String appId() {
        int i = RANDOM.nextInt(100);
        if (i < 80) {
            return "welab-skyscanner-tenma";
        } else {
            return "welab-skyscanner-xanfer";
        }
    }


    public static Integer subProcess(){
        int i = RANDOM.nextInt(11);
        if (i < 2) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Integer major(){
        int i = RANDOM.nextInt(10);
        if (i < 4) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Integer releaseState(){
        int releaseStatus =  RANDOM.nextInt(10);
        if (releaseStatus < 2) {
            return 0;
        } else {
            return 1;
        }
    }

    private static String name() {
        return RandomStringUtils.random(8, 0x4e00, 0x9fa5, false, false);
    }

    private static String randomAlphanumeric(){
        return RandomStringUtils.randomAlphanumeric(20);
    }

    private static Long code() {
        Long code = IdUtil.getSnowflake().nextId();
        return code;
    }

    private static Integer userId() {
        return RANDOM.nextInt(100);
    }

    private static Integer random100() {
        return RANDOM.nextInt(100);
    }

    private static Integer state() {
        int i = RANDOM.nextInt(50);
        if (i < 10) {
            return i;
        } else {
            return 11;
        }
    }

    private static Integer projectFlag() {
        int i = RANDOM.nextInt(10);
        if (i < 2) {
            return 0;
        } else {
            return 1;
        }
    }

    private static String desc() {
        return ".";
    }

    // 0离线, 1实时
    private static Integer type() {
        int i = RANDOM.nextInt(10);
        if (i < 2) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Date[] startEndTime(){


        long time1 = (long)(RANDOM.nextInt(31536000) + 1597567699) * 1000;
        long time2 = (long)(RANDOM.nextInt(31536) + 1597567699) * 1000;

        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        Date[] dates = new Date[2];
        if (time1 >= time2) {
            dates[0] = date2;
            dates[1] = date1;
        } else {
            dates[0] = date1;
            dates[1] = date2;
        }
        return dates;
    }

    public static void main(String[] args) {
        System.out.println(MessageFormat.format("({0,  date, yyyy-MM-dd HH:mm:ss.SSS})", startEndTime()[0]));
    }
}
