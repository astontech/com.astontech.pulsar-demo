package com.astontech.pulsardemoconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    private final List<String> receivedMessages = new ArrayList<>();

    public void saveMessageContent(String messageContent) {
        receivedMessages.add(messageContent);
    }

    public List<String> retrieveMessages() {
        return receivedMessages;
    }
}
