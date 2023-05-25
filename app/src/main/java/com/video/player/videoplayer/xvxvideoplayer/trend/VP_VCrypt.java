package com.video.player.videoplayer.xvxvideoplayer.trend;

import android.os.Build;
import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class VP_VCrypt {
    public static String pointer1 = "Watch Naughty Videos!";
    public static String pointer2 = "Discover The Best Collection of Naughty Videos.";

    public static String encrypt(String text) throws Exception {
        String encStr = "";
        if (text == null || text.length() == 0)
            return "";

        if (text != null)
            return vp_doSwitch(text, "dK1Z5QHFb}c615#d",
                    "dK1Z5QHFb}c615#d");

        byte[] encrypted = null;
        return encStr;
    }

    public static String vp_doSwitch(String input, String key, String specStr) {
        byte[] vp_data = null;
        try {
            IvParameterSpec vp_iv = new IvParameterSpec(specStr.getBytes());
            SecretKeySpec vp_skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher vp_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            vp_cipher.init(Cipher.ENCRYPT_MODE, vp_skey, vp_iv);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                vp_data = vp_cipher.doFinal(input.trim().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return Base64.encodeToString(vp_data, Base64.DEFAULT);
    }


    public static String vp_decrypt(String vp_code, String vp_key, String vp_specStr) throws Exception {
        if (vp_code == null || vp_code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;

        try {
            byte[] vp_data = Base64.decode(vp_code, Base64.DEFAULT);
            IvParameterSpec vp_iv = new IvParameterSpec(vp_specStr.getBytes());
            SecretKeySpec vp_skey = new SecretKeySpec(vp_key.getBytes(), "AES");
            Cipher vp_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            vp_cipher.init(Cipher.DECRYPT_MODE, vp_skey, vp_iv);
            decrypted = vp_cipher.doFinal(vp_data);
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return new String(decrypted);
    }
}

