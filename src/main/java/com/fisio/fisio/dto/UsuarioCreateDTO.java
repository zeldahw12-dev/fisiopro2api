package com.fisio.fisio.dto;

import jakarta.validation.constraints.*;

public class UsuarioCreateDTO {

    @NotBlank(message = "El nickname es obligatorio")
    @Size(max = 80, message = "El nickname no puede exceder 80 caracteres")
    private String nickname;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    private Integer edad;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contra;

    @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres")
    private String foto;

    @Size(max = 120, message = "La profesión no puede exceder 120 caracteres")
    private String profesion;

    // ===== Getters y Setters =====
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContra() { return contra; }
    public void setContra(String contra) { this.contra = contra; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }
}
