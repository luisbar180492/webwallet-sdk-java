package com.minka;

import com.minka.api.model.Keeper;
import com.minka.wallet.IOU;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Claim;
import com.minka.wallet.primitives.utils.Sdk;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.time.DateUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class main {

    private static KeyPair sourceKeyPair;
    private static KeyPair targetKeyPair;
    private static String sourceAddress;
    private static String targetAddress;

    public static void main(String args[]) throws MissingRequiredParameterIOUCreation, DecoderException, NoSuchAlgorithmException {

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

        System.out.println("Printing pretty JSON ");
        System.out.println(iou.toPrettyJson());
        System.out.println("Printing Raw JSON ");
        System.out.println(iou.toRawJson());

        System.out.println("Printing pretty JSON for the FORMAT V022");
        System.out.println(iou.toPrettyJsonV022());
        System.out.println("Printing raw JSON for the FORMAT V022");
        System.out.println(iou.toRawJsonV022());

//        Keeper keeper = Sdk.obtenerKeeper();
//        System.out.println(keeper.toString());

    }
}