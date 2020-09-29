package com.my.base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Test {


    @Test
    public void TestEncode() throws UnsupportedEncodingException {
        byte[] encode = Base64.getEncoder().encode("abcdefg".getBytes());
        String encodestr=new String(encode,"UTF-8");
        System.out.println("加密后的密文"+encodestr);
    }

   @Test
    public void TestDecode() throws UnsupportedEncodingException{
        String encodeStr="YWJjZGVmZw==";
       byte[] decode = Base64.getDecoder().decode(encodeStr);
       String s = new String(decode, "UTF-8");
       System.out.println(s);
   }
}
