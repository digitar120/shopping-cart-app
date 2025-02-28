package com.digitar120.shoppingcartapp.exception.globalhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

/**
 * Exception handler involved in the FC fallback behavior.
 * @author Gabriel Pérez (digitar120)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDTO> buildErrorDTO(CustomHttpStatusException ex){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<ErrorDTO>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> generateNotFoundException(NotFoundException ex){
        return buildErrorDTO(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> generateBadRequestException(BadRequestException ex){
        return buildErrorDTO(ex);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorDTO> generateServiceUnavailableException(ServiceUnavailableException ex){ return buildErrorDTO(ex);}
}
