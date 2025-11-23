package com.fisio.fisio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Si no tienes SMTP configurado, pon LOG_ONLY=true para imprimir en consola.
 * NOTA: Enviar contraseñas por correo en texto plano NO es seguro en producción.
 */
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.from:FisioApp <no-reply@fisioapp.local>}")
    private String defaultFrom;

    @Value("${app.mail.log-only:false}")
    private boolean logOnly;

    /** Código de verificación (registro / signup) */
    public void sendVerificationCode(String to, String code) {
        String subject = "Tu código de verificación - FisioPro";
        String text = "Hola,\n\n" +
                "Tu código de verificación para FisioPro es:\n\n" +
                code + "\n\n" +
                "Si no solicitaste este código, ignora este correo.";
        sendPlainEmail(to, subject, text);
    }

    /** Cambio de email: código al nuevo correo */
    public void sendEmailChangeCode(String to, String code) {
        String subject = "Confirma tu nuevo correo en FisioPro";
        String text = "Estás intentando cambiar tu correo en FisioPro.\n\n" +
                "Código de confirmación: " + code + "\n\n" +
                "Si no solicitaste este cambio, ignora este correo.";
        sendPlainEmail(to, subject, text);
    }

    /** Cambio de email: confirmación de éxito (opcional) */
    public void sendEmailChangeSuccess(String to) {
        String subject = "Correo actualizado en FisioPro";
        String text = "Listo. Tu correo fue actualizado correctamente a: " + to;
        sendPlainEmail(to, subject, text);
    }

    /** Recuperar contraseña (OJO: inseguro en producción) */
    public void sendPasswordReminder(String to, String password) {
        String subject = "Recuperación de contraseña - FisioPro";
        String text = "Hola,\n\n" +
                "Tu contraseña registrada en FisioPro es:\n\n" +
                password + "\n\n" +
                "Por seguridad, te recomendamos cambiarla al iniciar sesión.\n\n" +
                "— FisioPro";
        sendPlainEmail(to, subject, text);
    }

    private void sendPlainEmail(String to, String subject, String text) {
        if (logOnly || mailSender == null) {
            System.out.println("[EmailService] (LOG_ONLY=" + logOnly + ") De: " + defaultFrom + " | Para: " + to);
            System.out.println("[EmailService] Asunto: " + subject);
            System.out.println("[EmailService] Contenido:\n" + text);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(defaultFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
