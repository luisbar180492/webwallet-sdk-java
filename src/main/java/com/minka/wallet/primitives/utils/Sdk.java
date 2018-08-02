package com.minka.wallet.primitives.utils;

import com.minka.IouUtil;
import com.minka.KeyPairHolder;
//import com.minka.api.handler.ApiClient;
//import com.minka.api.handler.ApiException;
//import com.minka.api.handler.DefaultApi;
//import com.minka.api.model.Keeper;
import com.minka.wallet.IouParamsDto;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;



import java.math.BigDecimal;

/**
 * Utils exposing the SDK library.
 */
public class Sdk {

    public static class Keypair {

        public static KeyPair generate(){
            return new KeyPairHolder();
        }
    }

    public static class Address {
        public static com.minka.wallet.Address generate(String publicKey){
            return new com.minka.wallet.Address(publicKey);
        }
    }

    public static class IOU {
        public static com.minka.wallet.IOU write(Claim claim) throws MissingRequiredParameterIOUCreation {

            IouParamsDto iouParamsDto = new IouParamsDto(claim.getDomain(),
                    claim.getSource(),claim.getTarget(), new BigDecimal(claim.getAmount()), null,
                    claim.getSymbol(), null, null, claim.getExpiry());

            IouUtil iouUtil = new IouUtil();
            return iouUtil.write(iouParamsDto);
        }
    }

//    public static Keeper obtenerKeeper(){
//        DefaultApi api = new DefaultApi();
//        ApiClient apiclient = new ApiClient();
//        api.setApiClient(apiclient);
//
//        System.out.println(api.getApiClient().getBasePath());
//        try {
//            String apikey = "5b481fc2ae177010e197026b39c58cdb000f4c3897e841714e82c84c";
//
//            return api.obtenerKeeper(apikey);
//        } catch (ApiException e) {
//            System.out.println("EXCEPTION");
//
//            e.printStackTrace();
//            return null;
//        }
//    }
}
