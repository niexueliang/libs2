package com.nil.code.libs.util;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 3/14 0014.
 */
public class CheckNumber {

    private static int ID_LENGTH = 17;

    /**
     * 正则式校验
     * @param idNum
     * @return
     */
    private static boolean vIDNumByRegex(String idNum) {
        String curYear = "" + Calendar.getInstance().get(Calendar.YEAR);
        int y3 = Integer.valueOf(curYear.substring(2, 3));
        int y4 = Integer.valueOf(curYear.substring(3, 4));
        return idNum.matches("^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19\\d{2}|20([0-" + (y3 - 1) + "][0-9]|" + y3 + "[0-" + y4
                + "]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])))\\d{3}([0-9]|x|X)$");
    }

    /**
     * 验证码校验
     * @param idNum
     * @return
     */
    private static boolean vIDNumByCode(String idNum) {
        // 系数列表
        int[] ratioArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码列表
        char[] checkCodeList = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        // 获取身份证号字符数组
        char[] cIds = idNum.toCharArray();
        // 获取最后一位（身份证校验码）
        char oCode = cIds[ID_LENGTH];
        int[] iIds = new int[ID_LENGTH];
        int idSum = 0;// 身份证号第1-17位与系数之积的和
        int residue = 0;// 余数(用加出来和除以11，看余数是多少？)
        for (int i = 0; i < ID_LENGTH; i++) {
            iIds[i] = cIds[i] - '0';
            idSum += iIds[i] * ratioArr[i];
        }
        residue = idSum % 11;// 取得余数
        return Character.toUpperCase(oCode) == checkCodeList[residue];
    }

    public static boolean isIdCard(String idNum) {
        if(idNum!=null&&idNum.length() == 18)
            return vIDNumByRegex(idNum) && vIDNumByCode(idNum);
        else
            return false;
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean mobilePattern(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";

        return mobile.matches(regex);
    }
}
