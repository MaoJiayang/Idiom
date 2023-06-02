package com.nucleon.util;

public class PinyinHelper {
    public static String getPinyinWithoutTone(String tonedPinyin) {
        if (tonedPinyin == null || tonedPinyin.length() < 1) {
            throw new RuntimeException("invalid pinyin");
        }

        StringBuilder sb = new StringBuilder();
        for (char c : tonedPinyin.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ') {
                sb.append(c);
            } else {
                Character ch = VowelHelper.getVowelWithoutTone(c);
                if (ch == null) {
                    throw new RuntimeException("invalid pinyin:" + tonedPinyin);
                }
                sb.append(ch);
            }
        }

        return sb.toString();
    }

}