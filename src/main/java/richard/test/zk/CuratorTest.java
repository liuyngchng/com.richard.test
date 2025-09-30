package richard.test.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public interface CuratorTest {

    //会话超时时间
    int SESSION_TIMEOUT = 30 * 1000;

    //连接超时时间
    int CONNECTION_TIMEOUT = 3 * 1000;

    //ZooKeeper服务地址
    //String SERVER = "192.168.1.159:2100,192.168.1.159:2101,192.168.1.159:2102";
    String SERVER = "localhost:2182";


    /**
     * baseSleepTimeMs：初始的重试等待时间
     * maxRetries：最多重试次数
     */
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

}
