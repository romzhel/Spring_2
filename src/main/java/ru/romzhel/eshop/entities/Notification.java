package ru.romzhel.eshop.entities;

import lombok.Data;

@Data
public class Notification {
    private String message;
    private Long numericValue;

    public Notification() {
    }

    public Notification(Long numericValue) {
        this.numericValue = numericValue;
    }

    public Notification(String message) {
        this.message = message;
    }
}
