package tn.esprit.language_courses_service.Exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new UserNotFoundException(extractIdFromRequest(response));
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }

    private Long extractIdFromRequest(Response response) {
        // Best-effort: extract ID from URL path, e.g. /users/5
        try {
            String url = response.request().url();
            String[] parts = url.split("/");
            return Long.parseLong(parts[parts.length - 1]);
        } catch (Exception e) {
            return -1L;
        }
    }
}
