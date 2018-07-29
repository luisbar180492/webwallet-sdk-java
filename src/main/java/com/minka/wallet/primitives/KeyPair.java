package com.minka.wallet.primitives;

/**
 * Simplified KeyPair (public and private key)
 * in order to make  webwallet sdk similar to sdk nodejs version
 */
public interface KeyPair {

    String getScheme();
    String getPublic();
    String getSecret();
}
