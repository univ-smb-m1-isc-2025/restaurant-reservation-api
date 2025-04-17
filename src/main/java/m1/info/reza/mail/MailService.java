package m1.info.reza.mail;

import m1.info.reza.reservation.Reservation;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String toEmail, String subject, String htmlContent) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(toEmail);
            helper.setSubject("👨‍🍳 REZA - " + subject);
            helper.setText(htmlContent, true);
        };
        mailSender.send(messagePreparator);
    }

    public void sendReservationConfirmation(Reservation reservation) {
        String emailBody = MailTemplate.buildReservationConfirmationEmail(reservation);
        sendEmail(reservation.getCustomer().getEmail(), "Confirmation de votre réservation", emailBody);
    }

    public void sendReservationReminder(Reservation reservation) {
        String emailBody = MailTemplate.buildReservationReminderEmail(reservation);
        sendEmail(reservation.getCustomer().getEmail(), "Rappel de votre réservation", emailBody);
    }

    public void sendFeedbackReservation(Reservation reservation) {
        String emailBody = MailTemplate.buildFeedbackRequestEmail(reservation);
        sendEmail(reservation.getCustomer().getEmail(), "Comment s'est passé votre réservation ?", emailBody);
    }
}
