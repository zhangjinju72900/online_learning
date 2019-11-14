package com.tedu.base.util;

import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PinYinUtil;
import com.tedu.base.engine.util.FormLogger;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2019-06-11
 */

@Component
public class AESUtil {

	 private static String ENCRYPT_KEY = "";
	    private static String INIT_VECTOR = "";
	    public String encrypt(String key, String str) {
	        String k = MD5Util.MD5Encode(key).substring(0, 16);
	        Key secretKey = new SecretKeySpec(k.getBytes(), "AES");
	        String result = "";
	        try {
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            byte[] byteContent = str.getBytes("utf-8");
	            result = parseByte2HexStr(cipher.doFinal(byteContent));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return result;

	    }

	    public String decrypt(String key, String encryptStr) {

	        String k = MD5Util.MD5Encode(key).substring(0, 16);
	        Key secretKey = new SecretKeySpec(k.getBytes(), "AES");
	        String result = "";
	        try {
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            byte[] bytes = parseHexStr2Byte(encryptStr);
	            result = new String(cipher.doFinal(bytes));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return result;
	    }
    public static void main(String[] args) {

        AESUtil aesUtil = new AESUtil();

        try {
			//System.out.println(aesUtil.encrypt("aliToken", "19e242b9b3a7438cb33aa81c23bbbd9e"));
			//System.out.println(aesUtil.decrypt("aliToken", "2DDA437980A2E64503BEB5BAD98A4E25F36CB93CC9F61AA916634A1BE5FA3190BEC868FEADA8FD08A1A350E47FF35A03"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public static void checkEmail(String pinyin, Map map, int seq) {
        String filpath = "/opt/file/contract//goldShock/20190705/劳动合同201907051329422942.pdf";
        System.out.println(filpath.substring(0, filpath.lastIndexOf(File.separator)));
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public  String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
