package com.tiger.example;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;


public class Server {

    public static void main(String[] args) {
        try {
            TNonblockingServerSocket tNonblockingServerSocket = new TNonblockingServerSocket(8888);
            StudentService.Processor<StudentServiceImpl> processor = new StudentService.Processor<>(new StudentServiceImpl());

            // 通常通过对应Server的Args对象来创建Server
            THsHaServer.Args arg = new THsHaServer.Args(tNonblockingServerSocket)
                .minWorkerThreads(2)
                .maxWorkerThreads(4)
                // 默认情况下选择TBinaryProtocol
                .protocolFactory(new TCompactProtocol.Factory())
                .transportFactory(new TFramedTransport.Factory())
                // 指定Processor
                .processor(processor);
            TServer server = new THsHaServer(arg);
            server.serve();

            // server.stop()
        } catch (TTransportException e) {
            e.printStackTrace();
        }

    }


    @org.junit.Test
    public void simpleServer() {
        try {
            // TServerSocket是所有网络传输Protocol的基类
            TServerSocket serverSocket = new TServerSocket(8888);
            StudentService.Processor<StudentServiceImpl> processor = new StudentService.Processor<>(new StudentServiceImpl());

            // 堵塞式单线程模型, 一次只能处理一个请求
            TSimpleServer tSimpleServer = new TSimpleServer(new TServer.Args(serverSocket).processor(processor));

            tSimpleServer.serve();

            // server.stop()
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
