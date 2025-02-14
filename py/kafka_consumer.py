#!/usr/bin/python3
#       #!/usr/bin/env python
#       author whoami
#       pip3 install pip3 install confluent-kafka
#       ./kafks_consumer.py kafka.cfg
import sys
import time

from datetime import datetime
from argparse import ArgumentParser, FileType
from configparser import ConfigParser
from confluent_kafka import Consumer, OFFSET_BEGINNING
from confluent_kafka import TopicPartition
from confluent_kafka import KafkaException

def convert_timestamp_to_datetime(ts):
    """
    时间戳转日期时间
    :param ts: 时间戳
    :return:
    """
    dt = datetime.strptime(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(ts)), "%Y-%m-%d %H:%M:%S")
    return dt

def reset_offset_to_24_hours_ago(consumer):
    # Get the current assignment
    assignment = consumer.assignment()
    while not assignment:
        consumer.poll(1.0)  # Poll for 1 second to get the assignment
        assignment = consumer.assignment()

    # Calculate the timestamp for 24 hours ago
    timestamp_24_hours_ago = int((time.time() - 24 * 3600) * 1000)

    # Create a list of TopicPartition objects with the desired timestamp
    timestamps_list = [TopicPartition(partition.topic, partition.partition, timestamp_24_hours_ago) for partition in assignment]

    # Get the offsets for the given timestamps
    offset_map = consumer.offsets_for_times(timestamps_list)

    # Set the offset for each partition
    for offset_info in offset_map:
        #partition=TopicPartition(partition.topic, offset_info.partition)
        if offset_info.offset:
            consumer.seek(TopicPartition(offset_info.topic,offset_info.partition, offset_info.offset))

if __name__ == '__main__':
    # Parse the command line.
    parser = ArgumentParser()
    parser.add_argument('config_file', type=FileType('r'))
    parser.add_argument('--reset', action='store_true')
    args = parser.parse_args()
    print("args", args)
    # Parse the configuration.
    # See https://github.com/edenhill/librdkafka/blob/master/CONFIGURATION.md
    config_parser = ConfigParser()
    config_parser.read_file(args.config_file)
    config = dict(config_parser['default'])
    config.update(config_parser['consumer'])
    print("consumer_config,", config)

    # Create Consumer instance
    consumer = Consumer(config)

    # Subscribe to topic
    topic = "test-up-rpt-dt"
    consumer.subscribe([topic])
    reset_offset_to_24_hours_ago(consumer)
    # Set up a callback to handle the '--reset' flag.
    def reset_offset(consumer, partitions):
        if args.reset:
            for p in partitions:
                p.offset = OFFSET_BEGINNING
            consumer.assign(partitions)
    # consumer.subscribe([topic], on_assign=reset_offset)
    # print("consumer.subscribe,", topic)
    # Get the topic's partitions
    metadata = consumer.list_topics(topic, timeout=10)
    if metadata.topics[topic].error is not None:
        raise KafkaException(metadata.topics[topic].error)

    # Construct TopicPartition list of partitions to query
    partitions = [TopicPartition(topic, p) for p in metadata.topics[topic].partitions]
    print("partitions,", partitions)

    # Poll for new messages from Kafka and print them.
    try:
        #consumer.assign([TopicPartition(topic=topic, partition=0, offset=30)])
        print("consumer.subscribe,", topic)
        count =0;
        while True:
            msg = consumer.poll(1.0)
            if msg is None:
                # Initial message consumption may take up to
                # `session.timeout.ms` for the consumer group to
                # rebalance and start consuming
                print("Waiting...")
            elif msg.error():
                print("ERR,", msg.error())
                break
            else:
                # Extract the (optional) key and value, and print.

                print("topic = {topic}: key = {key:12} value = {value:12} offset = {offset}".format(
                    topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8'), offset=msg.offset()))
                print("timestamp", msg.timestamp())
                a = msg.timestamp()
                print("timestamp", a)
                # dt = datetime.strptime(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(msg.timestamp())), "%Y-%m-%d %H:%M:%S")
                print("timestamp", convert_timestamp_to_datetime(a[1] / 1000.0))
                break
                # count=count+1
                # if count >10:
                #     break
    except KeyboardInterrupt:
        pass
    finally:
        # Leave group and commit final offsets
        consumer.close()
