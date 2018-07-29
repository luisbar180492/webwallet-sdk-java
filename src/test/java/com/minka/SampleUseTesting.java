package com.minka;

import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Sdk;
import org.junit.Test;

public class SampleUseTesting {

    @Test
    public void createKeyPair(){
        KeyPair source = Sdk.Keypair.generate();
        KeyPair target = Sdk.Keypair.generate();

        System.out.println(source.toJson());
        System.out.println(target.toJson());

    }
}
