package richard.test.protobuf.netty;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * IM 客户端启动入口
 * @author yinjihuan
 */
public class ClientBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBootstrap.class);
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8899;
        Channel channel = new MyConnection().connect(host, port);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        // protobuf
        MessageProto.msg message = MessageProto.msg.newBuilder().setId(id).setContent("hello, where are you?").build();
        channel.writeAndFlush(message);
    }
}
