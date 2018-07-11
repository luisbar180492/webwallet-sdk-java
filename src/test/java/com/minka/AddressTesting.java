package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metaco.api.encoders.Base58Check;
import com.minka.wallet.Address;
//import com.subgraph.orchid.encoders.Hex;
//import org.bitcoinj.core.Base58;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class AddressTesting {


    private static String SOURCE_PUBLIC = "0411c0b8d67c8c47d617ea8312bb1e157b0bb32f9e50f5ed3d4387d3f76ab3bb3e10224ee9876e6c67d16b7d3ada0e19e2b1b9b196cca45131fe33c034c6d606f1";
    private static String SOURCE_WALLET = "wL1318MhGBnmMDaf6Xe9iH7CeESBj67qRy";

    @Test
    public void checkAddress()  throws Exception
    {
//        String prefix = "87";
//        String hash = "asdf";
//        String hash = "affafafafafafafa";
//        String inputToEncode = prefix + hash;
//        System.out.println("inputToEncode: " + inputToEncode);

        //        String hexString = new String(org.spongycastle.util.encoders.Hex.encode(inputToEncode.getBytes()));
//        String bytes = org.apache.commons.codec.binary.Hex.encodeHexString(inputToEncode.getBytes());  //.decodeHex(hexString.toCharArray());

//        String encode = Base58Check.encode(inputToEncode.getBytes(StandardCharsets.UTF_8));

//        String s = new String(org.spongycastle.util.encoders.Hex.encode(inputToEncode.getBytes()));

//        String stringHex = new String(org.spongycastle.util.encoders.Hex.encode(inputToEncode.getBytes()));
//        byte[] bytes = org.apache.commons.codec.binary.Hex.decodeHex(inputToEncode.toCharArray());

//        String encode = Base58Check.encode(org.spongycastle.util.encoders.Hex.encode(inputToEncode.getBytes()));
//        String encode = Base58Check.encodePlain(org.spongycastle.util.encoders.Hex.encode(inputToEncode.getBytes()));

//        String encode = Base58Check.encode(bytes);
//        System.out.println(encode);


//        DataSampleObject dataSampleObject = new DataSampleObject();
//        dataSampleObject.setData(SOURCE_PUBLIC);
//
//        Gson gson = (new GsonBuilder()).create();
//        String data = gson.toJson(dataSampleObject);
//        Address address = new Address("{\"data\":\"0411c0b8d67c8c47d617ea8312bb1e157b0bb32f9e50f5ed3d4387d3f76ab3bb3e10224ee9876e6c67d16b7d3ada0e19e2b1b9b196cca45131fe33c034c6d606f1\"}");
        Address address = new Address(SOURCE_PUBLIC);
        String value = address.generate().getValue();
        System.out.println("address generated");
        System.out.println(value);


//        Object data = new Object();
//        Gson gson = new GsonBuilder().create();
//
//        String stringifiedStyleData = gson.toJson(data);
//        System.out.println("stringifiedStyleData:" + stringifiedStyleData);
//        Address address = new Address(stringifiedStyleData, null, null, HashAlgorithms.RIPEMD);
//
//        String somePrefix  = "somePrefix";
//        String encodedAddress = address.encodeAddress(somePrefix);
//        System.out.println("encodedAddress:" + encodedAddress);
//
//        byte[] decodeAddress = Hex.decode(encodedAddress);
//        String decoded = address.decodeAddress(new String(decodeAddress), somePrefix);
//        System.out.println("decoded:" + decoded);

    }
}
