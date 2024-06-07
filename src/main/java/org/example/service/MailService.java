package org.example.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(MailService.class);;
    public boolean sendMessage(Long userId, String senderMail, String message){
        var sender = userService.findByEmail(senderMail);
        try {
            //создаем экземпляр класса
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userService.findById(userId).getEmail());
            mailMessage.setSubject("Сообщение от пользователя " + sender.getName());
            mailMessage.setText(message);
            mailMessage.setFrom("ykservice");
            mailSender.send(mailMessage);
            logger.info("Успешно отправлено сообщение");
        } catch (Exception e) {
            logger.error("Error while sending email: ", e);
        }
     return true;
    }
}
