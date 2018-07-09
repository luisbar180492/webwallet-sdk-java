package com.minka.wallet;

import com.minka.HashAlgorithms;
import com.minka.HashingUtil;
import org.bitcoinj.core.Base58;
import org.spongycastle.util.encoders.Hex;

public class Address {

    private String data;
    private String hash;
    private String value;

    public Address(String data, String hash, String value, HashAlgorithms hashAlgorithm){

        this.data = data;
        if (hash != null){
            this.hash = hash;
        } else{
            if(HashAlgorithms.RIPEMD.equals(hashAlgorithm)){
                this.hash =  HashingUtil.hashWithRipemd160(data);
            }
            if(HashAlgorithms.SHA256.equals(hashAlgorithm)){
                this.hash =  HashingUtil.hashWithsha256(data);
            }

        }
        this.value = value;
    }

    public String encodeAddress(String prefixInput){
        String prefix;
        if (prefixInput == null){
            prefix = "87";
        } else {
            prefix = prefixInput;
        }

        String inputToEncode = prefix + hash;
        String result = Base58.encode(inputToEncode.getBytes());
        this.value = Hex.toHexString(result.getBytes());
        return this.value;
    }

    public String decodeAddress(String value, String prefixInput){
        String prefix;
        if (prefixInput == null){
            prefix = "87";
        } else {
            prefix = prefixInput;
        }
        byte[] decoded = Base58.decode(value);
        return Hex.toHexString(decoded);
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
