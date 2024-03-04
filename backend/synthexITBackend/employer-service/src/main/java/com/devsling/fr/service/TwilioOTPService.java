package com.devsling.fr.service;

import com.devsling.fr.config.twilio.TwilioConfig;
import com.devsling.fr.dto.OtpStatus;
import com.devsling.fr.dto.ValidationNumberRequestDto;
import com.devsling.fr.dto.ValidationNumberResponseDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class TwilioOTPService {
    private final TwilioConfig twilioConfig;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<ValidationNumberResponseDto> sendOTPForValidationNumber(ValidationNumberRequestDto passwordResetRequestDto) {

        ValidationNumberResponseDto passwordResetResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMessage = "Dear Customer , Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank You.";
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
            otpMap.put(passwordResetRequestDto.getUserName(), otp);
            passwordResetResponseDto = new ValidationNumberResponseDto(OtpStatus.DELIVERED, otpMessage);
        } catch (Exception ex) {
            passwordResetResponseDto = new ValidationNumberResponseDto(OtpStatus.FAILED, ex.getMessage());
        }
        return Mono.just(passwordResetResponseDto);
    }

    public Mono<String> validateOTP(ValidationNumberRequestDto validationNumberRequestDto) {
        if (validationNumberRequestDto.equals(otpMap.get(validationNumberRequestDto.getUserName()))) {
            otpMap.remove(validationNumberRequestDto.getPhoneNumber(),validationNumberRequestDto);
            return Mono.just("Valid OTP please proceed with your transaction !");
        } else {
            return Mono.error(new IllegalArgumentException("Invalid otp please retry !"));
        }
    }

    //6 digit otp
    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

}
