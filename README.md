# webwallet-sdk
Webwallet sdk java version

This is the java implementation for the nodejs sdk https://github.com/webwallet/sdk

Below is a complete example for creating an IOU and print it.

``` java
package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.IouUtil;
import com.minka.KeyPairHolder;
import com.minka.wallet.*;
import org.apache.commons.codec.DecoderException;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.*;


public class main {

    public static void main(String args[]) throws MissingRequiredParameterIOUCreation, DecoderException, NoSuchAlgorithmException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder();
        KeyPairHolder.KeyPairHolderDto keypairDto = sourcekeyPairHolder.getDtoForJson();
        System.out.println(gson.toJson(keypairDto));
        System.out.println("Now we generate an address");

        Address address = new Address(keypairDto.getPublico());
        String sourceAddress = address.generate().getValue();
        System.out.println("addressGenerated : " + sourceAddress);

        IouParamsDto iouParamsDto;
        String domain = "domain";

        String source = "wRLdAFjt8rbHWVzvXkAJB68YPwYRWjvkCj";
        String target = "wVA63LTkhiVfc2fhN7iU6xvmTanp9y9qbU";
        String symbol = source;
        String random = "2eeff2202dc8c7f1db50";

        System.out.println("source:" + source);
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);
        System.out.println("We convert the java date to ISO FORMAT");
        String expiry = IouUtil.convertToIsoFormat(new Date());

        iouParamsDto = new IouParamsDto(domain,
                source, target, amount, credit,
                symbol, random, null, expiry);

        IouUtil iouUtil = new IouUtil();
        System.out.println("Here we write the claims");
        IOU theIou = iouUtil.write(iouParamsDto);

        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(sourcekeyPairHolder.getSecret(), sourcekeyPairHolder.getBasicSignatureDto(sourceAddress));
        System.out.println("In order to sign we use the Key,value = PrivateKeys, SignatureDto");
        theIou.sign(signaturePairs);

        System.out.println("Printing pretty JSON ");
        System.out.println(theIou.toPrettyJson());
        System.out.println("Printing Raw JSON ");
        System.out.println(theIou.toRawJson());

        System.out.println("Printing pretty JSON for the OLD FORMAT");
        System.out.println(theIou.toPrettyJsonOldFormat());
        System.out.println("Printing raw JSON for the OLD FORMAT");
        System.out.println(theIou.toRawJsonOldFormat());
    }
}
```
