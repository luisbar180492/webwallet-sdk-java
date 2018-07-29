package com.minka.wallet.primitives;

import org.apache.commons.codec.DecoderException;

import java.security.NoSuchAlgorithmException;

/**
 * Address interface
 */
public interface Address {

    String encode() throws DecoderException, NoSuchAlgorithmException;
}
