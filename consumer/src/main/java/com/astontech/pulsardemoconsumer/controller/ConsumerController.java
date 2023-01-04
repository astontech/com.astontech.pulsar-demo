package com.astontech.pulsardemoconsumer.controller;

import com.astontech.pulsardemoconsumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllReceivedMessages() {
        log.info("Received GET request. Retrieving all messages received since last app start.");
        return ResponseEntity.ok(consumerService.retrieveMessages());
    }
}
