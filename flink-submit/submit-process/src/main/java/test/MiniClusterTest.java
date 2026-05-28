package test;

import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.MiniClusterClient;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.configuration.Configuration;

public class MiniClusterTest {

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        MiniClusterConfiguration miniClusterConfig = new MiniClusterConfiguration.Builder()
            .setConfiguration(config)
            .setNumTaskManagers(1)
            .setNumSlotsPerTaskManager(2)
            .build();

        MiniCluster miniCluster = new MiniCluster(miniClusterConfig);
        miniCluster.start();

        ClusterClient<MiniClusterClient.MiniClusterId> clusterClient = new MiniClusterClient(config, miniCluster);

    }
}
