package com.astontech.pulsardemoconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@Slf4j
public class PulsarConfig {

    @Value("${pulsar.service-url}")
    private String SERVICE_URL;

    @Value("${pulsar.topic-name}")
    private String TOPIC_NAME;

    @Value("${pulsar.subscription-name}")
    private String SUBSCRIPTION_NAME;

    private PulsarClient pulsarClient;
    private Consumer<byte[]> pulsarConsumer;

    @Bean
    PulsarClient pulsarClient() throws PulsarClientException {
        log.info("Constructing new Pulsar client with service URL = '{}'...", SERVICE_URL);
        pulsarClient = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build();
        return pulsarClient;
    }

    @Bean
    Consumer<byte[]> pulsarConsumer(PulsarClient pulsarClient, MessageListener<byte[]> demoMessageListener) throws PulsarClientException {
        log.info("Constructing new Pulsar consumer for topic = '{}'...", TOPIC_NAME);
        pulsarConsumer = pulsarClient.newConsumer()
                .topic(TOPIC_NAME)
                .subscriptionName(SUBSCRIPTION_NAME)
                .messageListener(demoMessageListener)
                .subscribe();
        return pulsarConsumer;
    }

    @PreDestroy
    void cleanUpPulsarResources() {
        log.info("Closing Pulsar consumer...");
        try {
            pulsarConsumer.close();
            log.info("Successfully closed Pulsar consumer.");
        } catch (PulsarClientException e) {
            log.error("Exception while attempting to close Pulsar consumer:", e);
        }

        log.info("Closing Pulsar client...");
        try {
            pulsarClient.close();
            log.info("Successfully closed Pulsar client.");
        } catch (PulsarClientException e) {
            log.error("Exception while attempting to close Pulsar client:", e);
        }
    }

}
