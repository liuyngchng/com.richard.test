package richard.test.netty.ssl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.text.MessageFormat;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslClient {
    static final Logger LOGGER = LoggerFactory.getLogger(SslClient.class);

    public static void main(String[] args) {
        Channel channel = SslClient.createChannel("localhost",9912);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.error("error", e);
        }
        ChanelUtil.writeMessage(channel, "ssh over tcp test 1");
        ChanelUtil.writeMessage(channel, "ssh over tcp test 2");
        ChanelUtil.writeMessage(channel, "ssh over tcp test 3");
        ChanelUtil.writeMessage(channel, "ssh over tcp test 4");
        ChanelUtil.writeMessage(channel, "ssh over tcp test 5");
    }

    public static Channel createChannel(String host, int port) {
        Channel channel = null;
        Bootstrap b = getBootstrap();
        try {
            channel = b.connect(host, port).sync().channel();
            LOGGER.info(MessageFormat.format("connect to server ({0}:{1,number,#}) success for thread [" + Thread.currentThread().getName() + "].", host,port));
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
        return channel;
    }

    public static Bootstrap getBootstrap(){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.SO_REUSEADDR, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        TrustManagerFactory tf;
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("/Users/richard/work/study/server/cChat.jks"), "cNetty".toCharArray());
            tf = TrustManagerFactory.getInstance("SunX509");
            tf.init(keyStore);
            SslContext sslContext = SslContextBuilder.forClient().trustManager(tf).build();
            b.handler(new SslChannelInitializer(sslContext, true));
            return b;
        } catch(Exception e) {
            LOGGER.error("error", e);
        }
        return null;
    }
}
