package com.nil.code.libs.util;

import android.util.Base64;

/**
 * Base64编码管理
 */
public class Base64Util {
    /**
     * CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
     */
    public final static int CRLF= Base64.CRLF;
    /**
     * DEFAULT 这个参数是默认，使用默认的方法来加密


     */
    public final static int DEFAULT= Base64.DEFAULT;
    /**
     * NO_PADDING 这个参数是略去加密字符串最后的”=”
     */
    public final static int NO_PADDING= Base64.NO_PADDING;
    /**
     * ???
     */
    public final static int NO_CLOSE= Base64.NO_CLOSE;
    /**
     * NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
     */
    public final static int NO_WRAP= Base64.NO_WRAP;
    /**
     * URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
     */
    public final static int URL_SAFE= Base64.URL_SAFE;
    /**
     * 对字符串进行base64的编码处理.
     * @param encodeString 待编码字符串
     * @return byte[] base64编码字节数组
     */
    public static byte[] encode(String encodeString) {
        return encode(encodeString.getBytes());
    }
    /**
     * 对字节数组进行base64编码处理.
     * @param encodeBytes 待编码字节数组
     * @return byte[]   base64编码字节数组
     */
    public static byte[] encode(byte[] encodeBytes) {
        return Base64.encode(encodeBytes, Base64.DEFAULT);
    }
    /**
     * 对base64字节数组进行反base64编码处理.
     * @param encodeBytes 待反编码字节数组
     * @return byte[]   base64编码字节数组
     */
    public static byte[] decode(byte[] encodeBytes) {
        return Base64.decode(new String(encodeBytes), Base64.DEFAULT);
    }
    /**
     * 对base64字符串进行反base64编码处理.
     * @param encodeString 待反编码字符串
     * @return byte[]   base64编码字节数组
     */
    public static byte[] decode(String encodeString) {
        return Base64.decode(encodeString, Base64.DEFAULT);
    }
}
