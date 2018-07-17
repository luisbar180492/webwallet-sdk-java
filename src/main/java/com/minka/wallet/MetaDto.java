package com.minka.wallet;

import java.util.List;

public class MetaDto {

    private List<SignatureDto> signatures;


    public List<SignatureDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SignatureDto> signatures) {
        this.signatures = signatures;
    }
}
