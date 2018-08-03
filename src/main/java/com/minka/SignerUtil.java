package com.minka;

import com.minka.api.model.Keeper;
import com.minka.wallet.primitives.KeyPair;

public class SignerUtil {
    public static Signer toSigner(Keeper keeper, String source) {
        Signer result = new Signer();
        result.setAddress(source);
        result.setKeyPair(new KeeperMatcher(keeper));
        return result;
    }

    public static class KeeperMatcher implements KeyPair{

        private Keeper keeper;
        public KeeperMatcher(Keeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public String getScheme() {
            return this.keeper.getScheme();
        }

        @Override
        public String getPublic() {
            return this.keeper.getPublic();
        }

        @Override
        public String getSecret() {
            return this.keeper.getSecret();
        }

        @Override
        public String toJson() {
            return this.keeper.toString();
        }
    }
}
