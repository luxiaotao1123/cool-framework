package com.core.common;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * aes128-base64
 * @author Vincent
 */
public class AesUtils {

    private final static String DEFAULT_CHARSET = "utf-8";
    private final static int DEFAULT_KEY_LENGTH = 16;

    /**
     * aes128 - base64 加密
     * @param data  需要加密的明文
     * @param key   盐
     * @return   密文
     */
    public static String encrypt(String data, String key) {
        try {
            if (null == key || "".equals(key) || key.length() != DEFAULT_KEY_LENGTH) {
                return null;
            }
            byte[] raw = key.getBytes(DEFAULT_CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(DEFAULT_CHARSET));
            return RadixTools.bytesToHexStr(encrypted);
        } catch (Exception ex) {
            return null;
        }

    }

    /**
     * aes128 - base64 解密
     * @param data  需要解密的密文
     * @param key   盐
     * @return   明文
     */
    public static String decrypt(String data, String key) {
        try {
            if (null == key || "".equals(key) || key.length() != DEFAULT_KEY_LENGTH) {
                return null;
            }
            byte[] raw = key.getBytes(DEFAULT_CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(RadixTools.hexStringToBytes(data));
            return new String(original,DEFAULT_CHARSET);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        //test
        String key = "123456";
//        String jsonData = "json={\"status\":200,\"msg\":\"success\",\"data\":\"dsadsa\"}&timestamp=" + String.valueOf(System.currentTimeMillis()+5000000L);
////        String jsonData = "status=200&msg=xltys1995==sadsadsad&timestamp=" + String.valueOf(System.currentTimeMillis()+5000000L);
//        System.out.println(System.currentTimeMillis() + 5000000L);
//        String encrypt = encrypt(key, jsonData);
//        System.out.println(encrypt);
//        String key = "QeCB1d74ab24482b";
//        String s = decrypt("bd064484343cde2d325693c0611c157d04294ae2cea03854d10a2f0aa01377cfc69cf6c700ae665c8f4c539d030bb2af"
//                , key);
//        System.out.printf(s);


        String data = "15988786205&timestamp=" + (System.currentTimeMillis() + 5000000L);
//        String jsonData = "status=200&msg=xltys1995==sadsadsad&timestamp=" + String.valueOf(System.currentTimeMillis()+5000000L);
        System.out.println(System.currentTimeMillis() + 5000000L);
        String encrypt = encrypt(data,key);
        System.out.println(encrypt);
    }
}
