package com.minka.wallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minka.SignatureUtil;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IOU {

    private IouParamsDto iouParams;
    private HashDto hash;
    private MetaDto meta;

    public IOU(IouParamsDto data, HashDto hash, MetaDto meta){

        this.iouParams = data;
        this.hash = hash;
        this.meta = meta;
    }

    public IouParamsDto getIouParams() {
        return iouParams;
    }

    public HashDto getHash() {
        return hash;
    }

    public MetaDto getMeta() {
        return meta;
    }

    public IOU sign(List<PrivateKey > privateKeys){

        if (meta.getSignatures() == null){
            meta.setSignatures(new ArrayList<>());
        }

        for (PrivateKey currKey: privateKeys) {
            String signature;
            signature = SignatureUtil.signWithEd25519(this.hash.getValue(), currKey);
            meta.getSignatures().add(signature);
        }
        return this;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
