package org.tinygroup.flowbasiccomponent.util;

/**
 * 十六进制和ASCII互转
 *
 * @author qiucn
 */
public class Hex2AscUtil {

    /**
     * ASCII转十六进制
     *
     * @param asc
     * @return
     */
    public static int ascToHex(int asc) {
        if ((asc >= 0) && (asc <= 9))
            asc += 0x30;
        else if ((asc >= 10) && (asc <= 15))
            asc += 0x37;
        else
            asc = 0xff;
        return asc;
    }

    /**
     * 十六进制转ASCII
     *
     * @param asc
     * @return
     */
    public static int hexToAsc(int hex) {
        if ((hex >= 0x30) && (hex <= 0x39))
            hex -= 0x30;
        else if ((hex >= 0x41) && (hex <= 0x46))
            hex -= 0x37;
        else if ((hex >= 0x61) && (hex <= 0x66))
            hex -= 0x57;
        else
            hex = 0xff;
        return hex;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param String str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}
