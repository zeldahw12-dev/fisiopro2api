package com.fisio.fisio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailChangeVerifyRequest {

    @NotBlank(message = "newEmail es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 120, message = "Email muy largo")
    private String newEmail;

    @NotBlank(message = "code es obligatorio")
    @Size(min = 4, max = 10, message = "Código inválido")
    private String code;

    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
