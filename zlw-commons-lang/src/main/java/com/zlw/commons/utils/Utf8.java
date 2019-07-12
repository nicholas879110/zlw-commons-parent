package com.zlw.commons.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/8/4
 */
public class Utf8 {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public Utf8() {
    }

    public static byte[] encode(CharSequence string) {
        try {
            ByteBuffer e = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[e.limit()];
            System.arraycopy(e.array(), 0, bytesCopy, 0, e.limit());
            return bytesCopy;
        } catch (CharacterCodingException var3) {
            throw new IllegalArgumentException("Encoding failed", var3);
        }
    }

    public static String decode(byte[] bytes) {
        try {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException var2) {
            throw new IllegalArgumentException("Decoding failed", var2);
        }
    }
}
