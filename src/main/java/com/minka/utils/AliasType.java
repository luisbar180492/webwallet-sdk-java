package com.minka.utils;

public enum AliasType {

    SOURCE("SOURCE"),

    TARGET("TARGET");

    private String value;

    AliasType(String value) {
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
