package com.minka;

import com.minka.wallet.IOU;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Claim;
import com.minka.wallet.primitives.utils.Sdk;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleUseTesting {

    private KeyPair sourceKeyPair;
    private KeyPair targetKeyPair;
    private String sourceAddress;
    private String targetAddress;

    @Ignore
    @Test
    public void fullExample() throws DecoderException, NoSuchAlgorithmException, MissingRequiredParameterIOUCreation {

        System.out.println("Generate cryptographic keys\n");
        sourceKeyPair = Sdk.Keypair.generate();
        targetKeyPair = Sdk.Keypair.generate();

        System.out.println(sourceKeyPair.toJson());
        System.out.println(targetKeyPair.toJson());

        System.out.println("\nGenerate wallet addresses from public keys\n");

        sourceAddress = Sdk.Address.generate(sourceKeyPair.getPublic()).encode();
        targetAddress = Sdk.Address.generate(targetKeyPair.getPublic()).encode();

        System.out.println("\nCreate signer\n");

        Signer sourceSigner = new Signer(sourceAddress, sourceKeyPair);

        System.out.println("\nSOURCE address\n");
        System.out.println(sourceAddress);
        System.out.println("\nTARGET address\n");
        System.out.println(targetAddress);

        List<Signer> signers = new ArrayList<>();
        signers.add(sourceSigner);

        Claim claim = new Claim();
        claim.setDomain("wallet.example.com");
        claim.setSource(sourceAddress);
        claim.setTarget(targetAddress);
        claim.setAmount("100");
        claim.setSymbol(sourceAddress);
        Date today = new Date();
        Date tomorrow = DateUtils.addDays(today, 1);

        claim.setExpiry(IouUtil.convertToIsoFormat(tomorrow));

        IOU iou = Sdk.IOU.write(claim).sign(signers);

        System.out.println(iou.toPrettyJson()  );
    }


}
