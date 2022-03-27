package ru.geekbains.rabbitmq.consumers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProgrammingLanguagesReceiver {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Print exchanger name to connect");
        String exchangerName = in.nextLine();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setConnectionTimeout(10000);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangerName, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        List<String> deliveryThemes = new ArrayList<>();

        System.out.println("Print themes to deliver and 'end' to exit binding");
        String inputTheme;

        while (in.hasNextLine() && !((inputTheme = in.nextLine()).equalsIgnoreCase("end"))) {
            deliveryThemes.add(inputTheme);
        }

        for (String themes : deliveryThemes) {
            channel.queueBind(queueName, exchangerName, themes);
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

        Thread unbinding = new Thread(() -> {
            String unbindingTheme;
            while (true) {
                if (in.hasNextLine() && !((unbindingTheme = in.nextLine()).substring(0, 5).equalsIgnoreCase("unbind"))) {
                    try {
                        channel.queueUnbind(queueName, exchangerName, unbindingTheme.substring(7));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        unbinding.start();
    }
}
