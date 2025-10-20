package com.fisio.fisio.dto.signup;

import jakarta.validation.constraints.*;

public class CompleteSignupRequest {

    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Size(max = 80)
    private String nickname;

    @NotBlank @Size(max = 100)
    private String nombre;

    @NotNull @Min(1) @Max(120)
    private Integer edad;

    @NotBlank @Size(min = 6, max = 100)
    private String contra;

    @NotBlank @Size(min = 6, max = 100)
    private String confirmarContra;

    @Size(max = 500)
    private String foto;

    @Size(max = 120)
    private String profesion;

    public CompleteSignupRequest() {}

    // Getters/Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getContra() { return contra; }
    public void setContra(String contra) { this.contra = contra; }

    public String getConfirmarContra() { return confirmarContra; }
    public void setConfirmarContra(String confirmarContra) { this.confirmarContra = confirmarContra; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }
}
