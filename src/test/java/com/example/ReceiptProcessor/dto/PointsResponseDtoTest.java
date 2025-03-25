package com.example.ReceiptProcessor.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointsResponseDtoTest {

    @Test
    void testPointsDtoGettersAndSetters() {
        PointsResponseDto dto = new PointsResponseDto();

        dto.setPoints(55);

        assertEquals(55, dto.getPoints());
    }

    @Test
    void testPointsDtoConstructor() {
        int points = 42;

        PointsResponseDto dto = new PointsResponseDto(points);

        assertEquals(points, dto.getPoints());
    }
}