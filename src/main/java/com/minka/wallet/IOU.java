package com.minka.wallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.SignatureUtil;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOU {

    private IouParamsDto data;
    private HashDto.HashJson hash;
    private MetaDto meta;


    public void setMeta(MetaDto meta) {
        this.meta = meta;
    }

    public IOU(IouParamsDto data, HashDto.HashJson hash){

        this.data = data;
        this.hash = hash;
    }

    public IouParamsDto getData() {
        return data;
    }

    public HashDto.HashJson getHash() {
        return hash;
    }

    public MetaDto getMeta() {
        return meta;
    }

    public IOU sign(Map<PrivateKey,SignatureDto> signaturesPair){

        List<SignatureDto> signatureDtos = new ArrayList<>();

        for (PrivateKey currSignature: signaturesPair.keySet()) {
            String signature;
            signature = SignatureUtil.signWithEd25519(this.hash.getValue(), currSignature);
            SignatureDto signatureDto = signaturesPair.get(currSignature);
            signatureDto.setString(signature);
            signatureDtos.add(signatureDto);
        }

        MetaDto metaDto = new MetaDto();
        metaDto.setSignatures(signatureDtos);
        this.setMeta(metaDto);
        return this;
    }

    private class IOUPrintingWrapper {
        private IOU IOU;

        public com.minka.wallet.IOU getIOU() {
            return IOU;
        }

        public void setIOU(com.minka.wallet.IOU IOU) {
            this.IOU = IOU;
        }
    }

    public String toPrettyJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        IOUPrintingWrapper iouPrintingWrapper = new IOUPrintingWrapper();
        iouPrintingWrapper.setIOU(this);
        return gson.toJson(iouPrintingWrapper);
    }

    public String toRawJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        IOUPrintingWrapper iouPrintingWrapper = new IOUPrintingWrapper();
        iouPrintingWrapper.setIOU(this);

        return gson.toJson(iouPrintingWrapper);
    }

    public String toPrettyJsonOldFormat(){
        IouMapper iouMapper = new IouMapper(this);
        return iouMapper.getOldIouPrettyJson();
    }

    public String toRawJsonOldFormat(){
        IouMapper iouMapper = new IouMapper(this);
        return iouMapper.getOldIouRawJson();
    }



}
