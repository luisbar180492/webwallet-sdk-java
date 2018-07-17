package com.minka.wallet;

import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;

/**
 * Class holder for the parameters needed to create an IOU
 */
public class IouParamsDto {

    private BigDecimal amount;
    private BigDecimal credit;
    private String domain;
    private String expiry;
    private String active;
    private String random;
    private String source;
    private String symbol;
    private String target;

    public IouParamsDto(String domain, String source, String target, BigDecimal amount,
                        BigDecimal credit, String symbol, String random, String active, String expiry)
            throws MissingRequiredParameterIOUCreation {

        if(source == null || target == null ||
                amount == null || symbol == null || expiry == null){
            throw new MissingRequiredParameterIOUCreation("Missing required param");
        }

        this.domain = domain;
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.credit = credit;
        this.symbol = symbol;
        this.random = random;
        this.active = active;

        if (random == null){
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            this.random = RandomStringUtils.random(length, useLetters, useNumbers);
        }else{
            this.random = random;
        }

        this.expiry = expiry;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
