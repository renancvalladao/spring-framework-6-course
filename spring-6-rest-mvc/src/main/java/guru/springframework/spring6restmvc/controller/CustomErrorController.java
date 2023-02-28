package guru.springframework.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<Object> handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException ve) {
            List<Map<String, String>> errors = ve.getConstraintViolations().stream().map(constraintViolation -> {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());

                return errorMap;
            }).toList();

            return bodyBuilder.body(errors);
        }

        return bodyBuilder.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().stream().map(fieldError -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

            return errorMap;
        }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

}
