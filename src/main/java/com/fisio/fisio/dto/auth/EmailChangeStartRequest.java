package com.fisio.fisio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmailChangeStartRequest {

    @NotNull(message = "idUsuario es obligatorio")
    private Integer idUsuario;

    @Email(message = "Email inválido")
    @Size(max = 120, message = "Email muy largo")
    @NotNull(message = "newEmail es obligatorio")
    private String newEmail;

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
}
