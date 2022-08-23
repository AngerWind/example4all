package com.tiger.example;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

public class Client {

    public static void main(String[] args) {
        try {
            // 定义transport层
            TTransport transport = new TFramedTransport(new TSocket("localhost", 8888), 600);
            // 定义protocol层
            TProtocol protocol = new TCompactProtocol(transport);
            // 创建client
            StudentService.Client client = new StudentService.Client(protocol);
            // 启动服务
            transport.open();

            try {
                Student student = client.getStudentByIdAndName(1008);
            } catch (TException e) {
                e.printStackTrace();
            }
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
