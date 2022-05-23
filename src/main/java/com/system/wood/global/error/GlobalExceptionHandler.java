package com.system.wood.global.error;

import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto> handleBusinessException(BusinessException e) {
        e.printStackTrace();
        return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(e.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handelOtherExceptions(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(500));
    }
}
