package com.astontech.pulsardemoproducer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
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

    private PulsarClient pulsarClient;
    private Producer<byte[]> pulsarProducer;

    @Bean
    PulsarClient pulsarClient() throws PulsarClientException {
        log.info("Constructing new Pulsar client with service URL = '{}'...", SERVICE_URL);
        pulsarClient = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build();
        return pulsarClient;
    }

    @Bean
    Producer<byte[]> pulsarProducer(PulsarClient pulsarClient) throws PulsarClientException {
        log.info("Constructing new Pulsar producer for topic = '{}'...", TOPIC_NAME);
        pulsarProducer = pulsarClient.newProducer()
                .topic(TOPIC_NAME)
                .create();
        return pulsarProducer;
    }

    @PreDestroy
    void cleanUpPulsarResources() {
        log.info("Closing Pulsar producer...");
        try {
            pulsarProducer.close();
            log.info("Successfully closed Pulsar producer.");
        } catch (PulsarClientException e) {
            log.error("Exception while attempting to close Pulsar producer:", e);
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
