package com.thinkenterprise.graphqlio.server.wsf.codec;

/**
 * Codec
 */
public interface Codec {

   String decode(byte[] data);
   byte[] encode(String data);
    

}