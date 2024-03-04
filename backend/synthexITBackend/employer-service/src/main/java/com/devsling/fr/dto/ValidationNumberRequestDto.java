package com.devsling.fr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ValidationNumberRequestDto {
    private String phoneNumber;//destination
    private String userName;
    private String oneTimePassword;
}
