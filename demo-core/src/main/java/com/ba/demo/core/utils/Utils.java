package com.ba.demo.core.utils;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String EMPTYUUID = "00000000-0000-0000-0000-000000000000";


    public static final String generateRandomText(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static final String generateRandomNumber(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return new String(returnValue);
    }

    public static final Double roundToTwoDecimalPlaces(Double value){
        return Math.round(value * 100.0) / 100.0;

    }
}
