package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.minka.wallet.SignatureDto;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/***
 * It returns a key-pair generated using Ed25519
 */
public class KeyPairHolder implements com.minka.wallet.primitives.KeyPair{

    private PublicKey publicKey;
    private PrivateKey secret;

    public KeyPairHolder(){

        KeyPairGenerator generatorWithEd25519 = new KeyPairGenerator();
        KeyPair keyPair = generatorWithEd25519.generateKeyPair();
        publicKey = keyPair.getPublic();
        secret = keyPair.getPrivate();
    }

    public String getPublicKey() {
        byte[] encoded = publicKey.getEncoded();
        return Hex.toHexString(encoded);
    }

    public String getSecretInHexString() {
        byte[] encoded = secret.getEncoded();
        return Hex.toHexString(encoded);
    }

    @Override
    public String getScheme() {
        return "ed25519";
    }

    @Override
    public String getPublic() {
        return getPublicKey();
    }
    @Override
    public String getSecret(){
        return getSecretInHexString();
    }

    public PrivateKey getPrivateKey() {
        return secret;
    }


    public SignatureDto getBasicSignatureDto(String signerAddress){
        SignatureDto signatureDto = new SignatureDto();
        signatureDto.setPublic(this.getPublicKey());
        signatureDto.setScheme("ed25519");
        signatureDto.setSigner(signerAddress);
        return signatureDto;
    }

    public class KeyPairHolderDto{
        private String secret;
        @SerializedName("public")
        private String publico;
        private String scheme;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getPublico() {
            return publico;
        }

        public void setPublico(String publico) {
            this.publico = publico;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        @Override
        public String toString() {
            Gson gson = (new GsonBuilder()).create();

            return gson.toJson(this);
        }
    }


    public KeyPairHolderDto getDtoForJson() {
        KeyPairHolderDto keyPairHolderDto = new KeyPairHolderDto();
        keyPairHolderDto.setScheme("ed25519");
        keyPairHolderDto.setPublico(this.getPublicKey());
        keyPairHolderDto.setSecret(this.getSecretInHexString());
        return keyPairHolderDto;
    }
}
