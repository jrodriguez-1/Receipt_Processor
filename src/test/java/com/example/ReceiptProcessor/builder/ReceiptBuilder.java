package com.example.ReceiptProcessor.builder;

import com.example.ReceiptProcessor.model.Item;
import com.example.ReceiptProcessor.model.Receipt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReceiptBuilder {

    private final Receipt receipt = new Receipt();
    private final List<Item> items = new ArrayList<>();

    public static ReceiptBuilder buildReceipt() {
        return new ReceiptBuilder();
    }

    public ReceiptBuilder withId(String id) {
        receipt.setId(id != null ? id : UUID.randomUUID().toString());
        return this;
    }

    public ReceiptBuilder withRetailer(String retailer) {
        receipt.setRetailer(retailer);
        return this;
    }

    public ReceiptBuilder withPurchaseDate(String purchaseDate) {
        receipt.setPurchaseDate(purchaseDate);
        return this;
    }

    public ReceiptBuilder withPurchaseTime(String purchaseTime) {
        receipt.setPurchaseTime(purchaseTime);
        return this;
    }

    public ReceiptBuilder withItems(String description, String price) {
        Item item = new Item();
        item.setShortDescription(description);
        item.setPrice(price);
        items.add(item);
        return this;
    }

    public ReceiptBuilder withTotal(String total) {
        receipt.setTotal(total);
        return this;
    }

    public ReceiptBuilder withNullItems() {
        receipt.setItems(null);
        return this;
    }

    public ReceiptBuilder withClearItems() {
        this.items.clear();
        return this;
    }

    public ReceiptBuilder withItemHavingNullDescription(String price) {
        Item item = new Item();
        item.setShortDescription(null);
        item.setPrice(price);
        items.add(item);
        return this;
    }

    public ReceiptBuilder withItemHavingNullPrice(String description) {
        Item item = new Item();
        item.setShortDescription(description);
        item.setPrice(null);
        items.add(item);
        return this;
    }

    public Receipt build() {
        receipt.setItems(items);
        return receipt;
    }
}
