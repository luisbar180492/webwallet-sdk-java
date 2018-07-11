package com.minka;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class UtilTesting

{

    @Test
    public void SignatureMessage()  throws Exception
    {
        String message = "This is a message";
        String hashedMessage = HashingUtil.hashWithRipemd160(message, StandardCharsets.UTF_8);
        KeyPairHolder keyPairHolder = new KeyPairHolder();
        String signedMessage = SignatureUtil.signWithEd25519(hashedMessage, keyPairHolder.getSecret());

        System.out.println(signedMessage);
    }

    @Test
    public void HashingUtils()  throws Exception {
        String data = "asdf";
        String sha256 = HashingUtil.hashWithsha256(data);
        System.out.println(sha256);

        String ripemd160 = HashingUtil.hashWithRipemd160(data, StandardCharsets.UTF_8);
        System.out.println(ripemd160);

        String hash = HashingUtil.createHash(data);

        System.out.println(hash);

    }

    @Test
    public void KeyPairGeneration()  throws Exception {

        KeyPairHolder keyPair = new KeyPairHolder();
        System.out.println(keyPair.getPublicKey());
        System.out.println(keyPair.getSecretInHexString());

    }
}
