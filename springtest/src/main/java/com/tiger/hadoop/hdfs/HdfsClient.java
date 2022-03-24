package com.tiger.hadoop.hdfs;

import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.AclStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * @author Shen
 * @version v1.0
 * @Title HdfsClient
 * @date 2022/2/20 20:32
 * @description
 */
public class HdfsClient {

    public FileSystem fs;

    @Before
    @SneakyThrows
    public void init(){
        Configuration conf = new Configuration();
        // 设置默认的文件副本
        // 默认的配置优先级是 代码 >> 当前项目resource下的xxx-site.xml >> 集群中的xxx-site.xml配置文件 >> 集群中的xxx-default.xml
        conf.set("dfs.replication", "1");

        // 客户端操作hdfs时,默认使用的是当前系统登录的用户,有可能导致权限不足
        // 可以直接代码设置用户, 也可以设置系统环境变量或者jvm系统变量HADOOP_USER_NAME
        fs = FileSystem.get(new URI("hdfs://hadoop:8082"), conf, "tiger");
    }

    @After
    @SneakyThrows
    public void close() {
        fs.close();
    }

    @Test
    @SneakyThrows
    public void mkdir() {
        boolean mkdirs = fs.mkdirs(new Path("/sanguo"));
        System.out.println(mkdirs);
    }

    @Test
    @SneakyThrows
    public void getAcl() {
        AclStatus aclStatus = fs.getAclStatus(new Path("/"));
        System.out.println(aclStatus.getPermission().toString());

    }

    @Test
    @SneakyThrows
    public void put() {
        // 方式1
        fs.copyFromLocalFile(false, true, new Path("C:\\Users\\Tiger.Shen\\Desktop\\问题.md"),
                new Path("/xiyou/"));

        // 方式2
        // FSDataOutputStream fos = fs.create(new Path("/input"));
        // fos.write("hello world".getBytes());
    }

    @Test
    @SneakyThrows
    public void get() {
        // 参数的解读：参数一：原文件是否删除；参数二：原文件路径HDFS； 参数三：目标地址路径Win
        // 参数四：false的话会同时下载的文件的crc校验文件到文件夹中
        fs.copyToLocalFile(false, new Path("/xiyou/settings.txt"), new Path("D:\\"), true);
    }

    // 删除
    @Test
    public void testRm() throws IOException {

        // 参数解读：参数1：要删除的路径； 参数2 ： 是否递归删除
        // 删除文件
        //fs.delete(new Path("/jdk-8u212-linux-x64.tar.gz"),false);

        // 删除空目录
        //fs.delete(new Path("/xiyou"), false);

        // 删除非空目录
        fs.delete(new Path("/jinguo"), true);
    }

    // 文件的更名和移动
    @Test
    public void testmv() throws IOException {
        // 参数解读：参数1 ：原文件路径； 参数2 ：目标文件路径
        // 对文件名称的修改
        //fs.rename(new Path("/input/word.txt"), new Path("/input/ss.txt"));

        // 文件的移动和更名
        //fs.rename(new Path("/input/ss.txt"),new Path("/cls.txt"));

        // 目录更名
        fs.rename(new Path("/input"), new Path("/output"));

    }

    // 获取文件详细信息
    @Test
    public void fileDetail() throws IOException {

        // 获取所有文件信息
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        // 遍历文件
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();

            System.out.println("==========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();

            System.out.println(Arrays.toString(blockLocations));

        }
    }

    // 判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {

        FileStatus[] listStatus = fs.listStatus(new Path("/"));

        for (FileStatus status : listStatus) {

            if (status.isFile()) {
                System.out.println("文件：" + status.getPath().getName());
            } else {
                System.out.println("目录：" + status.getPath().getName());
            }
        }
    }
}
