package team.three.usedstroller.api.common.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import team.three.usedstroller.api.error.ApiErrorCode;

@Data
@Builder
@SuppressWarnings("unchecked")
public class ResponseDto<T> {
  private final LocalDateTime timestamp = LocalDateTime.now();

  private final int status;

  private final String errorCode;

  private final String errorMessage;

  private final T data;

  public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(ApiErrorCode errorCode, T value) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body((ResponseDto<T>) ResponseDto.builder()
            .status(HttpStatus.OK.value())
            .errorCode(errorCode.getCode())
            .errorMessage("")
            .data(value)
            .build()
        );
  }

  public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(T value) {
    return ResponseEntity
        .status(ApiErrorCode.SUCCESS.getStatus())
        .body((ResponseDto<T>) ResponseDto.builder()
            .status(ApiErrorCode.SUCCESS.getStatus().value())
            .errorCode(ApiErrorCode.SUCCESS.getCode())
            .errorMessage("")
            .data(value)
            .build()
        );
  }

  public static ResponseEntity<?> toResponseEntity(ApiErrorCode responseStatus) {
    return ResponseEntity
        .status(responseStatus.getStatus())
        .body(ResponseDto.builder()
            .status(responseStatus.getStatus().value())
            .errorCode(responseStatus.getCode())
            .errorMessage(responseStatus.getMessage())
            .build());
  }
}