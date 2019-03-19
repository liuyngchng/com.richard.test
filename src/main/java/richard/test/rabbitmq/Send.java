package richard.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * Created by Richard on 1/9/18.
 */
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("localhost");
        Channel channel = connection.createChannel();
        channel.queueDeclare(10, QUEUE_NAME);
        System.out.println("start send message.");
        while (System.currentTimeMillis() < Long.MAX_VALUE) {
            String message = "Hello World, this is me!" + Date.class.newInstance().toString();
            channel.basicPublish(10, "", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(5000);
        }
        channel.close(10, QUEUE_NAME);
        connection.close(10, QUEUE_NAME);
    }
}
