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
    private static final String PRIV_KEY = "4aea0e61c029e600d1ca36d6b50bc974a2001574a80ac69997c4b2a04997b884";
    private static final String PUB_KEY =  "6a35efbef9eaad6b486a1f5d52aed18260e06339e2182056ea060bd26d4b4a8a";

    private Gson gson;

    @Before
    public void starter(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.setPrettyPrinting().create();

    }

    @Test
    public void iouEjemplo() throws MissingRequiredParameterIOUCreation,
                                        DecoderException, NoSuchAlgorithmException {

        logger.info("Fist we generate a key-pair using the Ed25519");
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder(PRIV_KEY);

        KeyPairHolder.KeyPairHolderDto keypairDto = sourcekeyPairHolder.getDtoForJson();
        logger.info(gson.toJson(keypairDto));

        logger.info("Now we generate an address");

        Address address = new Address(keypairDto.getPublico());
        String sourceAddress = "wQStXD7irdnGjGZVJ99mZ25vbZeXamCGFi";
        logger.info("addressGenerated : " + sourceAddress);

        IouParamsDto iouParamsDto;
        String domain = "localhost";

        String source = "wQStXD7irdnGjGZVJ99mZ25vbZeXamCGFi";
        String target = "wT99yCRnoYrN3KvTte3XhkUzjB9naFbwHo";
        String symbol = source;
        String random = "d7fa72c397477797c487";

        logger.info("source:" + source);
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = null;
        logger.info("We convert the java date to ISO FORMAT");
        String expiry = "2018-10-05T22:41:05.061Z";

        iouParamsDto = new IouParamsDto(domain,
                source, target, amount.toString(), null,
                symbol, random, null, expiry);

        IouUtil iouUtil = new IouUtil();
        logger.info("Here we write the claims");
        IOU theIou = iouUtil.write(iouParamsDto);

        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(sourcekeyPairHolder.getPrivateKey(), sourcekeyPairHolder.getBasicSignatureDto(sourceAddress));
        logger.info("In order to sign we use the Key,value = PrivateKeys, SignatureDto");
        theIou.sign(signaturePairs);
        logger.info(gson.toJson(theIou));

        logger.info("Printing pretty JSON ");
        logger.info(theIou.toPrettyJson());
        logger.info("Printing Raw JSON ");
        logger.info(theIou.toRawJson());

//        logger.info("Printing pretty JSON for the FORMAT V022");
//        logger.info(theIou.toPrettyJsonV022());
//        logger.info("Printing raw JSON for the FORMAT V022");
//        logger.info(theIou.toRawJsonV022());
    }
}
