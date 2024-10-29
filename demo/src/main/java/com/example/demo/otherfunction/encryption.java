package com.example.demo.otherfunction;

import java.security.*;

public class encryption {

    private static final String PREFIX = "12345";
    private static final String SUFFIX = "09876";

    public static String encrypt(String password) {
        // Chuẩn bị chuỗi đã thêm tiền tố và hậu tố
        String combinedPassword = PREFIX + password + SUFFIX;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] byteData = md.digest(combinedPassword.getBytes());

            // Chuyển đổi mảng byte thành chuỗi hex
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            // Trả về chuỗi mã hóa cuối cùng
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found!", e);
        }
    }

    // So sánh mật khẩu gốc với mật khẩu mã hóa từ cơ sở dữ liệu
    public static boolean matches(String rawPassword, String encryptedPassword) {
        String hashedPassword = encrypt(rawPassword);
        return hashedPassword.equals(encryptedPassword);
    }

}
