package com.kafka.controller;

import com.kafka.dto.LibraryEvent;
import com.kafka.producer.LibraryEventsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController; //

@RestController
@Slf4j
public class LibraryEventsController {

    private final LibraryEventsProducer libraryEventsProducer;

    public LibraryEventsController(LibraryEventsProducer libraryEventsProducer){
        this.libraryEventsProducer = libraryEventsProducer;
    }
    @PostMapping("v1/postEvent")
    public ResponseEntity<LibraryEvent> postEvent(@RequestBody LibraryEvent libraryEvent) throws Exception {
        log.info("libraryEvent {}" + libraryEvent);
       // libraryEventsProducer.sendLibraryEvents(libraryEvent);
       // libraryEventsProducer.sendLibraryEvents_approach2(libraryEvent);
        libraryEventsProducer.sendLibraryEvents_approach3(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
