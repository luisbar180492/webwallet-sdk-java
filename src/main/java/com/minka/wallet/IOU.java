package com.minka.wallet;

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

    public IOU sign(Map<String, PrivateKey > privateKeys){

        if (meta.getSignatures() == null){
            meta.setSignatures(new ArrayList<>());
        }
        Set<String> hashes = privateKeys.keySet();

        for (String currHash: hashes) {
            String signature;
            signature = SignatureUtil.signWithEd25519(currHash, privateKeys.get(currHash));
            meta.getSignatures().add(signature);
        }
        return this;
    }

}
