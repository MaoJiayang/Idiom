package com.nucleon.util;

import java.util.HashMap;
import java.util.Map;

public class VowelHelper {

    /** 获取去除音调的元音字符 **/
    public static Character getVowelWithoutTone(char tonedVowel) {
        return vowelMap.get(tonedVowel);
    }

    private static final char a = 'a';
    private static final char e = 'e';
    private static final char i = 'i';
    private static final char o = 'o';
    private static final char u = 'u';
    private static final char v = 'v';

    private static Map<Character, Character> vowelMap;
    static {
        vowelMap = new HashMap<>();

        vowelMap.put('ā', a);
        vowelMap.put('á', a);
        vowelMap.put('ǎ', a);
        vowelMap.put('à', a);

        vowelMap.put('ē', e);
        vowelMap.put('é', e);
        vowelMap.put('ě', e);
        vowelMap.put('è', e);

        vowelMap.put('ī', i);
        vowelMap.put('í', i);
        vowelMap.put('ǐ', i);
        vowelMap.put('ì', i);

        vowelMap.put('ō', o);
        vowelMap.put('ó', o);
        vowelMap.put('ǒ', o);
        vowelMap.put('ò', o);

        vowelMap.put('ū', u);
        vowelMap.put('ú', u);
        vowelMap.put('ǔ', u);
        vowelMap.put('ù', u);

        vowelMap.put('ü', v);
        vowelMap.put('ǖ', v);
        vowelMap.put('ǘ', v);
        vowelMap.put('ǚ', v);
        vowelMap.put('ǜ', v);
    }
}
