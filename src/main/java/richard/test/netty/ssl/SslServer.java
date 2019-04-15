package richard.test.netty.ssl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * keytool -genkey -alias securechat -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass sNetty -storepass sNetty -keystore sChat.jks

 keytool -export -alias securechat -keystore sChat.jks -storepass sNetty -file sChat.cer
 存储在文件 <sChat.cer> 中的证书

 keytool -genkey -alias smcc -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass cNetty -storepass cNetty -keystore cChat.jks
 keytool -import -trustcacerts -alias securechat -file sChat.cer -storepass cNetty -keystore cChat.jks
 */
public class SslServer {
    static final Logger LOGGER = LoggerFactory.getLogger(SslServer.class);
    private static int WORKER_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    private static Class<? extends ServerChannel> channelClass;

    public static void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
            .childOption(ChannelOption.SO_RCVBUF, 1048576)
            .childOption(ChannelOption.SO_SNDBUF, 1048576);

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(WORKER_GROUP_SIZE);
        channelClass = NioServerSocketChannel.class;
        LOGGER.info("workerGroup size:" + WORKER_GROUP_SIZE);
        LOGGER.info("preparing to start server...");
        b.group(bossGroup, workerGroup);
        b.channel(channelClass);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("/Users/richard/work/study/server/sChat.jks"), "sNetty".toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore,"sNetty".toCharArray());
        SslContext sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
        b.childHandler(new SslChannelInitializer(sslContext, false));
        b.bind(9912).sync();
        LOGGER.info("server start success, listening on port " + 9912 + ".");
    }

    public static void main(String[] args) throws Exception {
        SslServer.start();
    }

    public static void shutdown() {
        LOGGER.debug("preparing to shutdown ssl server...");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        LOGGER.debug("ssl server is shutdown.");
    }
}