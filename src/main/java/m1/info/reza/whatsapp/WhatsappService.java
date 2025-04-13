package m1.info.reza.whatsapp;

import m1.info.reza.reservation.Reservation;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class WhatsappService {

    private final String WHATSAPP_API_URL = "";
    private final String ACCESS_TOKEN = "";
    private final String DESTINATION_PHONE_NUMBER = "";

    public void sendReservationConfirmation(Reservation reservation) {
        Restaurant restaurant = reservation.getRestaurant();

//        String phoneNumber = reservation.getCustomer().getPhone();
//        phoneNumber = formatToInternational(phoneNumber);

        String reservationDate = reservation.getReservationDate().toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String reservationTime = reservation.getReservationDate().toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        String cancelLink = "https://api.reza.oups.net/reservation/" + restaurant.getId() + "/cancel/" + reservation.getId();

        // Envoie du message
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "to", DESTINATION_PHONE_NUMBER,
                "type", "template",
                "template", Map.of(
                        "name", "reservation_confirmation",
                        "language", Map.of("code", "fr"),
                        "components", List.of(
                                Map.of(
                                        "type", "body",
                                        "parameters", List.of(
                                                Map.of("type", "text", "text", restaurant.getName()),
                                                Map.of("type", "text", "text", reservationDate),
                                                Map.of("type", "text", "text", reservationTime),
                                                Map.of("type", "text", "text", restaurant.getAddress()),
                                                Map.of("type", "text", "text", restaurant.getZipcode()),
                                                Map.of("type", "text", "text", restaurant.getCity()),
                                                Map.of("type", "text", "text", cancelLink)
                                        )
                                )
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(WHATSAPP_API_URL, entity, String.class);
    }

    private String formatToInternational(String phoneNumber) {
        if (phoneNumber.startsWith("0")) {
            return "33" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }

}