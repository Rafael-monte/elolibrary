package com.example.elolibrary.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiceUtils {

    public static String createExceptionMessage(final MessageTemplate template, String... params) {

        return String.format(template.getValue(), params);
    }

    public static Boolean isFalsyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    /*REFERENCIA https://www.javatpoint.com/how-to-encrypt-password-in-java*/
    public static String encryptPassword(String pass) throws NoSuchAlgorithmException {
        byte[] hash = getSHA(pass);
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }


    private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }




}
