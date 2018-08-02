package com.minka.wallet;

import com.google.gson.annotations.SerializedName;
import com.minka.Signer;
import com.minka.wallet.primitives.KeyPair;

public class SignatureDto {

    private String scheme;
    private String signer;
    @SerializedName("public")
    private String publico;
    private String string;

    public SignatureDto() {
    }

    public SignatureDto(Signer currentSigner, String signature) {
        this.setPublic(currentSigner.getKeyPair().getPublic());
        this.setScheme(currentSigner.getKeyPair().getScheme());
        this.setString(signature);
        this.setSigner(currentSigner.getAddress());
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getPublic() {
        return publico;
    }

    public void setPublic(String publico) {
        this.publico = publico;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
