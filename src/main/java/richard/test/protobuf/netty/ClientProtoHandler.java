package richard.test.protobuf.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProtoHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProtoHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageProto.msg message = (MessageProto.msg) msg;
        LOGGER.info("client:" + message.getContent());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
