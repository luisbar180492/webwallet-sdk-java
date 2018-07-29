package com.minka.wallet.primitives.utils;

import com.minka.KeyPairHolder;
import com.minka.wallet.primitives.KeyPair;

/**
 * Utils exposing the SDK library.
 */
public class Sdk {

    public static class Keypair {

        public static KeyPair generate(){
            return new KeyPairHolder();
        }
    }

    public static class Address {
        public static com.minka.wallet.Address generate(String publicKey){
            return new com.minka.wallet.Address(publicKey);
        }
    }
}
