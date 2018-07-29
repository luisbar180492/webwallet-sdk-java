package com.minka;

import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Sdk;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class SampleUseTesting {

    private KeyPair source;
    private KeyPair target;
    private String sourceSigner;
    private String targetSigner;

    @Test
    public void fullExample() throws DecoderException, NoSuchAlgorithmException {

        System.out.println("Generate cryptographic keys");
        source = Sdk.Keypair.generate();
        target = Sdk.Keypair.generate();

        System.out.println(source.toJson());
        System.out.println(target.toJson());


        System.out.println("Generate wallet addresses from public keys");
        sourceSigner = Sdk.Address.generate(source.getPublic()).encode();
        targetSigner = Sdk.Address.generate(target.getPublic()).encode();
        System.out.println(sourceSigner);
        System.out.println(targetSigner);



    }
}
