package com.minka.utils;



public enum ActionType{
    SEND("SEND"),

    REQUEST("REQUEST");

    private String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}