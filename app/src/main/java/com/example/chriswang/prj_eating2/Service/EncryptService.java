package com.example.chriswang.prj_eating2.Service;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ChrisWang on 2017/11/3.
 */

public class EncryptService {

    private static byte[] seedValue = {
            0x2d, 0x2a, 0x2d, 0x42, 0x55, 0x49, 0x4c, 0x44, 0x41, 0x43, 0x4f, 0x44, 0x45, 0x2d, 0x2a, 0x2d
    };
    private static String ALGORITHM = "AES";
    private static SecretKeySpec secretKey = new SecretKeySpec(seedValue, "AES");


    public String encrypt( String data ) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(data.getBytes("UTF8"));
            String encryptedString = new String(Base64.encode(cipherText ,Base64.DEFAULT ) );
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.decode(data.getBytes("UTF8"), Base64.DEFAULT);
            String decryptedString = new String(cipher.doFinal(cipherText),"UTF-8");
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
