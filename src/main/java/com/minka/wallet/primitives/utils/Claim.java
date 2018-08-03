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

    public Claim setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Claim setSource(String source) {
        this.source = source;
        return this;
    }

    public Claim setTarget(String target) {
        this.target = target;
        return this;
    }

    public Claim setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Claim setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Claim setExpiry(String expiry) {
        this.expiry = expiry;
        return this;
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
