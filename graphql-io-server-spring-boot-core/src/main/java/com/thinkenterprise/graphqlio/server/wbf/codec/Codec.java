package com.thinkenterprise.graphqlio.server.wbf.codec;

/**
 * Codec
 */
public interface Codec {

   String decode(byte[] data);
   byte[] encode(String data);
    

}