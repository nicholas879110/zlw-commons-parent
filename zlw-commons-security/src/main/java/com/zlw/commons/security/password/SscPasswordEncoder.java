package com.zlw.commons.security.password;

import com.zlw.commons.security.encode.Hex;
import com.zlw.commons.security.encode.Utf8;
import com.zlw.commons.security.util.PasswordEncoderUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/8/4
 */
public class SscPasswordEncoder {

    private final String algorithm;
    private int iterations;


    public SscPasswordEncoder(String algorithm) throws IllegalArgumentException {
        this.iterations = 1;
        this.algorithm = algorithm;
        this.getMessageDigest();
    }

    public String encodePassword(String rawPass, Object salt) {
        String saltedPass = this.mergePasswordAndSalt(rawPass, salt, false);
        MessageDigest messageDigest = this.getMessageDigest();
        byte[] digest = messageDigest.digest(Utf8.encode(saltedPass));

        for(int i = 1; i < this.iterations; ++i) {
            digest = messageDigest.digest(digest);
        }

        return new String(Hex.encode(digest));
    }

    protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalArgumentException("No such algorithm [" + this.algorithm + "]");
        }
    }

    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        String pass1 = "" + encPass;
        String pass2 = this.encodePassword(rawPass, salt);
        return PasswordEncoderUtils.equals(pass1, pass2);
    }

    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if(password == null) {
            password = "";
        }

        if(strict && salt != null && (salt.toString().lastIndexOf("{") != -1 || salt.toString().lastIndexOf("}") != -1)) {
            throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
        } else {
            return salt != null && !"".equals(salt)?password + "{" + salt.toString() + "}":password;
        }
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }


    public static void main(String[] args) {
        SscPasswordEncoder encoder=new SscPasswordEncoder("MD5");
        encoder.setIterations(4);
        System.out.println(encoder.encodePassword("123456", "ZY040401"));
    }
}
