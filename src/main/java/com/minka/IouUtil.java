package com.minka;

import com.google.gson.GsonBuilder;
import com.minka.wallet.*;
import io.minka.api.model.*;
import org.apache.commons.lang.RandomStringUtils;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class IouUtil {


    public static String convertToIsoFormat(Date date){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }
    //TODO multiple signing!!
    public static IouSigned generateIou(GetActionResponse action, String domain, OfflineSigningKeys keys) throws MissingRequiredParameterIOUCreation {

        IouSigned result = new IouSigned();
        IouSignedData data = new IouSignedData();
        data.setAmount(action.getAmount());
        data.setDomain(domain);
        data.setExpiry(action.getLabels().getCreated());
        data.setRandom(getRandomString());
        data.setSource(action.getSnapshot().getSource().getSigner().getHandle());
        data.setTarget(action.getSnapshot().getTarget().getSigner().getHandle());
        data.setSymbol(action.getSnapshot().getSymbol().getSigner().getHandle());
        result.data(data);

        String secret = keys.getKeeper().get(0).getSecret();
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder(secret);
        Map<PrivateKey, SignatureDto> signaturePairs = new HashMap<>();
        signaturePairs.put(sourcekeyPairHolder.getPrivateKey(), sourcekeyPairHolder.getBasicSignatureDto(data.getSource()));

        IouParamsDto iouParamsDto = new IouParamsDto(domain,
                data.getSource(), data.getTarget(), data.getAmount(), null,
                data.getSymbol(), data.getRandom(), null, data.getExpiry());
        IouUtil iouUtil = new IouUtil();
        IOU theIou = iouUtil.write(iouParamsDto);
        theIou.sign(signaturePairs);

        IouSignedHash hash = new IouSignedHash();
        hash.setSteps(theIou.getHash().getSteps());
        hash.setTypes(theIou.getHash().getTypes());
        hash.setValue(theIou.getHash().getValue());
        result.setHash(hash);

        IouSignedMeta signatures = new IouSignedMeta();

        Signatures item = new Signatures();
        item.setPublic(theIou.getMeta().getSignatures().get(0).getPublic());
        item.setScheme(theIou.getMeta().getSignatures().get(0).getScheme());
        item.setSigner(theIou.getMeta().getSignatures().get(0).getSigner());
        item.setString(theIou.getMeta().getSignatures().get(0).getString());
        signatures.addSignaturesItem(item);

        result.setMeta(signatures);
        return result;
    }

    public static String getRandomString() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public IOU write(IouParamsDto iouParamsDto)
    {
        HashDto hashDto = new HashDto();

        List<String> steps = new ArrayList<>();
        steps.add("stringify");
        steps.add("data");
        hashDto.setSteps(steps);

        List<String> types = new ArrayList<>();
        types.add("sha256");
        types.add("sha256");

        hashDto.setTypes(types);

        List<String> encodings = new ArrayList<>();
        encodings.add("utf8");

        String data = (new GsonBuilder()).create().toJson(iouParamsDto);
        hashDto.setValue(HashingUtil.createHashForIou(data, types, encodings));

        return new IOU(iouParamsDto, hashDto.getDtoForJson());
    }
}
