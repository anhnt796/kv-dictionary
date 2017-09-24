package com.anhnt.kovidict.myapplication.util;

public class StringMatcher {
    private static final char[] KOREAN_INITIAL;
    private static final char KOREAN_UNICODE_END = '\ud7a3';
    private static final char KOREAN_UNICODE_START = '\uac00';
    private static final char KOREAN_UNIT = '\u024c';

    static {
        KOREAN_INITIAL = new char[]{'\u3131', '\u3132', '\u3134', '\u3137', '\u3138', '\u3139', '?', '\u3142', '\u3143', '\u3145', '\u3146', '\u3147', '\u3148', '\u3149', '\u314a', '\u314b', '\u314c', '?', '\u314e'};
    }

    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null || keyword.length() > value.length()) {
            return false;
        }
        int i = 0;
        int j = 0;
        do {
            if (!isKorean(value.charAt(i)) || !isInitialSound(keyword.charAt(j))) {
                if (keyword.charAt(j) != value.charAt(i)) {
                    if (j > 0) {
                        break;
                    }
                    i++;
                } else {
                    i++;
                    j++;
                }
            } else if (keyword.charAt(j) != getInitialSound(value.charAt(i))) {
                if (j > 0) {
                    break;
                }
                i++;
            } else {
                i++;
                j++;
            }
            if (i >= value.length()) {
                break;
            }
        } while (j < keyword.length());
        if (j == keyword.length()) {
            return true;
        }
        return false;
    }

    private static boolean isKorean(char c) {
        if (c < KOREAN_UNICODE_START || c > KOREAN_UNICODE_END) {
            return false;
        }
        return true;
    }

    private static boolean isInitialSound(char c) {
        for (char i : KOREAN_INITIAL) {
            if (c == i) {
                return true;
            }
        }
        return false;
    }

    private static char getInitialSound(char c) {
        return KOREAN_INITIAL[(c - 44032) / 588];
    }
}