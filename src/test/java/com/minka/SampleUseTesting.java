package com.minka;

import com.minka.wallet.IOU;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Claim;
import com.minka.wallet.primitives.utils.Sdk;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.KeyFactory;
import org.apache.commons.codec.DecoderException;
import org.junit.Ignore;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class SampleUseTesting {

    private KeyPair source;
    private KeyPair target;
    private String sourceSigner;
    private String targetSigner;

    @Ignore
    @Test
    public void fullExample() throws DecoderException, NoSuchAlgorithmException, MissingRequiredParameterIOUCreation {

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


        List<KeyPair> signers = new ArrayList<>();
        signers.add(source);


        Claim claim = new Claim();
        claim.setDomain("wallet.example.com");
        claim.setSource(sourceSigner);
        claim.setTarget(targetSigner);
        claim.setAmount("100");
        claim.setSymbol(sourceSigner);
        claim.setExpiry(IouUtil.convertToIsoFormat(new Date()));
        //TODO check 
        IOU iou = Sdk.IOU.write(claim).sign(signers);

        System.out.println(iou.toRawJson());
    }


}
