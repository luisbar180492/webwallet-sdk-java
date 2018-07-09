package com.minka;

/**
 * Encodings allowed
 */
public enum Encodings {

    BASE58CHECK("base58check");
//    RIPEMD("ripemd160");

    private String name;

    Encodings(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
