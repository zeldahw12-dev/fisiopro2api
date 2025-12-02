package com.fisio.fisio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "nickname", length = 80, unique = true, nullable = false)
    private String nickname;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "contra", length = 200, nullable = false)
    private String contra;

    @Column(name = "foto", length = 500)
    private String foto;

    @Column(name = "profesion", length = 120)
    private String profesion;

    // 👉 NUEVO: plan del usuario (FREE / PREMIUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 20)
    private PlanTipo plan = PlanTipo.FREE;

    // 👉 (ya tenías esto de sesiones, lo dejo tal cual si lo agregaste)
    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    // getters/setters explícitos si quieres (o confía en Lombok)
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getContra() { return contra; }
    public void setContra(String contra) { this.contra = contra; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public PlanTipo getPlan() { return plan; }
    public void setPlan(PlanTipo plan) { this.plan = plan; }

    public Integer getTokenVersion() { return tokenVersion; }
    public void setTokenVersion(Integer tokenVersion) { this.tokenVersion = tokenVersion; }
}
