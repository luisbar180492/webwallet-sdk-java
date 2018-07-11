package com.minka;

import gnu.crypto.hash.RipeMD160;
import org.apache.commons.codec.DecoderException;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashingUtil {

    public static String hashWithsha256(String string){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(string.getBytes());

            byte[] digest = messageDigest.digest();

            return new String(Hex.encode(digest));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String hashWithRipemd160(String input, Charset charset) throws DecoderException {
        RipeMD160 ripeMD160 = new RipeMD160();
        byte[] bytes;

        if (charset == null){
            bytes = org.apache.commons.codec.binary.Hex.decodeHex(input.toCharArray());
        } else {
            bytes = input.getBytes(charset);
        }

        ripeMD160.update(bytes,0, bytes.length);
        byte[] digest = ripeMD160.digest();
        String output = new String(Hex.encode(digest));

        return output;
    }

    public static String createHash(String data) throws DecoderException {
        return  HashingUtil.hashWithRipemd160(HashingUtil.hashWithsha256(data), null);
    }
}
