package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.wallet.Address;
import com.subgraph.orchid.encoders.Hex;
import org.junit.Test;

public class AddressTesting {

    @Test
    public void checkAddress()  throws Exception
    {

        Object data = new Object();
        Gson gson = new GsonBuilder().create();

        String stringifiedStyleData = gson.toJson(data);
        System.out.println("stringifiedStyleData:" + stringifiedStyleData);
        Address address = new Address(stringifiedStyleData, null, null, HashAlgorithms.RIPEMD);

        String somePrefix  = "somePrefix";
        String encodedAddress = address.encodeAddress(somePrefix);
        System.out.println("encodedAddress:" + encodedAddress);

        byte[] decodeAddress = Hex.decode(encodedAddress);
        String decoded = address.decodeAddress(new String(decodeAddress), somePrefix);
        System.out.println("decoded:" + decoded);

    }
}
