package com.noureddine.WriteFlow.Utils;

import android.util.Base64;
import android.util.Log;

import com.noureddine.WriteFlow.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionManager {
    private static final String TAG = "EncryptionManager";
    // مفتاح التشفير (يجب استخدام مفتاح معقد وآمن)
    private static final String SECRET_KEY = "Oqv!tzt.C%]wnQh}04-9]DbI'&Zp@TJ^";

    // طريقة لتوليد مفتاح تشفير أكثر أمانًا باستخدام SHA-256
    private static SecretKeySpec generateSecretKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }

    // تشفير مفتاح API
    public static String encryptText(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = generateSecretKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String encryptUser(User user) {

        if (user == null) {
            Log.e(TAG, "Cannot save null user");
            return null;
        }
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("name", user.getName());
            userJson.put("email", user.getEmail());
            userJson.put("uid", user.getUid());
            userJson.put("membership", user.getMembership());
            userJson.put("endSubscription", user.getEndSubscription());
            userJson.put("wordPremium", user.getWordPremium());
            userJson.put("wordProcessing", user.getWordProcessing());

            try {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                SecretKeySpec secretKey = generateSecretKey();
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);

                byte[] encryptedBytes = cipher.doFinal(userJson.toString().getBytes(StandardCharsets.UTF_8));
                return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error serializing user data", e);
            return null;
        }

    }




    // فك تشفير مفتاح API
    public static String decryptText(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = generateSecretKey();
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User decryptUser(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = generateSecretKey();
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
            //return new String(decryptedBytes, StandardCharsets.UTF_8);

            String userString = new String(decryptedBytes, StandardCharsets.UTF_8);
            try {
                JSONObject userJson = new JSONObject(userString);
                return new User(
                        userJson.getString("name"),
                        userJson.getString("email"),
                        userJson.getString("uid"),
                        userJson.getString("membership"),
                        userJson.getLong("endSubscription"),
                        userJson.getLong("wordPremium"),
                        userJson.getLong("wordProcessing")
                );
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse user data", e);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





}