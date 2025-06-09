package com.solidarix.backend.service;

public class PostalCodeUtils {

    public static int proximityScore(String code1, String code2) {
        if (code1 == null || code2 == null) return 0;

        int max = Math.min(code1.length(), code2.length());
        for (int i = 0; i < max; i++) {
            if (code1.charAt(i) != code2.charAt(i)) {
                return i;
            }
        }
        return max;
    }
}
