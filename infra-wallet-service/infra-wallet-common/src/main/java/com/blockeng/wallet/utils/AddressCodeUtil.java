package com.blockeng.wallet.utils;

public class AddressCodeUtil {


    /**
     * 自定义进制()
     */
    private static final char[] r = new char[]{'5', '3', '9', '6', '7', '2', '8', '4', '0', '1'};

    /**
     * 进制长度
     */
    private static final int binLen = r.length;

    //    private static final long startNumber = 1048576L;
    private static final long startNumber = 1224343L;

    /**
     * @param id ID
     * @return 随机码
     */
    public static String idToCode(long id, long costomStartNumber) {
        if (costomStartNumber < 0) {
            costomStartNumber = startNumber;
        }
        id += costomStartNumber;
        char[] buf = new char[32];
        int charPos = 32;

        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos] = r[ind];
            id /= binLen;
        }
        buf[--charPos] = r[(int) (id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str = new String(buf, charPos, (32 - charPos));
        return str.toUpperCase();
    }

    public static String idToCode(long idL) {
        return idToCode(idL, -1L);
    }

    public static String idToCode(String id) {
        long idL = Long.parseLong(id);
        return idToCode(idL, -1L);
    }

    public static String idToCode(String id, long costomStartNumber) {
        long idL = Long.parseLong(id);
        return idToCode(idL, costomStartNumber);
    }


    public static long codeToId(String code) {
        code = code.toUpperCase();
        char chs[] = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chs.length; i++) {
            int ind = 0;
            for (int j = 0; j < binLen; j++) {
                if (chs[i] == r[j]) {
                    ind = j;
                    break;
                }
            }
            if (i > 0) {
                res = res * binLen + ind;
            } else {
                res = ind;
            }
        }
        res -= startNumber;
        return res;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        Long id = 1004280239540260865L;
        String s = idToCode(id);
        System.out.println(s);
        long l = codeToId(s);
        System.out.println(l);
    }


}
