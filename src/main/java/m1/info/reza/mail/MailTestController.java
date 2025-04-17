package m1.info.reza.mail;

import m1.info.reza.reservation.Reservation;
import m1.info.reza.reservation.ReservationRepository;
import m1.info.reza.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailTestController {

    private final MailService mailService;
    private final ReservationService reservationService;

    public MailTestController(MailService mailService, ReservationService reservationService) {
        this.mailService = mailService;
        this.reservationService = reservationService;
    }

    @GetMapping("/confirmation/{reservationId}")
    public ResponseEntity<String> sendReservationConfirmation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservation(reservationId);
        mailService.sendReservationConfirmation(reservation);

        return ResponseEntity.ok("E-mail de confirmation envoyé pour la réservation ID: " + reservationId);
    }

    @GetMapping("/reminder/{reservationId}")
    public ResponseEntity<String> sendReservationReminder(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservation(reservationId);
        mailService.sendReservationReminder(reservation);

        return ResponseEntity.ok("E-mail de rappel envoyé pour la réservation ID: " + reservationId);
    }

    @GetMapping("/feedback/{reservationId}")
    public ResponseEntity<String> sendFeedbackRequest(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservation(reservationId);
        mailService.sendFeedbackReservation(reservation);

        return ResponseEntity.ok("E-mail de demande de feedback envoyé pour la réservation ID: " + reservationId);
    }
}
