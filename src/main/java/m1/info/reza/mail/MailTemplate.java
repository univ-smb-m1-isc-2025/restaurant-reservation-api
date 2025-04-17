package m1.info.reza.mail;

import m1.info.reza.reservation.Reservation;

public class MailTemplate {

    public static String buildReservationConfirmationEmail(
            Reservation reservation
    ) {
        return """
        <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #348501;">Confirmation de votre réservation</h2>
                <p>Bonjour,</p>
                <p>Nous avons bien enregistré votre réservation au restaurant <strong>%s</strong>.</p>
                <ul>
                    <li><strong>Lieu :</strong> %s</li>
                    <li><strong>Date :</strong> %s</li>
                    <li><strong>Heure :</strong> %s</li>
                    <li><strong>Nombre de couverts :</strong> %d</li>
                </ul>
                <p>Nous avons hâte de vous accueillir !</p>
                <p style="font-size: 0.9em; color: #888;">Si vous avez des questions, n'hésitez pas à nous contacter.</p>
            </body>
        </html>
        """.formatted(
                reservation.getRestaurant().getName(),
                reservation.getRestaurant().getFullAddress(),
                reservation.getFormattedFrenchDate(),
                reservation.getFormattedFrenchTime(),
                reservation.getNbGuests()
        );
    }

    public static String buildReservationReminderEmail(Reservation reservation) {
        return """
        <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #348501;">Rappel de votre réservation</h2>
                <p>Bonjour,</p>
                <p>Un petit rappel concernant votre réservation au restaurant <strong>%s</strong> demain :</p>
                <ul>
                    <li><strong>Lieu :</strong> %s</li>
                    <li><strong>Date :</strong> %s</li>
                    <li><strong>Heure :</strong> %s</li>
                    <li><strong>Nombre de couverts :</strong> %d</li>
                </ul>
                <p>Nous avons hâte de vous accueillir !</p>
                <p style="font-size: 0.9em; color: #888;">Si vous avez des questions ou des changements de dernière minute, n'hésitez pas à nous contacter.</p>
            </body>
        </html>
        """.formatted(
                reservation.getRestaurant().getName(),
                reservation.getRestaurant().getFullAddress(),
                reservation.getFormattedFrenchDate(),
                reservation.getFormattedFrenchTime(),
                reservation.getNbGuests()
        );
    }

    public static String buildFeedbackRequestEmail(Reservation reservation, String feedbackLink) {
        return """
        <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #348501;">Merci de votre visite chez %s !</h2>
                <p>Bonjour,</p>
                <p>Nous espérons que vous avez passé un agréable moment au restaurant <strong>%s</strong>.</p>
                <p>Pour nous aider à améliorer notre service, nous vous invitons à donner votre avis sur votre expérience :</p>
                <p>
                    <a href="%s" style="display: inline-block; background-color: #348501; color: white; padding: 12px 24px; text-align: center; font-size: 16px; text-decoration: none; border-radius: 4px;">Donner mon avis</a>
                </p>
                <p>Merci beaucoup pour votre retour !</p>
                <p style="font-size: 0.9em; color: #888;">Si vous avez des questions ou des suggestions, n'hésitez pas à nous contacter.</p>
            </body>
        </html>
        """.formatted(
                reservation.getRestaurant().getName(),
                reservation.getRestaurant().getName(),
                feedbackLink
        );
    }
}
