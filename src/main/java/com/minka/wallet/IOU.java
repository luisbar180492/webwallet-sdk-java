package com.minka.wallet;

import java.security.PrivateKey;
import java.util.Map;

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

    public IOU sign(Map<String, PrivateKey > signatures){
        //TODO implement for signature
        return this;
    }

}
