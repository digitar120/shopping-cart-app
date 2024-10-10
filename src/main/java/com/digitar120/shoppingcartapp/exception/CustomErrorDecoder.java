package com.digitar120.shoppingcartapp.exception;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
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
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server-side error");
        }

        else if (responseStatus.is4xxClientError()) {
            return new BadRequestException("Request error");
        }

        else {
            return new Exception("Generic exception");
        }
    }
}
