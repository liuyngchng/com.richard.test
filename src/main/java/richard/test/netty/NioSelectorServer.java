package richard.test.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by richard on 01/04/2019.
 */
public class NioSelectorServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioSelectorServer.class);

    public static void main(String[] args) throws IOException {
        // 创建一个selector选择器
        Selector selector = Selector.open();
        // 打开一个通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        // 绑定到9000端口
        socketChannel.socket().bind(new InetSocketAddress(8000));
        // 使设定non-blocking的方式。
        socketChannel.configureBlocking(false);
        // 向Selector注册Channel及我们有兴趣的事件
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        for (;;) {
            // 选择事件
            selector.select();
            // 当有客户端准备连接到服务端时，便会出发请求
            Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
            while (keyIter.hasNext()) {
                SelectionKey key = keyIter.next();
                System.out.println(key.readyOps());
                if (key.isAcceptable()) {
                    //如果接受到了一个新连接
                    ServerSocketChannel server = (ServerSocketChannel) key.channel( );
                    SocketChannel channel = server.accept( );
                    if (channel == null) {
                        ;//handle code, could happen
                    }
                    channel.configureBlocking (false);    //非阻塞式
                    //给收到的这个channel，也注册给这个selector，并且关注它的op_read事件
                    channel.register (selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    // read data.
                    Channel channel = key.channel();
                    keyIter.remove();
                }
            }
        }
    }
}
