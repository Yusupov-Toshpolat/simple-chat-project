package uz.nt.simplechatproject.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.nt.simplechatproject.dto.ResponseDTO;
import uz.nt.simplechatproject.dto.ValidatorDTO;
import uz.nt.simplechatproject.exceptions.ImageNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidatorDTO>> handle(MethodArgumentNotValidException e) {
        List<ValidatorDTO> errors = e.getBindingResult().getFieldErrors().stream()
                .map(ee -> ValidatorDTO.builder()
                        .fieldName(ee.getField())
                        .error(ee.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IOException.class)
    public void handleIO(IOException e){
        ResponseEntity.status(500).body("Error while reading/saving file " + e.getMessage());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ResponseDTO<?>> handleImageNotFound(ImageNotFoundException e){
        return ResponseEntity.status(404).body(
                ResponseDTO.builder()
                        .code(-1)
                        .message("Image not found")
                        .success(false)
                        .data(e.getMessage())
                        .build()
        );
    }
}
