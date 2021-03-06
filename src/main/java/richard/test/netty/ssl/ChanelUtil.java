package richard.test.netty.ssl;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class ChanelUtil {
    static final Logger LOGGER = LoggerFactory.getLogger(ChanelUtil.class);

    public static ChannelFuture writeMessage(Channel channel,String msg) {
        if (channel== null) {
            LOGGER.error("send msg failed, channel is disconnected or not connect. channel is null, please see caller log.");
            return null;
        }
        try {
            return channel.writeAndFlush(msg).sync();
        } catch (Exception e) {
            LOGGER.error("error", e);
            String otherInfo;
            if(channel.remoteAddress() == null) {
                otherInfo = "channel is null.";
            } else {
                otherInfo = "remote address [" + ((InetSocketAddress)channel.remoteAddress()).toString() + "]";
            }
            if (e instanceof ClosedChannelException) {
                LOGGER.error("channel to " + otherInfo + " is closed",e);
            } else {
                LOGGER.error("timeout occurred during channel send msg, " + otherInfo,e);
            }
        }

        return null;
    }

    public static ChannelFuture writeMessage(Channel channel,ByteBuf msg) {
        if (channel == null) {
            LOGGER.error("send msg failed, channel is disconnected or not connect. channel is null, please see caller log.");
            return null;
        }
        try {
            return channel.writeAndFlush(msg).sync();
        } catch (Exception e) {
            LOGGER.error("timeout occured during channel send msg. remote address is:" + ((InetSocketAddress)channel.remoteAddress()).toString(),e);
        }
        return null;
    }
}