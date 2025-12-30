package com.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.dto.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class LibraryEventsProducer {

    @Value("${spring.kafka.topic}")
    private String topicName;

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendLibraryEvents(LibraryEvent libraryEvent) throws Exception {
        CompletableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topicName, libraryEvent.libraryEventId(), objectMapper.writeValueAsString(libraryEvent));
        future.whenComplete((sendresult, throwable) -> {
            if (throwable != null) {
                handleFailue(throwable);
            } else {
                handleSuccess(libraryEvent,sendresult);
            }
        });
    }

    public void sendLibraryEvents_approach2(LibraryEvent libraryEvent) throws Exception {
        SendResult<Integer, String> result = kafkaTemplate.send(topicName, libraryEvent.libraryEventId(), objectMapper.writeValueAsString(libraryEvent)).get();
        handleSuccess(libraryEvent,result);
    }

    private ProducerRecord<Integer,String> getProducerRecord(String topicName, Integer key, String value) {
        return new ProducerRecord<>(topicName,key,value);
    }

    public void sendLibraryEvents_approach3(LibraryEvent libraryEvent) throws Exception {
        SendResult<Integer, String> result = kafkaTemplate.send(getProducerRecord(topicName,libraryEvent.libraryEventId(),objectMapper.writeValueAsString(libraryEvent))).get();
        handleSuccess(libraryEvent,result);
    }

    private void handleSuccess(LibraryEvent libraryEvent,SendResult<Integer, String> result) {
        log.info("Successfully sent to topic "  + libraryEvent + " result was "+ result.getProducerRecord().partition());
    }

    private void handleFailue(Throwable throwable) {
        log.info("Exception {} "+ throwable);
    }

}
