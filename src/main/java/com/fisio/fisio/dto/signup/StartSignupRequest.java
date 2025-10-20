package com.fisio.fisio.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StartSignupRequest {

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 120, message = "Email muy largo")
    private String email;

    public StartSignupRequest() {}
    public StartSignupRequest(String email) { this.email = email; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
