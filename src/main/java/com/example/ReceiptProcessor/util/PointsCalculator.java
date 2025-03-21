package com.example.ReceiptProcessor.util;

import com.example.ReceiptProcessor.model.Item;
import com.example.ReceiptProcessor.model.Receipt;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class PointsCalculator {

    private static final BigDecimal QUARTER = new BigDecimal("0.25");
    private static final BigDecimal ITEM_MULTIPLIER = new BigDecimal("0.2");
    private static final BigDecimal TEN_DOLLARS = new BigDecimal("10.00");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Calculates the total points for a receipt based on all rules.
     */
    public static int calculatePoints(Receipt receipt) {
        if (receipt == null) {
//            log.warn("Null receipt provided to points calculator");
            return 0;
        }

        int points = 0;

        // Apply each rule and sum the points
        points += getRetailerNamePoints(receipt.getRetailer());
        points += getTotalBasedPoints(receipt.getTotal());
        points += getItemCountPoints(receipt.getItems());
        points += getItemDescriptionPoints(receipt.getItems());
        points += getPurchaseDatePoints(receipt.getPurchaseDate().toString());
        points += getPurchaseTimePoints(receipt.getPurchaseTime().toString());

        log.info("Receipt {} earned {} points", receipt.getId(), points);
        return points;
    }

    /**
     * Rule 1: One point for every alphanumeric character in the retailer name.
     */
    public static int getRetailerNamePoints (String retailer) {
        if (retailer == null) return 0;

        int points = 0;

        for (int i = 0; i < retailer.length(); i++) {
            char c = retailer.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                points++;
            }
        }

        log.debug("Retailer name '{}' earned {} points", retailer, points);
        return points;
    }

    /**
     * Applies all rules related to the receipt total:
     * - Rule 2: 50 points if the total is a round dollar amount
     * - Rule 3: 25 points if the total is a multiple of 0.25
     */
    private static int getTotalBasedPoints(String total) {
        int points = 0;
        BigDecimal totalAmount = parseAmount(total);

        if (totalAmount == null) return 0;

        // Rule 2: Round dollar amount
        if (totalAmount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            log.debug("Round dollar amount ({}): +50 points", total);
            points += 50;
        }

        // Rule 3: Multiple of 0.25
        if (totalAmount.remainder(QUARTER).compareTo(BigDecimal.ZERO) == 0) {
            log.debug("Multiple of 0.25 ({}): +25 points", total);
            points += 25;
        }

        return points;
    }

    /**
     * Rule 4: 5 points for every two items on the receipt.
     */
    private static int getItemCountPoints(List<Item> items) {
        if (items == null) return 0;

        int pairs = items.size() / 2;
        int points = pairs * 5;

        log.debug("{} items ({} pairs): +{} points", items.size(), pairs, points);
        return points;
    }

    /**
     * Rule 5: If the trimmed length of the item description is a multiple of 3,
     * multiply the price by 0.2 and round up to the nearest integer.
     */
    private static int getItemDescriptionPoints(List<Item> items) {
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
                    log.debug("Item '{}' (length {}): +{} points", description, length, itemPoints);
                    points += itemPoints;
                }
            }
        }

        return points;
    }

    /**
     * Rule 7: 6 points if the day in the purchase date is odd.
     */
    private static int getPurchaseDatePoints(String purchaseDate) {
        try {
            LocalDate date = LocalDate.parse(purchaseDate);
            int day = date.getDayOfMonth();

            if (day % 2 != 0) {
                log.debug("Purchase day {} is odd: +6 points", day);
                return 6;
            }
        } catch (Exception e) {
            log.debug("Unable to parse purchase date: {}", purchaseDate);
        }

        return 0;
    }

    /**
     * Rule 8: 10 points if the time of purchase is after 2:00pm and before 4:00pm.
     */
    private static int getPurchaseTimePoints(String purchaseTime) {
        try {
            LocalTime time = LocalTime.parse(purchaseTime, TIME_FORMATTER);
            LocalTime twopm = LocalTime.of(14, 0);
            LocalTime fourpm = LocalTime.of(16, 0);

            if (time.isAfter(twopm) && time.isBefore(fourpm)) {
                log.debug("Purchase time {} is between 2-4 PM: +10 points", purchaseTime);
                return 10;
            }
        } catch (Exception e) {
            log.debug("Unable to parse purchase time: {}", purchaseTime);
        }

        return 0;
    }

    /**
     * Utility method to safely parse a monetary amount.
     */
    private static BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException | NullPointerException e) {
            log.debug("Failed to parse amount: {}", amount);
            return null;
        }
    }
}