package com.minka;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.EdDSASecurityProvider;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import org.apache.commons.codec.DecoderException;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class UtilTesting

{

    @Test
    public void SignatureMessage()  throws Exception
    {
        String message = "This is a message";
        String hashedMessage = HashingUtil.hashWithRipemd160(message, StandardCharsets.UTF_8);
        KeyPairHolder keyPairHolder = new KeyPairHolder();
        String signedMessage = SignatureUtil.signWithEd25519(hashedMessage, keyPairHolder.getPrivateKey());


        System.out.println(signedMessage);
    }

    @Test
    public void SignatureBack() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyPairHolder sourcekeyPairHolder = new KeyPairHolder();

        String secret = sourcekeyPairHolder.getSecret();
        System.out.println("secret:" + secret);

//        String secret = "0e93ce131ff69320f67f4c60b41976b28e735aaf83407b03dff9ec53c9bd126e";
//        byte[] privateKeyBytes = Hex.decode(secret);
//        byte[] privateKeyBytes = secret.getBytes();
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec("010c2658713ab0853a515f47dcfcbdddc736fe6ff0de85793fd0c1d84e8b67aa".getBytes());
//        EdDSAPrivateKey keyIn = new EdDSAPrivateKey(encoded);
//        String secretBack = Hex.toHexString(keyIn.getEncoded());
//
//        System.out.println("secret:" + secretBack);
//        Assert.assertEquals(secret, secretBack);

    }
    @Test
    public void HashingUtils()  throws Exception {
        String data = "asdf";
        String sha256 = HashingUtil.hashWithsha256(data,null);
        System.out.println(sha256);

        String ripemd160 = HashingUtil.hashWithRipemd160(data, StandardCharsets.UTF_8);
        System.out.println(ripemd160);

        String hash = HashingUtil.createHash(data);

        System.out.println(hash);

    }

    @Test
    public void KeyPairGeneration()  throws Exception {

        KeyPairHolder keyPair = new KeyPairHolder();
//        System.out.println("Public key");
//        System.out.println(keyPair.getPublicKey().length());
//        System.out.println("Private key encoded in base64");
//        PrivateKey privateKey = keyPair.getPrivateKey();
//        String s = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//        System.out.println(privateKey.);

        System.out.println("Private key encoded in hex string");
        System.out.println(keyPair.getSecretInHexString().length());

//        System.out.println("Public key encoded in base64");
//        PublicKey publicKey = keyPair.getPublicKeyOriginal();
//        String spk = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        System.out.println(spk.length());

        System.out.println("Public key encoded in hex string");
        System.out.println(keyPair.getPublic().length());
    }

    @Test
    public void tempo() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new EdDSASecurityProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EdDSA", "EdDSA");

        KeyPair keyPair = keyGen.genKeyPair();
        EdDSAPrivateKey aPrivate = (EdDSAPrivateKey) keyPair.getPrivate();
        char[] chars = org.apache.commons.codec.binary.Hex.encodeHex(aPrivate.getAbyte());
        String result = new String(chars);
        System.out.println(result.length());

        try {
            Assert.assertArrayEquals(result.toCharArray(), chars);
            byte[] bytes = org.apache.commons.codec.binary.Hex.decodeHex(result.toCharArray());
            Assert.assertArrayEquals(bytes, aPrivate.getAbyte());

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
            EdDSAPrivateKey keyIn = new EdDSAPrivateKey(pkcs8EncodedKeySpec) ;
        } catch (DecoderException | InvalidKeySpecException e) {
            e.printStackTrace();
        }


//        try {

//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }

    }
    @Test
    public void tempoPublic() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new EdDSASecurityProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EdDSA", "EdDSA");

        KeyPair keyPair = keyGen.genKeyPair();
        EdDSAPublicKey publicKey = (EdDSAPublicKey) keyPair.getPublic();
//        "kpub=" +
//                String s = Base64.getEncoder().encodeToString(publicKey.);

        System.out.println(publicKey.getFormat());

//        System.out.println(s.length());
//        char[] chars = org.apache.commons.codec.binary.Hex.encodeHex(publicKey.getAbyte());
//        String result = new String(chars);
//        System.out.println(result);
//        System.out.println(result.length());


    }

    @Test
    public void fixMessageUnilink(){
        String xml = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header/><S:Body><ns0:receptor xmlns:ns0=\"http://service.ws.cliente.tesabiz.com/\"><parameters>&lt;ach_destinatario&gt;&lt;ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"&gt;&lt;ds:SignedInfo&gt;&lt;ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/&gt;&lt;ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/&gt;&lt;ds:Reference URI=\"#123456+-+Wed+Feb+06+08%3A54%3A47+BOT+2019\"&gt;&lt;ds:Transforms&gt;&lt;ds:Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/&gt;&lt;/ds:Transforms&gt;&lt;ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/&gt;&lt;ds:DigestValue&gt;sfoJ4YjN2TcYgDrDVZl9ky70a9sZEd7mxo1F1p1k03I=&lt;/ds:DigestValue&gt;&lt;/ds:Reference&gt;&lt;/ds:SignedInfo&gt;&lt;ds:SignatureValue&gt;eDvR2cXiqrTDOeoWhVBBAsU0oAmjzaOi/ydXOTLj+NQ+aqlhJHvKmN8jgsEr7wZqCOKXClaHLF39\n" +
                "nyvAXSipxVMzwv4pFNotEFJKm+Yh8LL7t7EttLCF51bM3ar32YLj+1b6sqWpKFx86PMhbtvx4GE7\n" +
                "3vLdEi7CIhwrU+p5Ifhf/4aK32/kukTYL2a4oQbs2axRwDBmboOB+ACLZEpZl/E9Ry1UGtX5A1HH\n" +
                "66GGEby8MiA6H/n6InneQOU2YcRQM0TdLsrS27WoMJDNR/d+YRGCvgccO3i+V5owp6odi9wow0/a\n" +
                "QKUWB2bn21pCNDVzrP8VBt+YoO4IgfVPExGKhQ==&lt;/ds:SignatureValue&gt;&lt;ds:KeyInfo&gt;&lt;ds:KeyName&gt;123456&lt;/ds:KeyName&gt;&lt;/ds:KeyInfo&gt;&lt;ds:Object Id=\"123456+-+Wed+Feb+06+08%3A54%3A47+BOT+2019\"&gt;&amp;lt;ach_destinatario_originalfc&amp;gt;&amp;lt;num_orden_originante&amp;gt;16031901300005&amp;lt;/num_orden_originante&amp;gt;&amp;lt;num_orden_ach&amp;gt;201901000000020&amp;lt;/num_orden_ach&amp;gt;&amp;lt;cod_camara&amp;gt;&amp;lt;/cod_camara&amp;gt;&amp;lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&amp;gt;&amp;lt;soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&amp;gt;&amp;lt;soapenv:Body&amp;gt;&amp;lt;receptor xmlns=\"http://servicios.sicom.ws.unilink.com.bo/\"&amp;gt;&amp;lt;tramaEntrada xmlns=\"\"&amp;gt;&amp;amp;lt;?xml version=&amp;amp;quot;1.0&amp;amp;quot; encoding=&amp;amp;quot;UTF-8&amp;amp;quot;?&amp;amp;gt;&amp;amp;lt;raiz&amp;amp;gt;&amp;amp;lt;ds:Signature xmlns:ds=&amp;amp;quot;http://www.w3.org/2000/09/xmldsig#&amp;amp;quot;&amp;amp;gt;&amp;amp;lt;ds:SignedInfo&amp;amp;gt;&amp;amp;lt;ds:CanonicalizationMethod Algorithm=&amp;amp;quot;http://www.w3.org/TR/2001/REC-xml-c14n-20010315&amp;amp;quot;/&amp;amp;gt;&amp;amp;lt;ds:SignatureMethod Algorithm=&amp;amp;quot;http://www.w3.org/2001/04/xmldsig-more#rsa-sha256&amp;amp;quot;/&amp;amp;gt;&amp;amp;lt;ds:Reference URI=&amp;amp;quot;#1603 - Wed Jan 30 11:59:00 BOT 2019&amp;amp;quot;&amp;amp;gt;&amp;amp;lt;ds:Transforms&amp;amp;gt;&amp;amp;lt;ds:Transform Algorithm=&amp;amp;quot;http://www.w3.org/TR/2001/REC-xml-c14n-20010315&amp;amp;quot;/&amp;amp;gt;&amp;amp;lt;/ds:Transforms&amp;amp;gt;&amp;amp;lt;ds:DigestMethod Algorithm=&amp;amp;quot;http://www.w3.org/2001/04/xmlenc#sha256&amp;amp;quot;/&amp;amp;gt;&amp;amp;lt;ds:DigestValue&amp;amp;gt;owWDC1Nf4H3o9IUfQMskcSZienpNONrUSmXaWSp0DIs=&amp;amp;lt;/ds:DigestValue&amp;amp;gt;&amp;amp;lt;/ds:Reference&amp;amp;gt;&amp;amp;lt;/ds:SignedInfo&amp;amp;gt;&amp;amp;lt;ds:SignatureValue&amp;amp;gt;zY7J51O4pBwktVxEKKJ8D+DDiJLlW8PA3LRiWwa1NnHcaZ13+vWo96LFR0ILFtobKDmbJGx8vZFyyeDj8/nV5hxTQKu2iV3/nAMsMCMZMsg+rtbFdI+ohNDTB9vfkpNPiEtHYldIrsZt4+rbIDLMgmy6mD3wsp6ELNRRhWYrdJW0aeI5zInPvolTNttkB1cLY0SfkmXAvSEt3l9plv9aG9mdEeoGwv713l8BVwAGXP0T5S/q4sdWm2tkVqZ1IKVRVIGZAHFHY0ek5u5WPT1MJuEQAfk8mRO7s2H71s38svqIcKS12t/u7XcDJO1MWwuzvRWT1Uv11+Laao6IijZ44w==&amp;amp;lt;/ds:SignatureValue&amp;amp;gt;&amp;amp;lt;ds:KeyInfo&amp;amp;gt;&amp;amp;lt;ds:KeyName&amp;amp;gt;1603&amp;amp;lt;/ds:KeyName&amp;amp;gt;&amp;amp;lt;/ds:KeyInfo&amp;amp;gt;&amp;amp;lt;ds:Object Id=&amp;amp;quot;1603 - Wed Jan 30 11:59:00 BOT 2019&amp;amp;quot;&amp;amp;gt;&amp;amp;lt;Originante_ACH&amp;amp;gt;&amp;amp;lt;num_orden_originante&amp;amp;gt;16031901300005&amp;amp;lt;/num_orden_originante&amp;amp;gt;&amp;amp;lt;cod_pais_originante&amp;amp;gt;BO&amp;amp;lt;/cod_pais_originante&amp;amp;gt;&amp;amp;lt;cod_sucursal_originante&amp;amp;gt;LPZ&amp;amp;lt;/cod_sucursal_originante&amp;amp;gt;&amp;amp;lt;cod_destinatario&amp;amp;gt;1602&amp;amp;lt;/cod_destinatario&amp;amp;gt;&amp;amp;lt;cod_pais_destinatario&amp;amp;gt;BO&amp;amp;lt;/cod_pais_destinatario&amp;amp;gt;&amp;amp;lt;cod_moneda&amp;amp;gt;BOB&amp;amp;lt;/cod_moneda&amp;amp;gt;&amp;amp;lt;importe&amp;amp;gt;200.00&amp;amp;lt;/importe&amp;amp;gt;&amp;amp;lt;tipo_documento&amp;amp;gt;PRIVADO&amp;amp;lt;/tipo_documento&amp;amp;gt;&amp;amp;lt;tip_orden&amp;amp;gt;200&amp;amp;lt;/tip_orden&amp;amp;gt;&amp;amp;lt;cod_procedimiento&amp;amp;gt;50&amp;amp;lt;/cod_procedimiento&amp;amp;gt;&amp;amp;lt;tip_cuenta_origen&amp;amp;gt;CCAO&amp;amp;lt;/tip_cuenta_origen&amp;amp;gt;&amp;amp;lt;tip_cuenta_destino&amp;amp;gt;CCAD&amp;amp;lt;/tip_cuenta_destino&amp;amp;gt;&amp;amp;lt;fec_camara&amp;amp;gt;2019-01-30&amp;amp;lt;/fec_camara&amp;amp;gt;&amp;amp;lt;cuenta_origen&amp;amp;gt;160300001&amp;amp;lt;/cuenta_origen&amp;amp;gt;&amp;amp;lt;cuenta_destino&amp;amp;gt;160200001&amp;amp;lt;/cuenta_destino&amp;amp;gt;&amp;amp;lt;ci_nit_originante&amp;amp;gt;3977081&amp;amp;lt;/ci_nit_originante&amp;amp;gt;&amp;amp;lt;titular_originante&amp;amp;gt;Victor Hugo Vera&amp;amp;lt;/titular_originante&amp;amp;gt;&amp;amp;lt;ci_nit_destinatario&amp;amp;gt;39770811&amp;amp;lt;/ci_nit_destinatario&amp;amp;gt;&amp;amp;lt;titular_destinatario&amp;amp;gt;Ruben Prado&amp;amp;lt;/titular_destinatario&amp;amp;gt;&amp;amp;lt;glosa&amp;amp;gt;test de prueba&amp;amp;lt;/glosa&amp;amp;gt;&amp;amp;lt;cod_compensador&amp;amp;gt;1004&amp;amp;lt;/cod_compensador&amp;amp;gt;&amp;amp;lt;cod_sub_destinatario&amp;amp;gt;1602&amp;amp;lt;/cod_sub_destinatario&amp;amp;gt;&amp;amp;lt;cod_sub_originante&amp;amp;gt;1603&amp;amp;lt;/cod_sub_originante&amp;amp;gt;&amp;amp;lt;origen_fondos&amp;amp;gt;ahorros&amp;amp;lt;/origen_fondos&amp;amp;gt;&amp;amp;lt;destino_fondos&amp;amp;gt;Ahorros&amp;amp;lt;/destino_fondos&amp;amp;gt;&amp;amp;lt;canal&amp;amp;gt;VEN&amp;amp;lt;/canal&amp;amp;gt;&amp;amp;lt;/Originante_ACH&amp;amp;gt;&amp;amp;lt;/ds:Object&amp;amp;gt;&amp;amp;lt;/ds:Signature&amp;amp;gt;&amp;amp;lt;etc/&amp;amp;gt;&amp;amp;lt;/raiz&amp;amp;gt;&amp;lt;/tramaEntrada&amp;gt;&amp;lt;/receptor&amp;gt;&amp;lt;/soapenv:Body&amp;gt;&amp;lt;/soapenv:Envelope&amp;gt;&amp;lt;/ach_destinatario_original&amp;gt;&lt;/ds:Object&gt;&lt;/ds:Signature&gt;&lt;/ach_destinatario&gt;</parameters></ns0:receptor></S:Body></S:Envelope>\n";

        String fixedXml = xml.replaceAll("&amp;lt;", "&lt;")
                            .replaceAll("&amp;gt;","&gt;")
                            .replaceAll("&amp;amp;lt;","&lt;");

        System.out.println(fixedXml);
    }
}
