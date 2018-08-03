package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.api.handler.ApiException;
import com.minka.api.model.WalletUpdateResponse;
import com.minka.wallet.IOU;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;
import com.minka.wallet.primitives.utils.Claim;
import com.minka.wallet.primitives.utils.Sdk;
import com.minka.wallet.primitives.utils.WalletResult;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.*;

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

        Date today = new Date();
        Date tomorrow = DateUtils.addDays(today, 1);

        Claim claim = new Claim()
                .setDomain("wallet.example.com")
                .setSource(sourceAddress)
                .setTarget(targetAddress)
                .setAmount("100")
                .setSymbol(sourceAddress)
                .setExpiry(IouUtil.convertToIsoFormat(tomorrow));

        IOU iou = Sdk.IOU.write(claim).sign(signers);

        System.out.println(iou.toPrettyJson()  );

    }


    static String DOMAIN_ACH = "achtin-tst.minka.io";

    @Test
    public void fullCreateWalletCalled() throws ApiException, MissingRequiredParameterIOUCreation {
        String sourceHandle = "$abcd11";// + RandomStringUtils.random(3);
        Map<String, Object> labelsSigner = new HashMap<>();
        Map<String, Object> labelsWallet = new HashMap<>();

        WalletResult walletCreationResult = Sdk.createWallet(sourceHandle, labelsSigner, labelsWallet);

        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

        System.out.println(gson.toJson(walletCreationResult));

        String targetHandle = "$abcd10";
        String amount = "100";
        Claim claim = Sdk.createClaim(sourceHandle, targetHandle, amount, DOMAIN_ACH);

        System.out.println("Claim\n");
        System.out.println(gson.toJson(claim));
        Signer sourceSigner = SignerUtil.toSigner(walletCreationResult.getKeeper(), claim.getSource());
        List<Signer> signers = new ArrayList<>();
        signers.add(sourceSigner);

        IOU iou = Sdk.IOU.write(claim).sign(signers);

        System.out.println(iou.toPrettyJson());
//        List<String> newSigners = walletCreationResult.getWallet().getSigner();
//        String newSigner = "wQJjeZMXctGvdhJMa63azvuFfxDNqV7CBc";
//
//        newSigners.add(newSigner);
//        WalletUpdateResponse walletUpdateResponse = Sdk.updateWallet(handle, newSigners, newSigners.get(0));
//
//        System.out.println(gson.toJson(walletUpdateResponse));

    }
}
