
package hu.kaoszkviz.server.api.service;

import hu.kaoszkviz.server.api.model.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendResetPasswordToken(PasswordResetToken passwordResetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@kaoszkviz.hu");
        message.setTo(passwordResetToken.getUser().getEmail());
        message.setSubject("Jelszó visszaállítás");
        
        String text = "http://kaoszkviz.hu/resetpassword/%s".formatted(passwordResetToken.getKey().toString());
        message.setText(text);
        
        mailSender.send(message);
    }
}
