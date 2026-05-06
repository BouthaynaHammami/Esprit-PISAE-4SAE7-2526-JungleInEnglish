package tn.esprit.learner_managment_service.DropoutPrediction.Config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class HttpClientConfigTest {

    @Test
    void restTemplateBean_isCreated() {
        HttpClientConfig config = new HttpClientConfig();
        RestTemplate restTemplate = config.restTemplate();
        assertNotNull(restTemplate);
    }
}
