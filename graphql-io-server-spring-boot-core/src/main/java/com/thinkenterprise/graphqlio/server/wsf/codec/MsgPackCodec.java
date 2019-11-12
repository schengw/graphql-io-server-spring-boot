package com.thinkenterprise.graphqlio.server.wsf.codec;

import java.io.IOException;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 * CBORCodec
 */
public class MsgPackCodec implements Codec {

	@Override
	public String decode(byte[] data) throws IOException {
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);
		String result;
		if (unpacker.tryUnpackNil()) {
			result = null; // unpacker.unpackNil();
		} else {
			result = unpacker.unpackString();
		}
		unpacker.close();
		return result;
	}

	@Override
	public byte[] encode(String data) throws IOException {
		MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
		if (data == null) {
			packer.packNil();
		} else {
			packer.packString(data);
		}
		packer.close();
		return packer.toByteArray();
	}

}