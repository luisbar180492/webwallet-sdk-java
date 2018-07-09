package com.minka;

import gnu.crypto.hash.RipeMD160;
import org.spongycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtil {

    public static String hashWithsha256(String string){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(string.getBytes());
            return new String(Hex.encode(messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String hashWithRipemd160(String string){
        RipeMD160 ripeMD160 = new RipeMD160();

        ripeMD160.update(string.getBytes(),0, string.length());
        byte[] digest = ripeMD160.digest();
        return Hex.toHexString(digest);
    }

}
