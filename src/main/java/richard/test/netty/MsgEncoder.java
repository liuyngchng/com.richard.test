package richard.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by richard on 18/04/2019.
 */
@ChannelHandler.Sharable
public class MsgEncoder extends MessageToByteEncoder<MyMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyMsg message, ByteBuf out) throws Exception {

        // 将Message转换成二进制数据
        MsgHeader header = message.getMsgHeader();

        // 这里写入的顺序就是协议的顺序.

        // 写入Header信息
        out.writeInt(header.getVersion());
        out.writeInt(message.getContent().length());
        out.writeBytes(header.getSessionId().getBytes());

        // 写入消息主体信息
        out.writeBytes(message.getContent().getBytes());
    }
}


