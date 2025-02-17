package com.nightlifeexplorer.beckend.exception;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String name) {
        super(String.valueOf(name));
    }
}