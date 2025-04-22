package com.glim.verification.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SmsGeneratorUtil {
    public String createCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}