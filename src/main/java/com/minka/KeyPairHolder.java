package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.minka.wallet.SignatureDto;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.spongycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/***
 * It returns a key-pair generated using Ed25519
 */
public class KeyPairHolder implements com.minka.wallet.primitives.KeyPair{

    private PublicKey publicKey;
    private String seed;
    private PrivateKey secret;
    private String groupA;

    private static final EdDSANamedCurveSpec ed25519 = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);

    public KeyPairHolder(String privateKeyHex){
        byte[] privateKey = Utils.hexToBytes(privateKeyHex);
        EdDSAPrivateKeySpec key = new EdDSAPrivateKeySpec(privateKey, ed25519);
        secret = new EdDSAPrivateKey(key);
        seed = privateKeyHex;
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(key.getA(), ed25519);
        groupA = Utils.bytesToHex(pubKey.getA().toByteArray());

        publicKey = new EdDSAPublicKey(pubKey);
    }

    public KeyPairHolder(){

        KeyPairGenerator generatorWithEd25519 = new KeyPairGenerator();
        KeyPair keyPair = generatorWithEd25519.generateKeyPair();
        publicKey = keyPair.getPublic();
        secret = keyPair.getPrivate();
    }

    public String getPublicKey() {
        byte[] encoded = this.publicKey.getEncoded();
        return groupA;
    }

    public PublicKey getPublicKeyOriginal(){
        return this.publicKey;
    }
    public String getSecretInHexString() {
        byte[] encoded = secret.getEncoded();
        return Hex.toHexString(encoded);
    }

    @Override
    public String getScheme() {
        return "eddsa-ed25519";
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
        signatureDto.setScheme("eddsa-ed25519");
        signatureDto.setSigner(signerAddress);
        return signatureDto;
    }

    @Override
    public String toJson() {
        return this.getDtoForJson().toString();
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
        keyPairHolderDto.setScheme("eddsa-ed25519");
        keyPairHolderDto.setPublico(this.getPublicKey());
        keyPairHolderDto.setSecret(this.getSecretInHexString());
        return keyPairHolderDto;
    }
}
