package com.minka;


import com.minka.wallet.primitives.KeyPair;

/**
 * Signer class
 */
public class Signer {

    private String address;
    private KeyPair keyPair;

    public Signer(String address, KeyPair keyPair) {
        this.address = address;
        this.keyPair = keyPair;
    }

    public Signer() {

    }

    public String getAddress() {
        return address;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}

