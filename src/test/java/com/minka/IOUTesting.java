package com.minka;

import com.minka.wallet.IOU;
import com.minka.wallet.IouParamsDto;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.*;

public class IOUTesting {

    @Test
    public void iou() throws MissingRequiredParameterIOUCreation {
        IouParamsDto iouParamsDto;
        String domain = "www";
        KeyPairHolder sourceKeys = new KeyPairHolder();
        String source = (sourceKeys).getPublicKey();
        String target= (new KeyPairHolder()).getPublicKey();
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);

        String symbol = source;
        Date active = new Date();
        Date expire = DateUtils.addDays(active,4);

        iouParamsDto = new IouParamsDto(domain, source,target, amount, credit,
                                        symbol, null,active, expire);


        IouUtil iouUtil = new IouUtil();
        IOU theIou = iouUtil.write(iouParamsDto);

        List<PrivateKey> privatekeys = new ArrayList<>();
        privatekeys.add(sourceKeys.getSecret());
        theIou.sign(privatekeys);

    }
}
