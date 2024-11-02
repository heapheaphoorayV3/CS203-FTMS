package cs203.ftms.user.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String gmailEmail;

    @Value("${spring.mail.password}")
    private String gmailPassword;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String mailAddress, String title, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(gmailEmail);
        msg.setTo(mailAddress);
        msg.setSubject(title);
        msg.setText(content);
        javaMailSender.send(msg);
    }
}
