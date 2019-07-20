package richard.test.zk.pubsub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApp.class);

    public static void main(String[] args) throws Exception {
        CuratorFramework client =
            CuratorFrameworkFactory.newClient(
                "127.0.0.1:2182",
                new ExponentialBackoffRetry(1000, 3)
            );
        client.start();
        ServiceDiscoverer serviceDiscoverer = new ServiceDiscoverer(client,"services");

        ServiceInstance<InstanceDetails> instance1 = serviceDiscoverer.getInstanceByName("service1");

        LOGGER.info("uri is {}", instance1.buildUriSpec());
        LOGGER.info("payload is {}", instance1.getPayload());

        ServiceInstance<InstanceDetails> instance2 = serviceDiscoverer.getInstanceByName("service2");

        LOGGER.info("uri is {}", instance2.buildUriSpec());
        LOGGER.info("payload is {}", instance2.getPayload());

        serviceDiscoverer.close();
        CloseableUtils.closeQuietly(client);
    }
}