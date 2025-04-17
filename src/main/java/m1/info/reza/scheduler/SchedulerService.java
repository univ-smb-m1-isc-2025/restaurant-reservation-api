package m1.info.reza.scheduler;

import m1.info.reza.mail.MailService;
import m1.info.reza.reservation.Reservation;
import m1.info.reza.reservation.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerService {

    private final ReservationService reservationService;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public SchedulerService(ReservationService reservationService, MailService mailService) {
        this.reservationService = reservationService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 17 * * *")
    public void sendReminderToTomorrowReservations() {
        List<Reservation> reservations = reservationService.findPendingReservationsForTomorrow();

        for(Reservation reservation : reservations) {
            mailService.sendReservationReminder(reservation);
        }

        logger.info("🕰️ Reminder emails have been sent for tomorrow's reservations.");
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void sendFeedbackRequestToYesterdayReservations() {
        List<Reservation> reservations = reservationService.findCompletedReservationsFromYesterday();

        for(Reservation reservation : reservations) {
            mailService.sendFeedbackReservation(reservation);
        }

        logger.info("⭐️ Feedback request emails have been sent for yesterday's reservations.");
    }
}
