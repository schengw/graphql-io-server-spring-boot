package com.thinkenterprise.graphqlio.server.wsf.codec;

/**
 * Codec
 */
public interface Codec {

	String decode(byte[] data) throws Exception;

	byte[] encode(String data) throws Exception;

}