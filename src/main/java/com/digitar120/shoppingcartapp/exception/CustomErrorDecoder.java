package com.digitar120.shoppingcartapp.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is5xxServerError()) {
            return new ResponseStatusException(responseStatus, "Server-side error");
        } else if (responseStatus.is4xxClientError()) {
            return new ResponseStatusException(responseStatus, "Request error");
        } else {
            return new Exception("Generic exception");
        }
    }
}
