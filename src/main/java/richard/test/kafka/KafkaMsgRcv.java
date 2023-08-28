package richard.test.kafka;

import com.google.common.base.Strings;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.*;

/**
 * rcv msg from kfk
 * @author whoami
 * @since  2023-06-01
 */
public class KafkaMsgRcv implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private static Properties props;

    static {
        KafkaMsgRcv.initProps();
    }

    private Consumer consumer;

    public KafkaMsgRcv() {
        this.init();
    }

    @Override
    public void run() {
        LOGGER.info("start_tsk_{}", KafkaMsgRcv.class.getSimpleName());
//        this.stOffset(0);
        this.stOffsetTime(24);

        while (true) {
            this.rcvKfkMsg();
        }

    }

    /**
     * customize offset,存储最早的消息offset为0,依顺递增
     * @param offset
     */
    private void stOffset(long offset) {
        Set<TopicPartition> assignment = this.consumer.assignment();
        while (null ==  assignment || assignment.size() == 0) {
            this.consumer.poll(Duration.ofSeconds(1));
            assignment = this.consumer.assignment();
        }
        for (TopicPartition partition : assignment) {
            this.consumer.seek(partition,offset);
        }
    }

    /**
     * customize offset
     * @param stepBackHrs 当前时间向后步进的小时数
     */
    private void stOffsetTime(final int stepBackHrs) {
        Set<TopicPartition> assignment = this.consumer.assignment();
        while (null ==  assignment || assignment.size() == 0) {
            this.consumer.poll(Duration.ofSeconds(1));
            assignment = this.consumer.assignment();
        }
        final HashMap<TopicPartition, Long> stTimeMap = new HashMap<>();
        for (TopicPartition topicPartition : assignment) {
            stTimeMap.put(
                topicPartition,
                System.currentTimeMillis() - stepBackHrs * 3600 * 1000
            );
        }
        final Map<TopicPartition, OffsetAndTimestamp> offsetMap =
            this.consumer.offsetsForTimes(stTimeMap);
        for (TopicPartition topicPartition : assignment) {
            this.consumer.seek(topicPartition, offsetMap.get(topicPartition).offset());
        }
    }

    /**
     * init kfk client
     * @return
     */
    private void init() {
        this.consumer = new KafkaConsumer<String, String>(KafkaMsgRcv.props);
        final String topic = "test-topic";
        this.consumer.subscribe(Arrays.asList(topic));
        LOGGER.info("subscribe_topic, {}", topic);
    }

    /**
     * rcv kfk msg
     */
    private void rcvKfkMsg() {
        final ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
            if (Strings.isNullOrEmpty(record.value())) {
                LOGGER.debug("rcv_kfk_msg_value_null");
                continue;
            }
//            LOGGER.debug("rcv_kfk_msg_offset, {}, k={}, v={}", record.offset(), record.key(), record.value());
            LOGGER.info("{}_{}_{} saved in db", record.offset(), record.key(), record.timestamp());
//            break;
        }
        this.consumer.commitSync();
    }

    private static void initProps() {
        KafkaMsgRcv.props = new Properties();
        KafkaMsgRcv.props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.1:9092,192.168.0.2:9092");
        KafkaMsgRcv.props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-test-grp");
        KafkaMsgRcv.props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_uncommitted");
        KafkaMsgRcv.props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        KafkaMsgRcv.props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        KafkaMsgRcv.props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        KafkaMsgRcv.props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName()
        );
        KafkaMsgRcv.props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName()
        );
//        KafkaMsgRcv.props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaMsgRcv.props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        LOGGER.info("props inited, {}", KafkaMsgRcv.props);
    }
}
