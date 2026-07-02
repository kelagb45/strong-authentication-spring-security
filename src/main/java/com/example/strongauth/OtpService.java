package com.example.strongauth;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String username) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStorage.put(username, otp);

        System.out.println("OTP for " + username + " is: " + otp);

        return otp;
    }

    public boolean verifyOtp(String username, String otp) {
        String savedOtp = otpStorage.get(username);

        if (savedOtp != null && savedOtp.equals(otp)) {
            otpStorage.remove(username);
            return true;
        }

        return false;
    }
}