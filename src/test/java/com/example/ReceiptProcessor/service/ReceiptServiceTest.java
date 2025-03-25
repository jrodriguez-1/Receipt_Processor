package com.example.ReceiptProcessor.service;

import com.example.ReceiptProcessor.builder.ReceiptBuilder;
import com.example.ReceiptProcessor.model.Receipt;
import com.example.ReceiptProcessor.repository.ReceiptRepository;
import com.example.ReceiptProcessor.util.PointsCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    /**
     * Tests for generatedReceiptId method
     */
    @Test
    void generateReceiptId_validReceipt_generatesIdAndSaves() {
        Receipt receipt = createSampleReceipt();
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        String id = receiptService.generateReceiptId(receipt);

        assertNotNull(id);
        assertEquals(id, receipt.getId());
        assertTrue(id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));

        ArgumentCaptor<Receipt> receiptCaptor = ArgumentCaptor.forClass(Receipt.class);

        verify(receiptRepository).save(receiptCaptor.capture());
        assertEquals(id, receiptCaptor.getValue().getId());
    }

    @Test
    void generateReceiptId_invalidReceipt_handlesException() {
        Receipt receipt = createSampleReceipt();
        when(receiptRepository.save(any(Receipt.class)))
                .thenThrow(new RuntimeException("Error saving receipt"));

        assertThrows(RuntimeException.class, () -> {
            receiptService.generateReceiptId(receipt);
        });

        verify(receiptRepository).save(any(Receipt.class));
    }

    /**
     * Tests for processReceiptPoints method
     */
    @Test
    void processReceiptPoints_existingId_returnPoints() {
        String id = "test-id";
        Receipt receipt = createSampleReceipt();
        receipt.setId(id);

        when(receiptRepository.findById(id)).thenReturn(Optional.of(receipt));

        try (MockedStatic<PointsCalculator> mockedCalculator = mockStatic(PointsCalculator.class)) {
            mockedCalculator.when(() -> PointsCalculator.calculatePoints(receipt)).thenReturn(42);

            int points = receiptService.processReceiptPoints(id);

            assertEquals(42, points);

            verify(receiptRepository).findById(id);
        }
    }

    @Test
    void processReceiptPoints_nonExistentId_throwsException() {
        String id = "non-existent-id";
        when(receiptRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> receiptService.processReceiptPoints(id)
        );

        assertTrue(exception.getMessage().contains("No receipt found for that ID"));
        verify(receiptRepository).findById(id);
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