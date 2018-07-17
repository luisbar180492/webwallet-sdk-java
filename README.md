# webwallet-sdk
Webwallet sdk java version

This is the java implementation for the nodejs sdk https://github.com/webwallet/sdk

``` java


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.wallet.*;
import org.apache.commons.codec.DecoderException;
import org.junit.Before;
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

    @Test
    public void iouEjemplo() throws MissingRequiredParameterIOUCreation, DecoderException, NoSuchAlgorithmException {

        logger.info("Fist we generate a key-pair using the Ed25519");
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder();
        KeyPairHolder.KeyPairHolderDto keypairDto = sourcekeyPairHolder.getDtoForJson();
        logger.info(gson.toJson(keypairDto));
        logger.info("Now we generate an address");

        Address address = new Address(keypairDto.getPublico());
        String sourceAddress = address.generate().getValue();
        logger.info("addressGenerated : " + sourceAddress);

        IouParamsDto iouParamsDto;
        String domain = "domain";

        String source = "wRLdAFjt8rbHWVzvXkAJB68YPwYRWjvkCj";
        String target = "wVA63LTkhiVfc2fhN7iU6xvmTanp9y9qbU";
        String symbol = source;
        String random = "2eeff2202dc8c7f1db50";

        logger.info("source:" + source);
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);
        logger.info("We convert the java date to ISO FORMAT");
        String expiry = IouUtil.convertToIsoFormat(new Date());

        iouParamsDto = new IouParamsDto(domain,
                source, target, amount, credit,
                symbol, random, null, expiry);

        IouUtil iouUtil = new IouUtil();
        logger.info("Here we write the claims");
        IOU theIou = iouUtil.write(iouParamsDto);

        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(sourcekeyPairHolder.getSecret(), sourcekeyPairHolder.getBasicSignatureDto(sourceAddress));
        logger.info("In order to sign we use the Key,value = PrivateKeys, SignatureDto");
        theIou.sign(signaturePairs);


        logger.info("Printing pretty JSON ");
        logger.info(theIou.toPrettyJson());
        logger.info("Printing Raw JSON ");
        logger.info(theIou.toRawJson());
                
        //  THESE PRINTINGS FOR OLD FORMAT ONLY OWRK for the 0.1.0
        logger.info("Printing pretty JSON for the OLD FORMAT");
        logger.info(theIou.toPrettyJsonOldFormat());
        logger.info("Printing raw JSON for the OLD FORMAT");
        logger.info(theIou.toRawJsonOldFormat());
    }
}

```
