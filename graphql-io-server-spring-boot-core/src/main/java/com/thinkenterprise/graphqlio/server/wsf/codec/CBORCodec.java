package com.thinkenterprise.graphqlio.server.wsf.codec;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

/**
 * CBORCodec
 */
public class CBORCodec implements Codec {

	private static final ObjectMapper mapper = new ObjectMapper(new CBORFactory());

	@Override
	public String decode(byte[] data) throws JsonParseException, JsonMappingException, IOException {
		String result = mapper.readValue(data, String.class);
		return result;
	}

	@Override
	public byte[] encode(String data) throws JsonProcessingException {
		byte[] cborData = mapper.writeValueAsBytes(data);
		return cborData;
	}

}