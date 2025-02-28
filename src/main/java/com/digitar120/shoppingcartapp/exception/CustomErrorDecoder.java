package com.digitar120.shoppingcartapp.exception;

import com.digitar120.shoppingcartapp.exception.globalhandler.BadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Part of the fallback functions for the user FeignClient client. Currently, a work-in-progress.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.feignclient.UserFeignClientFallback
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    /**
     * Defines reactions for when an FC call is answered with codes 4xx or 5xx.
     * @param methodKey {@link feign.Feign#configKey} of the java method that invoked the request. ex.
     *        {@code IAM#getUser()}
     * @param response HTTP response where {@link Response#status() status} is greater than or equal
     *        to {@code 300}.
     * @return Returns {@link BadRequestException} for 4xx errors and {@link ResponseStatusException} with code 500 for 5xx errors.
     */
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
