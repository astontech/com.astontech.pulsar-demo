package com.astontech.pulsardemoconsumer.config;

import com.astontech.pulsardemoconsumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoMessageListener implements MessageListener<byte[]> {

    private final ConsumerService consumerService;

    public DemoMessageListener(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    public void received(Consumer<byte[]> consumer, Message<byte[]> message) {
        try {
            log.info("Received message from topic '{}'", message.getTopicName());

            String messageContent = new String(message.getValue());
            log.info("Message content: {}", messageContent);
            consumerService.saveMessageContent(messageContent);

            consumer.acknowledge(message);
            log.info("Successfully acknowledged message");
        } catch (PulsarClientException e) {
            log.error("Failed to acknowledge message:", e);
            consumer.negativeAcknowledge(message);
        }
    }
}
