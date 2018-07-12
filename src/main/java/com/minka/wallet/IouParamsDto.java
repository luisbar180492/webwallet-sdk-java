package com.minka.wallet;

import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Class holder for the parameters needed to create an IOU
 */
public class IouParamsDto {

    private BigDecimal amount;
    private String credit;
    private String domain;
    private Date expire;
    private Date active;
    private String random;
    private String source;
    private String symbol;
    private String target;

    public IouParamsDto(String domain, String source, String target, BigDecimal amount,
                        String credit, String symbol, String random, Date active, Date expire)
            throws MissingRequiredParameterIOUCreation {

        if(source == null || target == null ||
                amount == null || symbol == null || expire == null){
            throw new MissingRequiredParameterIOUCreation("Missing required param");
        }

        this.domain = domain;
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

        this.expire = expire;
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

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getActive() {
        return active;
    }

    public void setActive(Date active) {
        this.active = active;
    }
}
