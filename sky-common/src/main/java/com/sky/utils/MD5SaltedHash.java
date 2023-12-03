package com.sky.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MD5SaltedHash {

    // 生成随机的盐
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    // 对密码进行加盐MD5哈希
    public static String hashPassword(String password, String salt) {
        String saltedPassword = salt + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 生成包含密码和盐的加密字符串
    public static String hashAndSaltPassword(String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return salt + "$" + hashedPassword;
    }

    // 验证密码是否匹配
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        String[] parts = storedPassword.split("\\$");
        if (parts.length != 2) {
            return false; // 存储的密码格式不正确
        }

        String salt = parts[0];
        String hashedInputPassword = hashPassword(inputPassword, salt);

        return storedPassword.equals(salt + "$" + hashedInputPassword);
    }

  /*  public static void main(String[] args) {
        // 示例用法
        String originalPassword = "mySecurePassword";
        String encryptedPassword = hashAndSaltPassword(originalPassword);

        System.out.println("Original Password: " + originalPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);

        // 验证密码
        String userInputPassword = "mySecurePassword";
        if (verifyPassword(userInputPassword, encryptedPassword)) {
            System.out.println("Password is correct!");
        } else {
            System.out.println("Password is incorrect!");
        }
    }*/
}
