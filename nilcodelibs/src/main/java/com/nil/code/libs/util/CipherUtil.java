package com.nil.code.libs.util;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mwh on 2017/6/14.
 */

public class CipherUtil {

    // 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
    // AES/CBC/NoPadding             16                          不支持
    // AES/CBC/PKCS5Padding          32                          16
    // AES/CBC/ISO10126Padding       32                          16
    // AES/CFB/NoPadding             16                          原始数据长度
    // AES/CFB/PKCS5Padding          32                          16
    // AES/CFB/ISO10126Padding       32                          16
    // AES/ECB/NoPadding             16                          不支持
    // AES/ECB/PKCS5Padding          32                          16
    // AES/ECB/ISO10126Padding       32                          16
    // AES/OFB/NoPadding             16                          原始数据长度
    // AES/OFB/PKCS5Padding          32                          16
    // AES/OFB/ISO10126Padding       32                          16
    // AES/PCBC/NoPadding            16                          不支持
    // AES/PCBC/PKCS5Padding         32                          16
    // AES/PCBC/ISO10126Padding      32                          16

    //要求密钥长度为16的倍数  密文长度为任意
    public static final String OFB_NOPADDING = "AES/OFB/NoPadding";

    //要求密钥长度为16的倍数  密文长度为任意
    public static final String CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    public static final String ZERO_BYTEPADDING = "AES/ECB/ZeroBytePadding";

    public static final String ECB_PKCS5PADDING="AES/ECB/PKCS5Padding";

    //生成：cell的密码值 加密
    public static String encrypt(String normalString, String method, String key) {
        String encryData = null;
        try {
            if (!TextUtils.isEmpty(normalString)) {
                byte[] encryBytes = encrypt(normalString.getBytes(), method, key);
                if (encryBytes != null) {
//                    encryData = StringUtils.bytesToHexString(encryBytes);
                    encryData = Base64.encodeToString(encryBytes, Base64.DEFAULT);
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return encryData;
    }

    public static String encrypt(String normalString, String method, String key, String ivParams) {
        String encryData = null;
        try {
            if (!TextUtils.isEmpty(normalString)) {
                byte[] encryBytes = encrypt(normalString.getBytes(), method, key, ivParams);
                if (encryBytes != null) {
//                    encryData = StringUtils.bytesToHexString(encryBytes);
                    encryData = Base64.encodeToString(encryBytes, Base64.DEFAULT);
                }
            }

        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return encryData;
    }

    public static byte[] encrypt(byte[] normalBytes, String method, String key) {
        return encrypt(normalBytes, method, key, getIV(""));
    }

    /**
     * 加密方法
     *
     * @param normalBytes 待加密数据
     * @param method      数据加密的方式
     * @param key         密钥
     * @param ivParameter 向量  向量的长度必须是16位
     * @return
     */
    public static byte[] encrypt(byte[] normalBytes, String method, String key, String ivParameter) {
        byte[] arrayOfByte = null;
        try {
            if (TextUtils.isEmpty(key)) {
                throw new RuntimeException("the key is empty!!");
            }

            if (TextUtils.isEmpty(ivParameter) || ivParameter.length() != 16) {
                throw new RuntimeException("the iv is empty or iv length is not 16");
            }
            //获取密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            //初始化加密类型
            Cipher localCipher = Cipher.getInstance(method);
            //初始化数据处理方式
            if (method.contains("ECB")) {
                localCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } else {
                //获取向量
                IvParameterSpec paramSpec = new IvParameterSpec(ivParameter.getBytes());
                localCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
            }

            //执行加密
            arrayOfByte = localCipher.doFinal(normalBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayOfByte;
    }

    //解密
    public static String decrypt(String encryString, String method, String key) {
        //将hexstring转为string
        String data = null;
        if (!TextUtils.isEmpty(encryString)) {
//            byte[] enBYtes = StringUtils.hexStringToBytes(encryString);
            byte[] enBYtes = Base64.decode(encryString, Base64.DEFAULT);
            byte[] deData = decrypt(enBYtes, method, key, getIV(""));
            if (deData != null) {
                data = new String(deData);
            }
        }
        return data;
    }

    public static byte[] decrypt(String encryString, String method, String key, String ivParameter) {
        //将hexstring转为string
        byte[] enBYtes = null;
        if (!TextUtils.isEmpty(encryString)) {
//            enBYtes = StringUtils.hexStringToBytes(encryString);
            enBYtes= Base64.decode(encryString, Base64.DEFAULT);
        }
        return decrypt(enBYtes, method, key, ivParameter);
    }

    public static byte[] decrypt(byte[] encryBytes, String method, String key) {
        return decrypt(encryBytes, method, key, getIV(""));
    }

    /**
     * 解密方法
     *
     * @param encryBytes  待解密数据
     * @param method      数据加密的方式
     * @param key         密钥
     * @param ivParameter 向量  向量的长度必须是16位
     * @return
     */
    public static byte[] decrypt(byte[] encryBytes, String method, String key, String ivParameter) {
        byte[] normalBytes = null;
        try {
            if (TextUtils.isEmpty(key)) {
                throw new RuntimeException("the key is empty!!");
            }

            if (TextUtils.isEmpty(ivParameter) || ivParameter.length() != 16) {
                throw new RuntimeException("the iv is empty or iv length is not 16");
            }
            //获取密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            //初始化加密类型
            Cipher localCipher = Cipher.getInstance(method);
            //初始化数据处理方式
            if (method.contains("ECB")) {
                localCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            } else {
                //获取向量
                IvParameterSpec paramSpec = new IvParameterSpec(ivParameter.getBytes());
                localCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);
            }
            //执行解密
            normalBytes = localCipher.doFinal(encryBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalBytes;
    }

    /**
     * 向量一般是16位
     * @param ivParam
     * @return
     */
    private static String getIV(String ivParam) {
        if (TextUtils.isEmpty(ivParam)) {
            ivParam = "12345678abcdefgh";
        }
        StringBuffer localStringBuffer = new StringBuffer(16);
        localStringBuffer.append(ivParam);
        while (localStringBuffer.length() < 16) {
            localStringBuffer.append("0");
        }
        if (localStringBuffer.length() > 16) {
            localStringBuffer.setLength(16);
        }
        return localStringBuffer.toString();
    }

}
