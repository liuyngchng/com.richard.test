package richard.test.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuratorPublisher implements CuratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorPublisher.class);

    //创建连接实例
    private static CuratorFramework client = null;

    public static void main(String[] args) throws Exception {

        //创建 CuratorFrameworkImpl实例
        client = CuratorFrameworkFactory.newClient(CuratorPublisher.SERVER, SESSION_TIMEOUT, CONNECTION_TIMEOUT, retryPolicy);

        //启动
        client.start();
//        try {
//            //创建永久节点
//            client.create().forPath("/curator","/curator data".getBytes());
//        } catch (Exception ex) {
//            LOGGER.error("error", ex);
//        }
//
//
//        for (int i = 0; i < 2; i++) {
//            //创建永久有序节点\
//            client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
//                    .forPath("/curator_sequential","/curator_sequential data".getBytes());
//
//        }

        //创建临时节点
//        client.create().withMode(CreateMode.EPHEMERAL)
//                .forPath("/curator/ephemeral","/curator/ephemeral data".getBytes());

        client.create().withMode(CreateMode.PERSISTENT)
                .forPath("/service", "service root".getBytes());
        for (int i = 0; i < 2; i++) {
            String ip = "192.168.100.1" + i;
            String data = "{\"ip\":\""+ ip +"\"}";
            //创建临时有序节点
            client.create().withMode(CreateMode.EPHEMERAL)
                    .forPath("/service/serviceProvider" + i, data.getBytes());
        }


//        client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
//                .forPath("/curator/ephemeral_path2","/curator/ephemeral_path2 data".getBytes());
        client.close();
        LOGGER.info("client closed.");

    }
}
