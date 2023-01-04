package com.astontech.pulsardemoproducer.controller;

import com.astontech.pulsardemoproducer.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
@Slf4j
public class ProducerController {

    private static final String SUCCESS_RESPONSE = "Successfully produced message to Pulsar.";
    private static final String FAILURE_RESPONSE = "Failed to produced message to Pulsar. Check producer app log for more information.";

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<String> postMessageToProducer(@RequestBody String message) {
        log.info("Received POST request. Forwarding request body to producer service...");
        try {
            producerService.produceMessageToPulsar(message);
            return ResponseEntity.ok(SUCCESS_RESPONSE);
        } catch (PulsarClientException e) {
            log.error("Failed to produce message to Pulsar:", e);
        }
        return ResponseEntity.internalServerError().body(FAILURE_RESPONSE);
    }
}
