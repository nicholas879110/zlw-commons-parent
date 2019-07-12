package com.zlw.commons.security.encode;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/8/4
 */
public class Hex {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Hex() {
    }

    public static char[] encode(byte[] bytes) {
        int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;

        for(int i = 0; i < nBytes; ++i) {
            result[j++] = HEX[(240 & bytes[i]) >>> 4];
            result[j++] = HEX[15 & bytes[i]];
        }

        return result;
    }

    public static byte[] decode(CharSequence s) {
        int nChars = s.length();
        if(nChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        } else {
            byte[] result = new byte[nChars / 2];

            for(int i = 0; i < nChars; i += 2) {
                int msb = Character.digit(s.charAt(i), 16);
                int lsb = Character.digit(s.charAt(i + 1), 16);
                if(msb < 0 || lsb < 0) {
                    throw new IllegalArgumentException("Non-hex character in input: " + s);
                }

                result[i / 2] = (byte)(msb << 4 | lsb);
            }

            return result;
        }
    }
}
