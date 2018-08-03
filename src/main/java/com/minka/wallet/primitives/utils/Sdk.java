package com.minka.wallet.primitives.utils;

import com.minka.IouUtil;
import com.minka.KeyPairHolder;
import com.minka.api.handler.ApiException;
import com.minka.api.model.Keeper;
import com.minka.api.model.WalletUpdateRequest;
import com.minka.api.model.WalletUpdateResponse;
import com.minka.wallet.IouParamsDto;
import com.minka.wallet.MissingRequiredParameterIOUCreation;
import com.minka.wallet.primitives.KeyPair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    public static WalletResult createWallet(String handle, Map<String, Object> labelsSigner, Map<String, Object> labelsWallet) throws ApiException {

        WalletResult result = new WalletResult();

        SdkApiClient sdkApiClient = new SdkApiClient();
        Keeper keeper = sdkApiClient.getKeeper();
//        System.out.println(keeper);
        result.setKeeper(keeper);

        result.setWallet(sdkApiClient.getWallet(keeper, handle, labelsSigner, labelsWallet));

        return result;
    }

    public static WalletUpdateResponse updateWallet(String handle, List<String> signerAddresses, String defaultAddres) throws ApiException {

        SdkApiClient sdkApiClient = new SdkApiClient();
        WalletUpdateRequest wallet = new WalletUpdateRequest();
        wallet.setSigner(signerAddresses);
        wallet.setDefault(defaultAddres);
        return sdkApiClient.updateWallet(handle,wallet);
    }

}
