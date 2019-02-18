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
    private static final String PRIV_KEY = "0428d3c7c859773de10cdf887032fde8558b8ae4256714d911009be4cbf8f53b";
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
//        String sourceAddress = "weDRQjKoE2UhD7yjicJTkDgXAwc2zqivXL";
//        logger.info("addressGenerated : " + sourceAddress);

        IouParamsDto iouParamsDto;
        String domain = "achtin.minka.io";

//        String source = "weDRQjKoE2UhD7yjicJTkDgXAwc2zqivXL";
//        String target = "wQxWXHPCDcfmGnuNPRAxfVoxWH79YcGBJV";
        String source = "wQxWXHPCDcfmGnuNPRAxfVoxWH79YcGBJV";
        String target = "weDRQjKoE2UhD7yjicJTkDgXAwc2zqivXL";

        String symbol = source;
        String random = "0c24a9f69f4b18ca1648";

        logger.info("source:" + source);
        BigDecimal amount = new BigDecimal(100);
//        BigDecimal credit = null;
        logger.info("We convert the java date to ISO FORMAT");
        String expiry = "2019-02-17T17:56:34.391Z";

        iouParamsDto = new IouParamsDto(domain,
                source, target, amount.toString(), null,
                symbol, random, null, expiry);

        IouUtil iouUtil = new IouUtil();
        logger.info("Here we write the claims");
        IOU theIou = iouUtil.write(iouParamsDto);

        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(sourcekeyPairHolder.getPrivateKey(), sourcekeyPairHolder.getBasicSignatureDto(source));
        logger.info("In order to sign we use the Key,value = PrivateKeys, SignatureDto");
        theIou.sign(signaturePairs);
        logger.info(gson.toJson(theIou));

//        logger.info("Printing pretty JSON ");
//        logger.info(theIou.toPrettyJson());
//        logger.info("Printing Raw JSON ");
//        logger.info(theIou.toRawJson());

//        logger.info("Printing pretty JSON for the FORMAT V022");
//        logger.info(theIou.toPrettyJsonV022());
//        logger.info("Printing raw JSON for the FORMAT V022");
//        logger.info(theIou.toRawJsonV022());
    }
}
