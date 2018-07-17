package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.wallet.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.*;
import java.util.logging.Logger;


public class IOUTesting {


    static final Logger logger = Logger.getLogger(String.valueOf(IOUTesting.class));

    private Gson gson;

    @Before
    public void starter(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting().create();

    }
    @Ignore
    @Test
    public void iou() throws MissingRequiredParameterIOUCreation {
        IouParamsDto iouParamsDto;
        String domain = "www";
        KeyPairHolder sourceKeys = new KeyPairHolder();
        String source = (sourceKeys).getPublicKey();
        System.out.println("source:" + source);
        String target= (new KeyPairHolder()).getPublicKey();
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);

        String symbol = source;
        Date active = new Date();
        Date expire = DateUtils.addDays(active,4);

//        iouParamsDto = new IouParamsDto(domain, source,target, amount, credit,
//                                        symbol, null,active, expire);
//
//
//        IouUtil iouUtil = new IouUtil();
//        IOU theIou = iouUtil.write(iouParamsDto);

//        List<PrivateKey> privatekeys = new ArrayList<>();
//        privatekeys.add(sourceKeys.getSecret());
//        theIou.sign(privatekeys);
//        //IMPRIMIR JSON
//        String theIouJson = theIou.toString();
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();
//
//
//        System.out.println(gson.toJson(theIou));

    }

    @Test
    public void iouEjemplo() throws MissingRequiredParameterIOUCreation, DecoderException, NoSuchAlgorithmException {

        logger.info("Fist we generate a key-pair using the Ed25519");
        KeyPairHolder keyPairHolder = new KeyPairHolder();
        KeyPairHolder.KeyPairHolderDto keypairDto = keyPairHolder.getDtoForJson();
        logger.info(gson.toJson(keypairDto));
        logger.info("Now we generate an address");

        Address address = new Address(keypairDto.getPublico());
        String addressGenerate = address.generate().getValue();
        logger.info("addressGenerate : " + addressGenerate);

        IouParamsDto iouParamsDto;
        String domain = "domain";
        String source = "wRLdAFjt8rbHWVzvXkAJB68YPwYRWjvkCj";
        String target = "wVA63LTkhiVfc2fhN7iU6xvmTanp9y9qbU";
        String symbol = source;
        String random = "2eeff2202dc8c7f1db50";

        logger.info("source:" + source);
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);


        String expiry = "2018-07-16T21:50:42.550Z";

        iouParamsDto = new IouParamsDto(domain,
                source, target, amount, credit,
                symbol, random, null, expiry);

        IouUtil iouUtil = new IouUtil();
        IOU theIou = iouUtil.write(iouParamsDto);

        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(keyPairHolder.getSecret(), keyPairHolder.getBasicSignatureDto(addressGenerate));
        theIou.sign(signaturePairs);
        logger.info(gson.toJson(theIou));

//        List<PrivateKey> privatekeys = new ArrayList<>();
//        privatekeys.add("wZeNAaSyfdcHLvkmvpsobbWH8CtMDf2`2kF");
//        theIou.sign(privatekeys);
        //        keyPairHolder.getBasicSignatureDto();
//        System.out.println(gson.toJson(theIou));
//      System.out.println(gson.toJson(signatureDto));


    }
}
