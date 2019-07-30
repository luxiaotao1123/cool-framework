package com.core.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * radix tools
 * Created by vincent on 2018/10/6
 */
public class RadixTools {

    public static void main(String[] args) {
        String s = RadixTools.toBinaryString((byte) 1);
        System.out.println(s);
        for(int i=s.length()-1;i>=0;i--){
            char c=s.charAt(i);
            if (i == 7 && c =='1'){
                System.out.println("===");
            }
        }
    }

    /************************************** BinaryString **********************************************/

    public static String toBinaryString(byte b){
        return Long.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }

    public static String toBinaryString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Long.toBinaryString((aByte & 0xFF) + 0x100).substring(1));
        }
        return sb.toString();
    }

    /************************************** HexString **********************************************/

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String bytesToHexStr(byte[] bytes){
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) {
            buf.append(String.format("%02x", b & 0xff));
        }

        return buf.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /************************************** String **********************************************/

    public static String bytesToStr(byte[] bytes){
        return bytesToStr(bytes, Charset.forName("gbk"));
    }

    public static String bytesToStr(byte[] bytes, Charset charset){
        return new String(bytes, charset).trim().toUpperCase();
    }

    public static byte[] strToBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("gbk");
    }

    /************************************** long **********************************************/

    public static byte[] longToBytes(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    public static long bytesToLong(byte[] bytes) {
        long s = 0;
        long s0 = bytes[0] & 0xff;// 最低位
        long s1 = bytes[1] & 0xff;
        long s2 = bytes[2] & 0xff;
        long s3 = bytes[3] & 0xff;
        long s4 = bytes[4] & 0xff;// 最低位
        long s5 = bytes[5] & 0xff;
        long s6 = bytes[6] & 0xff;
        long s7 = bytes[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }


    /************************************** int **********************************************/

    public static byte[] intToBytes(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }


    public static int bytesToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    /************************************** short **********************************************/

    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();//将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

}
