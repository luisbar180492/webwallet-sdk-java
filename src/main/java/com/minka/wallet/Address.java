package com.minka.wallet;

import com.metaco.api.encoders.Base58Check;
import com.minka.HashAlgorithms;
import com.minka.HashingUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Address {

    private String data;
    private String hash;
    private String value;
    private List<HashAlgorithms> hashAlgorithms;

    public Address(String data){
        this.data = data;
    }

    public Address withHash(String hash){
        this.hash = hash;
        return this;
    }

    public Address withHashAlgorithms(List<HashAlgorithms> listHashAlgorithms){
        this.hashAlgorithms = hashAlgorithms;
        return this;
    }

    public Address generate() throws DecoderException, NoSuchAlgorithmException {
        List<Charset> ENCODINGS = new ArrayList<>();
        ENCODINGS.add(StandardCharsets.UTF_8);
        System.out.println("this.data: " + this.data);

        System.out.println("this.hash: " + this.hash);

        if (hash == null){
            this.hash = HashingUtil.createHash(this.data);
            System.out.println("this.hash: " + this.hash);
        }
        this.value = encodeAddress(null);
        return this;
    }

    public String encodeAddress(String prefixInput) throws DecoderException, NoSuchAlgorithmException {
        String prefix;
        if (prefixInput == null){
            prefix = "87";
        } else {
            prefix = prefixInput;
        }

        String inputToEncode = prefix + hash;
        System.out.println("inputToEncode encodeAddress");
        System.out.println(inputToEncode);
        byte[] bytes = org.apache.commons.codec.binary.Hex.decodeHex(inputToEncode.toCharArray());
        this.value = Base58Check.encode(bytes);
        return this.value;
    }

    public String decodeAddress(String value, String prefixInput) throws DecoderException, NoSuchAlgorithmException {
        String prefix;
        if (prefixInput == null){
            prefix = "87";
        } else {
            prefix = prefixInput;
        }
        byte[] decoded = Base58Check.decode(value);
        return Hex.encodeHexString(decoded);
    }

    public boolean check(String address, String prefix, String encoding){
        try {
            decodeAddress(address,prefix);
            return true;
        } catch (DecoderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getValue() {
        return value;
    }
}
