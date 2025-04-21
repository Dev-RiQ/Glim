package com.glim.verification.util;

import java.util.Random;

public class SmsGeneratorUtil { // 인증번호 생성 유틸

    public static String generateAuthCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
