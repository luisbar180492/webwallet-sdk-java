package com.minka.wallet.primitives.utils;

/**
 * Simplied Claim
 */
public class Claim {

    private String domain;
    private String source;
    private String target;
    private String amount;
    private String symbol;
    private String expiry;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getDomain() {
        return domain;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getAmount() {
        return amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getExpiry() {
        return expiry;
    }
}
