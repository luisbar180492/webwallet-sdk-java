package com.minka;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import java.security.KeyPair;
import java.security.Key;
import java.util.Base64;
import java.math.BigInteger;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import net.i2p.crypto.eddsa.EdDSAEngine; 

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testKeyPairGenerator()  throws Exception
    {
	System.out.println("KeyPairGenerator");
        KeyPairGenerator generator = new KeyPairGenerator();
	
	
	KeyPair keyPair = generator.generateKeyPair();
       
      PrivateKey privateKey = keyPair.getPrivate();
    Key publicKey = keyPair.getPublic(); 
        System.out.println( "Private:" + keyPair.getPrivate() );

        System.out.println( "Algorithm:" + keyPair.getPublic().getAlgorithm() );
  	
	   Base64.Encoder encoder = Base64.getEncoder();
    String privateKeyBase64Str = encoder.encodeToString(privateKey.getEncoded());
    System.out.println("Private key in Base64 format:\n" + privateKeyBase64Str);//it creates 1623 chars or 1620 chars
    
    Base64.Decoder decoder = Base64.getDecoder();
    byte[] privateKeyBytes = decoder.decode(privateKeyBase64Str);
    System.out.println("The private Key is " + privateKeyBytes.length + " bytes long");
    String privateKeyHex = String.format("%040x", new BigInteger(1, privateKeyBytes));
    System.out.println("The private key in hexadecimal digits:\n" + privateKeyHex);
    
	//TODO MAKE it randowm to have a differente outcome every execution          
        
	//SIGNATURE
         

	EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
	
        Signature sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm())); 
	
	sgr.initSign(privateKey);
	
	String hashMessageExample = "someHashMEssage";

	sgr.update(hashMessageExample.getBytes());
	
	System.out.println("SIGNATURE:" + sgr.sign());
	assertTrue( true );
    }
	
}
