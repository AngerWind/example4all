package org.example;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {

        List<VirtualMachineDescriptor> listBefore = VirtualMachine.list();
        // agentmain()方法所在jar包
        String jar =
            "C:\\Users\\Administrator\\Desktop\\demo\\example4javaagent\\agent-intrumentation\\target\\agent-intrumentation-1.0-SNAPSHOT-jar-with-dependencies.jar";

        for (VirtualMachineDescriptor virtualMachineDescriptor : VirtualMachine.list()) {
            // 针对指定名称的JVM实例
            if (virtualMachineDescriptor.displayName().equals("com.tiger.AgentMainDemo")) {
                System.out
                    .println("将对该进程的vm进行增强：org.example.agent.AgentTest的vm进程, pid=" + virtualMachineDescriptor.id());
                // attach到新JVM
                VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor);
                // 加载agentmain所在的jar包, 堵塞直到agentmain()方法执行完毕
                vm.loadAgent(jar);
                // detach
                vm.detach();
            }
        }
    }

}
