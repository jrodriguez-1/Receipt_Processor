package com.example.ReceiptProcessor.model;

import com.example.ReceiptProcessor.builder.ReceiptBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptTest {
    @Test
    void testReceipt_gettersAndSetters() {
        Receipt receipt = buildReceipt();

        assertEquals("id-123", receipt.getId());
        assertEquals("Target", receipt.getRetailer());
        assertEquals("2023-01-01", receipt.getPurchaseDate());
        assertEquals("13:45", receipt.getPurchaseTime());
        assertEquals("35.35", receipt.getTotal());
        assertEquals(5, receipt.getItems().size());
    }

    @Test
    void constructorTest() {
        String id = "id-123";
        String retailer = "Target";
        String purchaseDate = "2023-01-01";
        String purchaseTime = "13:45";
        List<Item> items = new ArrayList<>();
        String total = "35.35";

        Receipt receipt = new Receipt(id, retailer, purchaseDate, purchaseTime, items, total);

        assertEquals(id, receipt.getId());
        assertEquals(retailer, receipt.getRetailer());
        assertEquals(purchaseDate, receipt.getPurchaseDate());
        assertEquals(purchaseTime, receipt.getPurchaseTime());
        assertSame(items, receipt.getItems());
        assertEquals(total, receipt.getTotal());
    }

//    Helper method
    private Receipt buildReceipt() {
        Receipt receipt = ReceiptBuilder.buildReceipt()
                .withId("id-123")
                .withRetailer("Target")
                .withPurchaseDate("2023-01-01")
                .withPurchaseTime("13:45")
                .withItems("Mountain Dew 12PK", "6.49")
                .withItems("Emils Cheese Pizza", "12.25")
                .withItems("Knorr Creamy Chicken", "1.26")
                .withItems("Doritos Nacho Cheese", "3.35")
                .withItems("Klarbrunn 12-PK 12 FL OZ  ", "12.00")
                .withTotal("35.35")
                .build();

        return receipt;
    }
}