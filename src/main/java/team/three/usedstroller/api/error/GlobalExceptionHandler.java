package team.three.usedstroller.api.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import team.three.usedstroller.api.common.dto.ResponseDto;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {CustomException.class})
  protected ResponseEntity<?> handleCustomException(CustomException ex) {
    //log.error("CustomException : {}", ex.getApiErrorCode().name(), ex);
    return ResponseDto.toResponseEntity(ex.getApiErrorCode());
  }
}
