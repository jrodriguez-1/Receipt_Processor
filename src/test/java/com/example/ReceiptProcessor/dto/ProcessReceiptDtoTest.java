package com.example.ReceiptProcessor.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessReceiptDtoTest {

    @Test
    void testProcessDtoGettersAndSetters() {
        ProcessReceiptDto dto = new ProcessReceiptDto();

        dto.setId("test-Id");

        assertEquals("test-Id", dto.getId());
    }

    @Test
    void testProcessDtoConstructor() {
        String id = "test-Id";

        ProcessReceiptDto dto = new ProcessReceiptDto(id);

        assertEquals(id, dto.getId());
    }
}