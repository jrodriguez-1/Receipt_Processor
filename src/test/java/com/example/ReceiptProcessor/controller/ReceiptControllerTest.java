package com.example.ReceiptProcessor.controller;

import com.example.ReceiptProcessor.builder.ReceiptBuilder;
import com.example.ReceiptProcessor.dto.PointsResponseDto;
import com.example.ReceiptProcessor.dto.ProcessReceiptDto;
import com.example.ReceiptProcessor.model.Receipt;
import com.example.ReceiptProcessor.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    /**
     * Tests for getPoints method
     */
    @Test
    void getPoints_validId_returnsPointsResponse() {
        String id = UUID.randomUUID().toString();
        when(receiptService.processReceiptPoints(id)).thenReturn(42);

        ResponseEntity<PointsResponseDto> response = receiptController.getPoints(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(42, response.getBody().getPoints());
        verify(receiptService).processReceiptPoints(id);
    }

    @Test
    void getPoints_invalidId_throwsResponseStatusException() {
        String id = "non-existent-id";
        when(receiptService.processReceiptPoints(id))
                .thenThrow(new IllegalArgumentException("Receipt not found"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> receiptController.getPoints(id)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Receipt not found"));
        verify(receiptService).processReceiptPoints(id);
    }

    /**
     * Tests for processReceipts method
     */
    @Test
    void processReceipts_validReceipt_returnsId() {
        Receipt receipt = createSampleReceipt();
        String expectedId = UUID.randomUUID().toString();
        when(receiptService.generateReceiptId(any(Receipt.class))).thenReturn(expectedId);

        ResponseEntity<ProcessReceiptDto> response = receiptController.processReceipts(receipt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedId, response.getBody().getId());
        verify(receiptService).generateReceiptId(receipt);
    }

    @Test
    void processReceipts_serviceException_returnsBadRequest() {
        Receipt receipt = createSampleReceipt();
        when(receiptService.generateReceiptId(any(Receipt.class)))
                .thenThrow(new RuntimeException("Processing failed"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> receiptController.processReceipts(receipt)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(receiptService).generateReceiptId(receipt);
    }

    /**
     * Helper method
     */
    private Receipt createSampleReceipt() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Test Store")
                .withPurchaseDate("2022-01-01")
                .withPurchaseTime("13:00")
                .withItems("Test Item", "10.00")
                .build();
        return receipt;
    }
}