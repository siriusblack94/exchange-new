package com.blockeng.extend.util;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


public class DecryptCoder {


    private final static String DES = "DES";

    /**
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    public final static String decrypt(String data, String key) {
        try {
            //这里就没走
            return new String(decrypt(String2byte(data.getBytes()), key.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] String2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String DataDecrypt(String str, byte[] key) {
        String decrypt = null;
        try {
            byte[] ret = decrypt(Base64.decode(str), key);
            decrypt = new String(ret, "UTF-8");
        } catch (Exception e) {
            System.out.print(e);
            decrypt = str;
        }
        return decrypt;


    }

   public static void main(String[] args) {
        String desencryptString = decrypt("AE3B474D414324A510BC62982BE966403E70289E8EF1C334F9D06C4E75E3AC26", "!q@i#q$i%l^i&u*()-");
        System.out.println(desencryptString);
    }
//    输出：is张三丰
}
