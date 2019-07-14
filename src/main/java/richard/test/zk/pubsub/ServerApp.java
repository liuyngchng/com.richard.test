package richard.test.zk.pubsub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;


/**
 *
 */
public class ServerApp {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
            "127.0.0.1:2182",
            new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        ServiceRegistry serviceRegistry = new ServiceRegistry(client,"services");
        ServiceInstance<InstanceDetails> instance1 = ServiceInstance.<InstanceDetails>builder()
            .name("service1")
            .port(12345)
            .address("192.168.1.100")   //address不写的话，会取本地ip
            .id("192.168.1.100:12345")
            .payload(
                new InstanceDetails(
                    "192.168.1.100:12345/Test.Service1",
                    "192.168.1.100",
                    12345,
                    "Test.Service1"
                )
            )
            .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
            .build();
        ServiceInstance<InstanceDetails> instance2 = ServiceInstance.<InstanceDetails>builder()
            .name("service1")
            .port(12345)
            .address("192.168.1.101")
            .id("192.168.1.101:12345")
            .payload(
                new InstanceDetails(
                    "192.168.1.101:12345/Test.Service1",
                    "192.168.1.101",
                    12345,
                    "Test.Service1"
                )
            )
            .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
            .build();
        serviceRegistry.registerService(instance1);
        serviceRegistry.registerService(instance2);


        Thread.sleep(Integer.MAX_VALUE);
    }
}