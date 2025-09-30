package richard.test.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.net.InetSocketAddress;

/**
 * Created by richard on 23/07/2019.
 * see https://doc.akka.io/docs/akka/snapshot/io-tcp.html?language=java
 */
public class Client extends AbstractActor {
    /*这是网络地址的封装类*/
    final InetSocketAddress remote;
    /*这是创建actor所依赖的类*/
    final ActorRef listener;

    //通过静态方法的调用来实现客户端的创建
    public static Props props(InetSocketAddress remote, ActorRef listener) {
        return Props.create(Client.class, remote, listener);
    }

    //构造方法
    public Client(InetSocketAddress remote, ActorRef listener) {
        this.remote = remote;
        this.listener = listener;

        //客户端的actor的创建
        final ActorRef tcp = null; //Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.connect(remote), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Tcp.CommandFailed.class, msg -> {
                listener.tell("failed", getSelf());
                getContext().stop(getSelf());

            })
            //接收到连接，进行回复及创建，调用下方的connected操作
            .match(Tcp.Connected.class, msg -> {
                listener.tell(msg, getSelf());
                getSender().tell(TcpMessage.register(getSelf()), getSelf());
                getContext().become(connected(getSender()));
            })
            .build();
    }


    //进行接受数据，根据类型进行对应操作
    private Receive connected(final ActorRef connection) {
        return receiveBuilder()
            //发送给服务端数据的动作
            .match(ByteString.class, msg -> {
                //具体操作，msg为要发送给服务端的数据
                connection.tell(TcpMessage.write((ByteString) msg), getSelf());
            })
            .match(Tcp.CommandFailed.class, msg -> {
                // OS kernel socket buffer was full
            })
            //接收服务端数据后的动作
            .match(Tcp.Received.class, msg -> {
                //具体操作
                listener.tell(msg.data(), getSelf());
            })
            .matchEquals("close", msg -> {
                connection.tell(TcpMessage.close(), getSelf());
            })
            .match(Tcp.ConnectionClosed.class, msg -> {
                getContext().stop(getSelf());
            })
            .build();
    }

}