package com.minka;

/**
 * Hash algorithms allowed
 */
public enum HashAlgorithms {

    SHA256("sha256"),
    RIPEMD("ripemd160");

    private String name;

    HashAlgorithms(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
