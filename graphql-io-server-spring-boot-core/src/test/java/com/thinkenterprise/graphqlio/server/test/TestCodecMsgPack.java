package com.thinkenterprise.graphqlio.server.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.thinkenterprise.graphqlio.server.wsf.codec.MsgPackCodec;

public class TestCodecMsgPack {

	private static final MsgPackCodec codec = new MsgPackCodec();

	@Test
	public void testEncode() throws IOException {
		testEncode(null, 1);
		testEncode("", 1);
		testEncode("{}", 3);
		testEncode("{a:'x'}", 8);
		testEncode("{ a: 'x', b: 23, f2: 'äöü'}", 31);
		testEncode("{ a: 32423, b: 22453, f2: 235235235.234}", 42);
		testEncode(
				"{ afvsdfd: 32423, vsdfsdb: 22453, f2sddfdf: 235235235.234, adfsdf: { afvsdfd: 32423, vsdfsdb: 22453, f2sddfdf: 235235235.234 } }",
				130);
	}

	private void testEncode(String input, int length) throws IOException {
		byte[] result = codec.encode(input);
		// System.out.println("result.len = " + result.length);
		Assert.assertTrue(result.length == length);
	}

	@Test
	public void testDecode() throws IOException {
		testDecodeNull(null);
		testDecode("");
		testDecode("{}");
		testDecode("{a:'x'}");
		testDecode("{ a: 'x', b: 23, f2: 'äöü'}");
		testDecode("{ a: 32423, b: 22453, f2: 235235235.234}");
		testDecode(
				"{ afvsdfd: 32423, vsdfsdb: 22453, f2sddfdf: 235235235.234, adfsdf: { afvsdfd: 32423, vsdfsdb: 22453, f2sddfdf: 235235235.234 } }");
	}

	private void testDecodeNull(String input) throws IOException {
		byte[] data = codec.encode(input);
		String result = codec.decode(data);

		Assert.assertEquals(input, null);
		Assert.assertEquals(result, null);
		Assert.assertNotNull(data);
		Assert.assertTrue(input == result);
	}

	private void testDecode(String input) throws IOException {
		byte[] data = codec.encode(input);
		String result = codec.decode(data);
		Assert.assertEquals(input, result);
		Assert.assertTrue(input.contentEquals(result));
	}

}
