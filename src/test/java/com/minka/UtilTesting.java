package com.minka;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.EdDSASecurityProvider;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import org.apache.commons.codec.DecoderException;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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

//        String secret = "0e93ce131ff69320f67f4c60b41976b28e735aaf83407b03dff9ec53c9bd126e";
//        byte[] privateKeyBytes = Hex.decode(secret);
//        byte[] privateKeyBytes = secret.getBytes();
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec("010c2658713ab0853a515f47dcfcbdddc736fe6ff0de85793fd0c1d84e8b67aa".getBytes());
//        EdDSAPrivateKey keyIn = new EdDSAPrivateKey(encoded);
//        String secretBack = Hex.toHexString(keyIn.getEncoded());
//
//        System.out.println("secret:" + secretBack);
//        Assert.assertEquals(secret, secretBack);

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
//        System.out.println("Public key");
//        System.out.println(keyPair.getPublicKey().length());
//        System.out.println("Private key encoded in base64");
//        PrivateKey privateKey = keyPair.getPrivateKey();
//        String s = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//        System.out.println(privateKey.);

        System.out.println("Private key encoded in hex string");
        System.out.println(keyPair.getSecretInHexString().length());

//        System.out.println("Public key encoded in base64");
//        PublicKey publicKey = keyPair.getPublicKeyOriginal();
//        String spk = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        System.out.println(spk.length());

        System.out.println("Public key encoded in hex string");
        System.out.println(keyPair.getPublic().length());
    }

    @Test
    public void tempo() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new EdDSASecurityProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EdDSA", "EdDSA");

        KeyPair keyPair = keyGen.genKeyPair();
        EdDSAPrivateKey aPrivate = (EdDSAPrivateKey) keyPair.getPrivate();
        char[] chars = org.apache.commons.codec.binary.Hex.encodeHex(aPrivate.getAbyte());
        String result = new String(chars);
        System.out.println(result.length());

        try {
            Assert.assertArrayEquals(result.toCharArray(), chars);
            byte[] bytes = org.apache.commons.codec.binary.Hex.decodeHex(result.toCharArray());
            Assert.assertArrayEquals(bytes, aPrivate.getAbyte());

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
            EdDSAPrivateKey keyIn = new EdDSAPrivateKey(pkcs8EncodedKeySpec) ;
        } catch (DecoderException | InvalidKeySpecException e) {
            e.printStackTrace();
        }


//        try {

//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }

    }
    @Test
    public void tempoPublic() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new EdDSASecurityProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EdDSA", "EdDSA");

        KeyPair keyPair = keyGen.genKeyPair();
        EdDSAPublicKey publicKey = (EdDSAPublicKey) keyPair.getPublic();
//        "kpub=" +
//                String s = Base64.getEncoder().encodeToString(publicKey.);

        System.out.println(publicKey.getFormat());

//        System.out.println(s.length());
//        char[] chars = org.apache.commons.codec.binary.Hex.encodeHex(publicKey.getAbyte());
//        String result = new String(chars);
//        System.out.println(result);
//        System.out.println(result.length());


    }
}
