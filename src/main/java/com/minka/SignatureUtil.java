package com.minka;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import org.spongycastle.util.encoders.Hex;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SignatureUtil {

    public static String signWithEd25519(String hashMessageExample, PrivateKey privateKey){

        try {
            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            Signature sgr;
            sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            sgr.initSign(privateKey);
            sgr.update(hashMessageExample.getBytes());
            byte[] sign = sgr.sign();
            return Hex.toHexString(sign);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            return null;
        }
    }
    public static String signWithEd25519(String hashMessageExample, String privateKeyInHexString) {

//        byte[] privateKeyBytes = Hex.decode(privateKeyInHexString);
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(privateKeyInHexString.getBytes());
        EdDSAPrivateKey keyIn;
        try {
            keyIn = new EdDSAPrivateKey(encoded);
            return signWithEd25519(hashMessageExample, keyIn);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
