package com.ba.demo.service;

import com.ba.demo.configuration.EmailConfig;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  private final TemplateService templateService;
  private final AwsService awsService;
  private final EmailConfig emailConfig;

  public void sendNotification(
      String to, String subject, String template, Map<String, Object> immutableMap) {
    LOGGER.info(
        "Sending notification by email to : {} , subject: {}, template: {}, params : {}",
        to,
        subject,
        template,
        immutableMap);
    try {
      Map<String, Object> params = new HashMap<>(immutableMap);
      params.put("year", LocalDate.now().getYear());
      String emailBody = this.templateService.template(template, params);
      LOGGER.trace("Sending notification body : {}", emailBody);
      awsService.sendEmail(this.emailConfig.getFrom(), to, subject, emailBody);
    } catch (IOException | MessagingException e) {
      LOGGER.error("Error trying to send notification.", e);
    }
  }
}
