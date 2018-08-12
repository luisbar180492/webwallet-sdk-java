package com.minka;


import net.i2p.crypto.eddsa.EdDSAKey;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSASecurityProvider;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.bitcoinj.core.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysEncodingProblem {

    String llavePublicaEnHexDesdeNodeJs = "02397611f07ee295c0baba81ba229558466c998580751515aa77bdb404722b6705";
    String llavePrivadaEnHexDesdeNodeJs = "0116e34cfae0b0c2a37ea3fd3a2897bd37bc609c2f91815a1e9a2d3b32345c50";
    @Test
    public void encodingIssue() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Security.addProvider(new EdDSASecurityProvider());
//        System.out.println("llavePublicaEnHexDesdeNodeJs");
//        System.out.println(llavePublicaEnHexDesdeNodeJs);
//        System.out.println("llavePublicaEnHexDesdeNodeJs:" + llavePublicaEnHexDesdeNodeJs.length());
//
//        System.out.println("llavePrivadaEnHexDesdeNodeJs");
//        System.out.println(llavePrivadaEnHexDesdeNodeJs);
//        System.out.println("llavePrivadaEnHexDesdeNodeJs:" + llavePrivadaEnHexDesdeNodeJs.length());
//
        byte[] publicKeyRaw = Utils.parseAsHexOrBase58(llavePublicaEnHexDesdeNodeJs);

//        X509EncodedKeySpec x509EncodedKeySpec;
//        x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyRaw);

//        System.out.println(x509EncodedKeySpec.getFormat());
//        System.out.println(x509EncodedKeySpec.getEncoded().length);


        byte[] privateKeyRaw = Utils.parseAsHexOrBase58(llavePrivadaEnHexDesdeNodeJs);

        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);

        EdDSAPrivateKeySpec edDSAPrivateKeySpec = new EdDSAPrivateKeySpec(privateKeyRaw, spec);
        EdDSAPrivateKey edDSAPrivateKey = new EdDSAPrivateKey(edDSAPrivateKeySpec);

        System.out.println(edDSAPrivateKey.getEncoded());
        System.out.println(privateKeyRaw );

//        System.out.println(privateKeyRaw.length);
//
//        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyRaw);
//
//        System.out.println(pkcs8EncodedKeySpec.getFormat());
//        System.out.println(pkcs8EncodedKeySpec.getEncoded().length);
//
////        PublicKey publicKey = extractPublicKey(llavePublicaEnHexDesdeNodeJs);
//
//        System.out.println("///////////////////////////////////////");
//        net.i2p.crypto.eddsa.KeyPairGenerator generatorWithEd25519 = new net.i2p.crypto.eddsa.KeyPairGenerator();
//        KeyPair keyPair = generatorWithEd25519.generateKeyPair();
//        EdDSAPrivateKey aPrivate = (EdDSAPrivateKey) keyPair.getPrivate();
//
//        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(aPrivate.getEncoded());
//
//        EdDSAPrivateKey keyIn = new EdDSAPrivateKey(encoded);
//
//        System.out.println(keyIn.getAbyte().length);
//        System.out.println(aPrivate.getAbyte().length);
//
//        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
//
//
//
//        // Encode
//        EdDSAPrivateKeySpec decoded = new EdDSAPrivateKeySpec(
//                keyIn.getSeed(),
//                keyIn.getH(),
//                keyIn.geta(),
//                keyIn.getA(),
//                keyIn.getParams());
//        EdDSAPrivateKey keyOut = new EdDSAPrivateKey(decoded);


    }

    private static PublicKey extractPublicKey(String publicKeyRaw) {
        try {
            KeyFactory factory = KeyFactory.getInstance(EdDSAKey.KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(new EdDSAPublicKeySpec(
                    Base64.getDecoder().decode(publicKeyRaw), EdDSANamedCurveTable.getByName("Ed25519")));
            return publicKey;
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
