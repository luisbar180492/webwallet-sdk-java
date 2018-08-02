package com.minka;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class UtilTesting

{

    @Test
    public void SignatureMessage()  throws Exception
    {
        String message = "This is a message";
        String hashedMessage = HashingUtil.hashWithRipemd160(message, StandardCharsets.UTF_8);
        KeyPairHolder keyPairHolder = new KeyPairHolder();
        String signedMessage = SignatureUtil.signWithEd25519(hashedMessage, keyPairHolder.getPrivateKey());

        System.out.println(signedMessage);
    }

    @Test
    public void SignatureBack() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder();

        String secret = sourcekeyPairHolder.getSecret();
        System.out.println("secret:" + secret);

        byte[] privateKeyBytes = Hex.decode(secret);
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(privateKeyBytes);
        EdDSAPrivateKey keyIn = new EdDSAPrivateKey(encoded);
        String secretBack = Hex.toHexString(keyIn.getEncoded());

        System.out.println("secret:" + secretBack);
        Assert.assertEquals(secret, secretBack);

    }
    @Test
    public void HashingUtils()  throws Exception {
        String data = "asdf";
        String sha256 = HashingUtil.hashWithsha256(data,null);
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
