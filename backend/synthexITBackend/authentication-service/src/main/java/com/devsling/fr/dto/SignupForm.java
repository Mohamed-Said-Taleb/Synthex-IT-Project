package com.devsling.fr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {

    private String username;
    private String password;
    private Boolean enabled;
    private String email;
    private String gender;
    private String role_Name;
}
