package m1.info.reza.user_feedback;

import m1.info.reza.user_feedback.DTO.LoginResponse;
import m1.info.reza.user_feedback.DTO.SubGroupResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UserFeedbackService {

    private final RestTemplate restTemplate;

    public UserFeedbackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String loginAndGetToken() {
        String url = "https://api.user-feedback.oups.net/api/auth/login";

        String requestJson = """
            {
                "username": "reza",
                "password": "1234"
            }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<LoginResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                LoginResponse.class
        );

        return response.getBody() != null ? response.getBody().token() : null;
    }

    public Long createSubGroup(String name, String token) {
        String url = "https://api.user-feedback.oups.net/api/groups/2/subgroups";

        String requestJson = """
            {
                "name": "%s",
                "description": "Restaurant, propulsé par 👨🏼‍🍳 REZA"
            }
        """.formatted(name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<SubGroupResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                SubGroupResponse.class
        );

        SubGroupResponse responseBody = response.getBody();
        return responseBody != null ? responseBody.id() : null;
    }
}