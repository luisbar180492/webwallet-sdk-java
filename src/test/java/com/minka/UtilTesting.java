package com.minka;

import org.junit.Test;

public class UtilTesting

{

    @Test
    public void SignatureMessage()  throws Exception
    {
        String message = "This is a message";
        String hashedMessage = HashingUtil.hashWithRipemd160(message);
        KeyPairHolder keyPairHolder = new KeyPairHolder();
        String signedMessage = SignatureUtil.signWithEd25519(hashedMessage, keyPairHolder.getSecret());

        System.out.println(signedMessage);
    }

    @Test
    public void HashingUtils()  throws Exception {
        String sha256 = HashingUtil.hashWithsha256("dfsa");
        System.out.println(sha256);

        String ripemd160 = HashingUtil.hashWithRipemd160("dfsa");
        System.out.println(ripemd160);

    }

    @Test
    public void KeyPairGeneration()  throws Exception {

        KeyPairHolder keyPair = new KeyPairHolder();
        System.out.println(keyPair.getPublicKey());
        System.out.println(keyPair.getSecretInHexString());

    }
}
