package com.example.ReceiptProcessor.util;

import com.example.ReceiptProcessor.builder.ReceiptBuilder;
import com.example.ReceiptProcessor.model.Receipt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointsCalculatorTest {

    /**
     * Testing given scenarios
     **/
    @Test
    void calculatePoints_validReceipt1_returns28Points() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Target")
                .withPurchaseDate("2022-01-01")
                .withPurchaseTime("12:00")
                .withItems("Mountain Dew 12PK", "6.49")
                .withItems("Emils Cheese Pizza", "12.25")
                .withItems("Knorr Creamy Chicken", "1.26")
                .withItems("Doritos Nacho Cheese", "3.35")
                .withItems("Klarbrunn 12-PK 12 FL OZ  ", "12.00")
                .withTotal("35.35")
                .build();

        assertEquals(28, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_validReceipt2_returns109Points() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("M&M Corner Market")
                .withPurchaseDate("2022-03-20")
                .withPurchaseTime("14:33")
                .withItems("Gatorade", "2.25")
                .withItems("Gatorade", "2.25")
                .withItems("Gatorade", "2.25")
                .withItems("Gatorade", "2.25")
                .withTotal("9.00")
                .build();

        assertEquals(109, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void shouldReturn0Points_whenReceiptIsNull() {
        assertEquals(0, PointsCalculator.calculatePoints(null));
    }

    @Test
    void whenNullRetailer_shouldNotAddRetailerPoints() {

        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer(null)
                .withPurchaseDate("2022-01-02")
                .withPurchaseTime("12:00")
                .withItems("", "")
                .withTotal("0.01")
                .build();

        assertEquals(0, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void nullItemsEmptyValues_shouldReturn75() {

        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer(null)
                .withPurchaseDate("2022-01-02")
                .withPurchaseTime("12:00")
                .withItems(null, null)
                .withTotal("0.00")
                .build();

        assertEquals(75, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void givenInvalidPurchaseDate_shouldReturns75Points() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("")
                .withPurchaseDate("not-a-valid-date")
                .withPurchaseTime("12:00")
                .withItems("Soda", "1.50")
                .withItems("Candy", "1.50")
                .withTotal("3.00")
                .build();

        assertEquals(80, PointsCalculator.calculatePoints(receipt));
    }

    /**
     * Testing item description edge cases
     **/
    @Test
    void calculateItemDescription_handlesAllEdgeCases() {
        ReceiptBuilder baseBuilder = ReceiptBuilder.buildReceipt()
                .withRetailer("Walmart")
                .withPurchaseDate("2022-01-02")
                .withPurchaseTime("12:00")
                .withTotal("10.00");

        // Case 1: null items list
        Receipt nullItems = baseBuilder.withNullItems().build();

        assertEquals(82, PointsCalculator.calculatePoints(nullItems));

        // Case 2: item with null description
        Receipt nullDescription = baseBuilder.withItemHavingNullDescription("1.00").build();

        assertEquals(82, PointsCalculator.calculatePoints(nullDescription));

        // Case 3: description not multiple of 3
        Receipt nonMultipleReceipt = baseBuilder.withClearItems()
                .withItems("Apple", "1.00")
                .build();

        assertEquals(82, PointsCalculator.calculatePoints(nonMultipleReceipt));

        // Case 4: description multiple of 3 but null price
        Receipt nullPriceReceipt = baseBuilder.withClearItems()
                .withItemHavingNullPrice("Hat")
                .build();

        assertEquals(82, PointsCalculator.calculatePoints(nullPriceReceipt));

        // Case 5: description multiple of 3 with valid price
        Receipt validReceipt = baseBuilder.withClearItems()
                .withItems("ABC", "5.00")
                .build();

        assertEquals(83, PointsCalculator.calculatePoints(validReceipt));
    }

    /**
     * Testing empty receipt field scenarios
     **/
    @Test
    void handlesEmptyValues_whenCalculatingPoints() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("")
                .withPurchaseDate("2022-01-01")
                .withPurchaseTime("")
                .withItems("", "")
                .withTotal("")
                .build();

        assertEquals(6, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_emptyDescriptionAfterTrimming_shouldNotAddPoints() {

        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Kroger")
                .withPurchaseDate("2022-01-02")
                .withPurchaseTime("12:00")
                .withItems("   ", "5.00")
                .withTotal("5.00")
                .build();

        assertEquals(81, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_emptyDescriptionAfterTrimming_shouldNotAdd() {

        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Kroger")
                .withPurchaseDate("2022-01-02")
                .withPurchaseTime("12:00")
                .withItemHavingNullDescription("5.00")
                .withTotal("5.00")
                .build();

        assertEquals(81, PointsCalculator.calculatePoints(receipt));
    }

    /**
     * Testing purchase time scenarios
     */
    @Test
    void calculatePoints_betweenTwoAndFourPm_return10Points() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Jewel")
                .withPurchaseDate("2025-04-02")
                .withPurchaseTime("15:30")
                .withItems("Lemons", "3.00")
                .withTotal("3.00")
                .build();

        assertEquals(91, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_invalidPurchaseTime_return10Points() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withRetailer("Jewel")
                .withPurchaseDate("2025-04-02")
                .withPurchaseTime("non-valid-time")
                .withItems("Lemons", "3.00")
                .withTotal("3.00")
                .build();

        assertEquals(81, PointsCalculator.calculatePoints(receipt));
    }
}