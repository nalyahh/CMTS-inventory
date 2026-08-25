package com.CMTS.inventory.exception;

public class ItemNotDeletableException extends RuntimeException {
    public ItemNotDeletableException(String message) {
        super(message);
    }
}