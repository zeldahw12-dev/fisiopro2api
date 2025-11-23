package com.fisio.fisio.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyCodeRequest {

    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Size(min = 4, max = 10)
    private String code;

    public VerifyCodeRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
