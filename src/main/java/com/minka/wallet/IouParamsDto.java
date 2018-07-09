package com.minka.wallet;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

/**
 * Class holder for the parameters needed to create an IOU
 */
public class IouParamsDto {

    private static String DEFAULT_DOMAIN = "www";
    private String domain;
    private String source;
    private String target;
    private String amount;
    private String credit;
    private String symbol;
    private String random;

    private Date notBefore;
    private Date expire;

    public IouParamsDto(String domain, String source, String target, String amount,
                        String credit, String symbol, String random, Date notBefore, Date expire)
            throws MissingRequiredParameterIOUCreation {

        if(source == null || target == null || amount == null || symbol == null){
            throw new MissingRequiredParameterIOUCreation("Missing required param");
        }

        this.source = source;
        this.target = target;
        this.amount = amount;
        this.credit = credit;
        this.symbol = symbol;
        this.random = random;

        if (random == null){
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            this.random = RandomStringUtils.random(length, useLetters, useNumbers);
        }else{
            this.random = random;
        }

        this.notBefore = notBefore;
        this.expire = expire;

        if (domain == null){
            this.domain = DEFAULT_DOMAIN;
        } else{
            this.domain = domain;
        }
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
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

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
