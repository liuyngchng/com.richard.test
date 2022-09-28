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
public class Server extends AbstractActor {

    final ActorRef manager;

    public Server(ActorRef manager) {
        this.manager = manager;
    }

    public static Props props(ActorRef manager) {
        return Props.create(Server.class, manager);
    }

    @Override
    public void preStart() throws Exception {
        final ActorRef tcp = null; //Tcp.get(getContext().getSystem()).manager();
        //老样子，是用来通知进行创建服务端actor
        tcp.tell(TcpMessage.bind(getSelf(),
            new InetSocketAddress("localhost", 0), 100), getSelf());
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Tcp.Bound.class, msg -> {
                manager.tell(msg, getSelf());

            })
            .match(Tcp.CommandFailed.class, msg -> {
                getContext().stop(getSelf());

            })
            .match(Tcp.Connected.class, conn -> {
                //有客户端连接
                manager.tell(conn, getSelf());
                //这个handler为自己实现的actor，即可指定让某个actor来处理接收的数据;
                final ActorRef handler = getContext().actorOf(
                    Props.create(SimplisticHandler.class));
                //注册一个handler来进行接收客户端传来的数据.
                getSender().tell(TcpMessage.register(handler), getSelf());
            })
            .build();
    }
    static class SimplisticHandler extends AbstractActor {
        @Override
        public Receive createReceive() {
            return receiveBuilder()
                .match(
                    Tcp.Received.class,
                    msg -> {
                        final ByteString data = msg.data();
                        System.out.println(data);
                        getSender().tell(TcpMessage.write(data), getSelf());
                    })
                .match(
                    Tcp.ConnectionClosed.class,
                    msg -> {
                        getContext().stop(getSelf());
                    })
                .build();
        }
    }

}