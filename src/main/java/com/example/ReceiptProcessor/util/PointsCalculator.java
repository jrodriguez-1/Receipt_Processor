package com.example.ReceiptProcessor.util;

import com.example.ReceiptProcessor.model.Item;
import com.example.ReceiptProcessor.model.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PointsCalculator {

    private static final BigDecimal QUARTER = new BigDecimal("0.25");
    private static final BigDecimal ITEM_MULTIPLIER = new BigDecimal("0.2");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Calculates the total points for a receipt based on all rules.
     */
    public static int calculatePoints(Receipt receipt) {
        if (receipt == null) {
            return 0;
        }

        int points = 0;

        // Apply each rule to sum the points
        points += calculateRetailerPoints(receipt.getRetailer());
        points += calculateRoundDollarAndQuarterPoints(receipt.getTotal());
        points += calculateItemCountPoints(receipt.getItems());
        points += calculateItemDescriptionPoints(receipt.getItems());
        points += calculatePurchaseDatePoints(receipt.getPurchaseDate());
        points += calculatePurchaseTimePoints(receipt.getPurchaseTime());

        return points;
    }

    /**
     * Rule 1: One point for every alphanumeric character in the retailer name.
     */
    public static int calculateRetailerPoints(String retailer) {
        if (retailer == null) return 0;

        int points = 0;

        for (int i = 0; i < retailer.length(); i++) {
            char c = retailer.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                points++;
            }
        }
        return points;
    }

    /**
     * Applies all rules related to the receipt total:
     * - Rule 2: 50 points if the total is a round dollar amount
     * - Rule 3: 25 points if the total is a multiple of 0.25
     */
    private static int calculateRoundDollarAndQuarterPoints(String total) {
        int points = 0;
        BigDecimal totalAmount = parseAmount(total);

        if (totalAmount == null) return 0;

        // Rule 2: Round dollar amount
        if (totalAmount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }

        // Rule 3: Multiple of 0.25
        if (totalAmount.remainder(QUARTER).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }
        return points;
    }

    /**
     * Rule 4: 5 points for every two items on the receipt.
     */
    private static int calculateItemCountPoints(List<Item> items) {
        if (items == null) return 0;

        int pairs = items.size() / 2;
        int points = pairs * 5;

        return points;
    }

    /**
     * Rule 5: If the trimmed length of the item description is a multiple of 3,
     * multiply the price by 0.2 and round up to the nearest integer.
     */
    private static int calculateItemDescriptionPoints(List<Item> items) {
        if (items == null) return 0;

        int points = 0;
        for (Item item : items) {
            if (item.getShortDescription() == null) continue;

            String description = item.getShortDescription().trim();
            int length = description.length();

            if (length > 0 && length % 3 == 0) {
                BigDecimal price = parseAmount(item.getPrice());
                if (price != null) {
                    int itemPoints = (int) Math.ceil(price.multiply(ITEM_MULTIPLIER).doubleValue());
                    points += itemPoints;
                }
            }
        }

        return points;
    }

    /**
     * Rule 7: 6 points if the day in the purchase date is odd.
     */
    private static int calculatePurchaseDatePoints(String purchaseDate) {
        try {
            LocalDate date = LocalDate.parse(purchaseDate);
            int day = date.getDayOfMonth();

            if (day % 2 != 0) {
                return 6;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    /**
     * Rule 8: 10 points if the time of purchase is after 2:00pm and before 4:00pm.
     */
    private static int calculatePurchaseTimePoints(String purchaseTime) {
        try {
            LocalTime time = LocalTime.parse(purchaseTime, TIME_FORMATTER);
            LocalTime twopm = LocalTime.of(14, 0);
            LocalTime fourpm = LocalTime.of(16, 0);

            if (time.isAfter(twopm) && time.isBefore(fourpm)) {
                return 10;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    /**
     * Helper method to parse amount.
     */
    private static BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}