package cs203.ftms.overall.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for handling email-related operations.
 * Uses Spring's JavaMailSender to send emails asynchronously.
 */
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

    /**
     * Sends an email asynchronously using the configured mail executor.
     *
     * @param mailAddress the recipient's email address
     * @param title the subject of the email
     * @param content the body content of the email
     * @return a CompletableFuture indicating the asynchronous execution of the email sending task
     * @throws RuntimeException if sending the email fails
     */
    @Async("mailExecutor")
    public CompletableFuture<Void> sendMail(String mailAddress, String title, String content) {
        return CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(gmailEmail);
                msg.setTo(mailAddress);
                msg.setSubject(title);
                msg.setText(content);
                javaMailSender.send(msg);
            } catch (Exception e) {
                throw new RuntimeException("Failed to send email", e);
            }
        });
    }
}
