package com.example.ReceiptProcessor.util;

import com.example.ReceiptProcessor.model.Item;
import com.example.ReceiptProcessor.model.Receipt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointsCalculatorTest {

    @Test
    void calculatePoints_validReceipt1_returns28Points() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("12:00");
        receipt.setItems(new ArrayList<>());

        List<Item> items = Arrays.asList(
            createItem("Mountain Dew 12PK", "6.49"),
            createItem("Emils Cheese Pizza", "12.25"),
            createItem("Knorr Creamy Chicken", "1.26"),
            createItem("Doritos Nacho Cheese", "3.35"),
            createItem("   Klarbrunn 12-PK 12 FL OZ  ", "12.00")
        );
        receipt.setItems(items);
        receipt.setTotal("35.35");

        assertEquals(28, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_validReceipt2_returns109Points() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("M&M Corner Market");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setItems(new ArrayList<>());

        List<Item> items = Arrays.asList(
            createItem("Gatorade", "2.25"),
            createItem("Gatorade", "2.25"),
            createItem("Gatorade", "2.25"),
            createItem("Gatorade", "2.25")
        );
        receipt.setItems(items);
        receipt.setTotal("9.00");

        assertEquals(109, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void shouldReturn0Points_whenReceiptIsNull() {

        assertEquals(0, PointsCalculator.calculatePoints(null));
    }

    @Test
    void handlesEmptyValues_whenCalculatingPoints() {

        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("");
        receipt.setItems(new ArrayList<>());
        receipt.setTotal("");

        assertEquals(6, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void givenInvalidPurchaseDate_shoudReturns0Points() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("not-a-valid-date");
        receipt.setPurchaseTime("12:00");
        receipt.setItems(new ArrayList<>());
        receipt.setTotal("0.01");

        assertEquals(0, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void whenNullRetailer_shouldNotAddRetailerPoints() {
        Receipt receipt = new Receipt();
        receipt.setRetailer(null);
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("12:00");
        receipt.setItems(new ArrayList<>());
        receipt.setTotal("0.01");

        assertEquals(0, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void nullItemsEmptyValues_shouldReturnZero() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("12:00");
        receipt.setItems(null);
        receipt.setTotal("0.01");

        assertEquals(0, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void getItemDescriptionPoints_handlesAllItemDescriptionEdgeCases() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("12:00");
        receipt.setTotal("0.01");

        List<Item> items = new ArrayList<>();

        // Case 1: null items list
        receipt.setItems(null);
        assertEquals(0, PointsCalculator.calculatePoints(receipt));

        // Case 2: item with null description
        Item nullDescItem = new Item();
        nullDescItem.setShortDescription(null);
        nullDescItem.setPrice("1.00");
        items.add(nullDescItem);
        receipt.setItems(items);
        assertEquals(0, PointsCalculator.calculatePoints(receipt));

        // Case 3: description not multiple of 3
        items.clear();
        Item nonMultipleItem = new Item();
        nonMultipleItem.setShortDescription("Apple"); // Length 4
        nonMultipleItem.setPrice("1.00");
        items.add(nonMultipleItem);
        receipt.setItems(items);
        assertEquals(0, PointsCalculator.calculatePoints(receipt));

        // Case 4: description multiple of 3 but null price
        items.clear();
        Item nullPriceItem = new Item();
        nullPriceItem.setShortDescription("Hat"); // Length 3
        nullPriceItem.setPrice(null);
        items.add(nullPriceItem);
        receipt.setItems(items);
        assertEquals(0, PointsCalculator.calculatePoints(receipt));

        // Case 5: description multiple of 3 with valid price
        items.clear();
        Item validItem = new Item();
        validItem.setShortDescription("ABC");
        validItem.setPrice("5.00");
        items.add(validItem);
        receipt.setItems(items);
        assertEquals(1, PointsCalculator.calculatePoints(receipt));
    }

    @Test
    void calculatePoints_emptyDescriptionAfterTrimming_shouldNotAddPoints() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("12:00");
        receipt.setTotal("0.01");

        List<Item> items = new ArrayList<>();

        Item emptyDescItem = new Item();
        emptyDescItem.setShortDescription("   ");
        emptyDescItem.setPrice("5.00");
        items.add(emptyDescItem);
        receipt.setItems(items);

        assertEquals(0, PointsCalculator.calculatePoints(receipt));
    }

    // Helper methods
    private Receipt createBaseReceipt() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("12:00");
        receipt.setItems(new ArrayList<>());
        receipt.setTotal("0.00");
        return receipt;
    }

    private Item createItem(String description, String price) {
        Item item = new Item();
        item.setShortDescription(description);
        item.setPrice(price);
        return item;
    }
}