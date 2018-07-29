package com.minka.wallet.primitives.utils;

import com.minka.KeyPairHolder;
import com.minka.wallet.primitives.KeyPair;

/**
 * Utils exposing the SDK library.
 */
public class Sdk {

    public class Keypair {

        public KeyPair generate(){
            return new KeyPairHolder();
        }
    }

}
