package com.example.ReceiptProcessor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    @Test
    void testItemGettersAndSetters() {
        Item item = new Item();
        item.setId(1L);
        item.setShortDescription("Item 1");
        item.setPrice("10.99");

        assertEquals(1L, item.getId());
        assertEquals("Item 1", item.getShortDescription());
        assertEquals("10.99", item.getPrice());
    }

    @Test
    void testItemConstructor() {
        Long id = 1L;
        String shortDescription = "Item 1";
        String price = "10.99";

        Item item = new Item(id, shortDescription, price);

        assertEquals(id, item.getId());
        assertEquals(shortDescription, item.getShortDescription());
        assertEquals(price, item.getPrice());
    }
}