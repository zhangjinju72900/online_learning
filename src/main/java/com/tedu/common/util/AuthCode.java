package com.tedu.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.sun.crypto.provider.SunJCE;

@SuppressWarnings("restriction")
public class AuthCode {

	
	public static final String strKey = "20181030";
	
	/**
	 * 加密字符串encode
	 */
	public static String encodeEncrypt(String strIn) {
		try {
			return URLEncoder.encode(encrypt(strIn), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
    /**
     * 加密字符串
     *
     * @param strIn  需加密的字符串
     * @param strKey 密钥
     * @return 加密后的字符串
     */
	public static String encrypt(String strIn) {
        try {
            Security.addProvider(new SunJCE());
            Key key = getKey(strKey.getBytes());
            Cipher encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.encodeBase64(encryptCipher.doFinal(strIn.getBytes())));
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密字符串
     *
     * @param strIn  需解密的字符串
     * @param strKey 密钥
     * @return 解密后的字符串
     */
	public static String decrypt(String strIn) {
        try {
            Security.addProvider(new SunJCE());
            Key key = getKey(strKey.getBytes());
            Cipher decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            return new String(decryptCipher.doFinal(Base64.decodeBase64(strIn.getBytes())));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception 异常
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++)
            arrB[i] = arrBTmp[i];
        // 生成密钥
        return new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    }


    public static void main(String[] args) {
        try {
            /*String s1 = "D:\\apache-tomcat-8.5.29\\中文的\\路径\\view\\common";
            System.out.println("加密前的字符：" + s1);
            String s2 = AuthCode.encrypt(s1);
            System.out.println(s2);
            System.out.println("加密后的字符：" + s2);
            String s3 = AuthCode.decrypt(s2);
            System.out.println("解密后的字符：" + s3);
            System.out.println(s1.equals(s3));*/
        	String key = "000000000000,500010100001,300010100001,600010100001,800010100001,200010100001,110010100001,320010100001,340010100001,150010100001,"
        			+ "160010100001,520010100001,430010100001,170010100001,A00010100001,C10010100001";
        	Arrays.asList(key.split(",")).stream().map(m ->{
        		return encrypt(m);
        	}).forEach(l ->{
        		System.out.println(l);
        	});
        	System.out.println(encrypt("000000000000"));
        	System.out.println(encrypt("C10010100001"));
        	System.out.println(URLEncoder.encode("http://know-how001.oss-cn-beijing.aliyuncs.com/dad8760cd5ae4ae887eb96fa1ff8cb8820190221090605.jpg?x-oss-process=image/resize,w_1000,h_1000/quality,Q_100", "UTF-8"));
        	System.out.println(URLEncoder.encode("http://know-how001.oss-cn-beijing.aliyuncs.com/5551f666ca0c474bb3c017e1a49266a920190221090557.jpg?x-oss-process=image/resize,w_1000,h_1000/quality,Q_100", "UTF-8"));
        	System.out.println(URLEncoder.encode("http://know-how001.oss-cn-beijing.aliyuncs.com/ebab639a91dd4a89a4580b507dcd0b3f20190221090552.jpg?x-oss-process=image/resize,w_1000,h_1000/quality,Q_100", "UTF-8"));
        	
        	System.out.println(URLDecoder.decode("http%3A%2F%2Fknow-how001-cdn.knowhowedu.cn%2Febab639a91dd4a89a4580b507dcd0b3f20190221090552.jpg%3Fx-oss-process%3Dimage%2Fresize%2Cw_1000%2Ch_1000%2Fquality%2CQ_100"));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

