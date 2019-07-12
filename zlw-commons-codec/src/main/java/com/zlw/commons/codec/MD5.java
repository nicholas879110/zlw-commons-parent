package com.zlw.commons.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * MD5加密工具
 *
 * @author fukui
 */
public class MD5 {

    private static final Logger log = LoggerFactory.getLogger( MD5.class);



    public static String EncoderByMd5(String buf) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] rs = digist.digest(buf.getBytes("UTF-8"));
            StringBuffer digestHexStr = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }


    /**
     * 加盐的md5值。这样即使被拖库，仍然可以有效抵御彩虹表攻击
     *
     * @param inbuf 需做md5的字符串
     * @return
     */
    public static String encodeByMd5AndSalt(String inbuf, String salt) {
        if (salt == null) {
            return EncoderByMd5(inbuf);
        } else {
            return EncoderByMd5(inbuf+ salt);
        }
    }

    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
    
    public static void main(String[] args) {
        System.out.println("MD5:"+MD5.EncoderByMd5("benedict0909") );
    }

}
