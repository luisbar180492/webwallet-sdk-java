package com.minka;

import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/***
 * It returns a key-pair generated using Ed25519
 */
public class KeyPairHolder {

    private PublicKey publicKey;
    private PrivateKey secret;

    public KeyPairHolder(){

        KeyPairGenerator generatorWithEd25519 = new KeyPairGenerator();
        KeyPair keyPair = generatorWithEd25519.generateKeyPair();
        publicKey = keyPair.getPublic();
        secret = keyPair.getPrivate();
    }

    public String getPublicKey() {
        byte[] encoded = publicKey.getEncoded();
        return Hex.toHexString(encoded);
    }

    public String getSecretInHexString() {
        byte[] encoded = secret.getEncoded();
        return Hex.toHexString(encoded);
    }

    public PrivateKey getSecret() {
        return secret;
    }
}
