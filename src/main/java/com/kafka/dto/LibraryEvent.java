package com.kafka.dto;
import com.kafka.enums.LibraryEnumType;

public record LibraryEvent(Integer libraryEventId,
                           LibraryEnumType libraryEventType,
                           Book book
                           ) {
}
