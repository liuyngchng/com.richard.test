package richard.test.protobuf.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerProtoHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProtoHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageProto.msg message = (MessageProto.msg) msg;
//        if (ConnectionPool.getChannel(message.getId()) == null) {
//            ConnectionPool.putChannel(message.getId(), ctx);
//        }
        System.err.println("server:" + message.getId());
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}