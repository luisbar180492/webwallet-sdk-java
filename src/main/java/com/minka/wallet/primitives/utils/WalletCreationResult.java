package com.minka.wallet.primitives.utils;

import com.minka.api.model.Keeper;
import com.minka.api.model.WalletUpdateResponse;


public class WalletCreationResult {

    private Keeper keeper;
    private WalletUpdateResponse wallet;

    public Keeper getKeeper() {
        return keeper;
    }

    public void setKeeper(Keeper keeper) {
        this.keeper = keeper;
    }

    public WalletUpdateResponse getWallet() {
        return wallet;
    }

    public void setWallet(WalletUpdateResponse wallet) {
        this.wallet = wallet;
    }
}
