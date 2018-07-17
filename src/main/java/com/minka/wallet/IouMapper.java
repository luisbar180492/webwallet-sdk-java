package com.minka.wallet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class IouMapper {

    private IouOldDataDto data;
    private IouOldHashDto hash;
    private IouOldSigsDto meta;


    public IouMapper(IOU iou) {
        this.data = mapData(iou.getData());
        this.hash = mapHash(iou.getHash());
        this.meta = mapMeta(iou.getMeta());
    }

    private IouOldSigsDto mapMeta(MetaDto meta) {

        IouOldSigsDto result = new IouOldSigsDto();

        List<SignatureDto> signatures = meta.getSignatures();
        List<OldSignature> oldSignatures = new ArrayList<>();
        for (SignatureDto curr:signatures) {
            OldSignature currOldSignature = new OldSignature(curr);
            oldSignatures.add(currOldSignature);
        }
        result.setSigs(oldSignatures);
        return result;
    }

    private IouOldHashDto mapHash(HashDto.HashJson hash) {

        IouOldHashDto result = new IouOldHashDto();
        result.setAlg(hash.getTypes());
        result.setVal(hash.getValue());
        return result;
    }

    private IouOldDataDto mapData(IouParamsDto data) {


        IouOldDataDto result = new IouOldDataDto();
        result.setSub(data.getSource());
        result.setAmt(data.getAmount().toString());
        result.setAud(data.getTarget());
        result.setCru(data.getSymbol());
        result.setExp(data.getExpiry());
        result.setIss(data.getDomain());
        result.setNce(data.getRandom());
        return result;
    }

    private class IouWrapper{
        private IouMapper IOU;

        public IouMapper getIOU() {
            return IOU;
        }

        public void setIOU(IouMapper IOU) {
            this.IOU = IOU;
        }
    }

    public String getOldIouRawJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        IouWrapper iouWrapper = new IouWrapper();
        iouWrapper.setIOU(this);
        return gson.toJson(iouWrapper);
    }

    public String getOldIouPrettyJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        IouWrapper iouWrapper = new IouWrapper();
        iouWrapper.setIOU(this);
        return gson.toJson(iouWrapper);
    }


    private class IouOldDataDto {
        private String sub;
        private String aud;
        private String amt;
        private String cru;
        private String exp;
        private String iss;
        private String nce;

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public String getAud() {
            return aud;
        }

        public void setAud(String aud) {
            this.aud = aud;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getCru() {
            return cru;
        }

        public void setCru(String cru) {
            this.cru = cru;
        }

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }

        public String getNce() {
            return nce;
        }

        public void setNce(String nce) {
            this.nce = nce;
        }
    }

    private class IouOldHashDto {
        private String alg;
        private String val;

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    private class OldSignature {
        private String alg;
        private String wid;
        private String key;
        private String sig;

        public OldSignature(SignatureDto curr) {
            this.key = curr.getPublic();
            this.alg = curr.getScheme();
            this.wid = curr.getSigner();
            this.sig = curr.getString();
        }

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public String getWid() {
            return wid;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSig() {
            return sig;
        }

        public void setSig(String sig) {
            this.sig = sig;
        }
    }
    private class IouOldSigsDto {
        private List<OldSignature> sigs;

        public List<OldSignature> getSigs() {
            return sigs;
        }

        public void setSigs(List<OldSignature> sigs) {
            this.sigs = sigs;
        }
    }

    public IouOldDataDto getData() {
        return data;
    }

    public void setData(IouOldDataDto data) {
        this.data = data;
    }

    public IouOldHashDto getHash() {
        return hash;
    }

    public void setHash(IouOldHashDto hash) {
        this.hash = hash;
    }

    public IouOldSigsDto getMeta() {
        return meta;
    }

    public void setMeta(IouOldSigsDto meta) {
        this.meta = meta;
    }
}
