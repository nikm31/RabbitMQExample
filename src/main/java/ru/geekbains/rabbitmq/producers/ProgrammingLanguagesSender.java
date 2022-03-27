package ru.geekbains.rabbitmq.producers;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class ProgrammingLanguagesSender {
    private static final String EXCHANGER_NAME = "Programming";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setConnectionTimeout(10000);

        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT);

            channel.basicPublish(EXCHANGER_NAME, "php", null, "PHP MSG".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGER_NAME, "java", null, "JAVA MSG".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGER_NAME, "python", null, "PYTHON MSG".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGER_NAME, "c++", null, "C++ MSG".getBytes(StandardCharsets.UTF_8));
        }

    }
}
