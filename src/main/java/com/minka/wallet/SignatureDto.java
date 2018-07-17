package com.minka.wallet;

import com.google.gson.annotations.SerializedName;

public class SignatureDto {

    private String scheme;
    private String signer;
    @SerializedName("public")
    private String publico;
    private String string;

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
