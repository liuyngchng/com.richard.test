package richard.test.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CuratorSubscriber implements CuratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorPublisher.class);

    //创建连接实例
    private static CuratorFramework client = null;

    public static void main(String[] args) throws Exception {
        //创建 CuratorFrameworkImpl实例
        client = CuratorFrameworkFactory.newClient(CuratorPublisher.SERVER, SESSION_TIMEOUT, CONNECTION_TIMEOUT, retryPolicy);
        String servicePath = "/curator/service";
        Stat stat1 = client.checkExists().forPath(servicePath);
        LOGGER.info("{} 是否存在: {}", servicePath, stat1 != null ? true : false);
        List<String> providerList = client.getChildren().forPath(servicePath);

        providerList.forEach(item -> {
            try {
                byte[] data = client.getData().forPath(item);
                LOGGER.info("provider ip is {}", new String(data));
            } catch (Exception ex) {
                LOGGER.error("error", ex);
            }

        });


    }
}
