package edu.hufe.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;

/**
 * BASE64
 * @author zyj
 */

public class Base64Decoder {

    /**
     * Base64编码
     * @param data
     * @return
     */
    public static String getBASE64(byte[] data){
        return new BASE64Encoder().encode(data);
    }

    /**
     * Base64解码
     * @param str
     * @return
     */
    public static String decode(String str){
        byte[] data = null;
        if(str != null){
            try {
                BASE64Decoder decoder = new BASE64Decoder();
                data = decoder.decodeBuffer(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String result = null;
        if(data != null){
            result = new String(data);
        }
        return result;
    }
}
