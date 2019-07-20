package richard.test.zk.pubsub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


/**
 *
 */
public class ServerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
            "127.0.0.1:2181",
            new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        ServiceRegistry serviceRegistry = new ServiceRegistry(client,"services");
        ServiceInstance<InstanceDetails> instance1 = ServiceInstance.<InstanceDetails>builder()
            .name("richard.test.rpc.HelloService")
            .port(12345)
            .address("127.0.0.1")   //address不写的话，会取本地ip
            .id("127.0.0.1:12345")
            .payload(
                new InstanceDetails(
                    UUID.randomUUID().toString(),
                    "192.168.1.100",
                    12345,
                    "richard.test.rpc.HelloService"
                )
            )
            .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
            .build();
        ServiceInstance<InstanceDetails> instance2 = ServiceInstance.<InstanceDetails>builder()
            .name("richard.test.rpc.HelloService")
            .port(56789)
            .address("127.0.0.1")
            .id("127.0.0.1:56789")
            .payload(
                new InstanceDetails(
                    UUID.randomUUID().toString(),
                    "192.168.1.101",
                    12345,
                    "richard.test.rpc.HelloService"
                )
            )
            .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
            .build();
        serviceRegistry.registerService(instance1);
        serviceRegistry.registerService(instance2);

        LOGGER.info("service published.");
        Thread.sleep(Integer.MAX_VALUE);
    }
}