package com.ba.demo.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwsService {

  @NonNull private final JavaMailSender mailSender;

  public void sendEmail(
      final String from, final String to, final String subject, final String content)
      throws MessagingException {
    final MimeMessage mimeMessage = mailSender.createMimeMessage();

    final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(content, true);
    mailSender.send(mimeMessage);
  }
}
