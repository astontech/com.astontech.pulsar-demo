package com.astontech.pulsardemoproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService {

    private final Producer<byte[]> pulsarProducer;

    public ProducerService(Producer<byte[]> pulsarProducer) {
        this.pulsarProducer = pulsarProducer;
    }

    public void produceMessageToPulsar(String message) throws PulsarClientException {
        log.info("Attempting to produce message to Pulsar...");
        pulsarProducer.send(message.getBytes());
        log.info("Successfully produced message to Pulsar.");
    }
}
