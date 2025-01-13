package team.three.usedstroller.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@ToString
public class ApiExceptionDto {
  private final LocalDateTime timestamp;
  private final int status;
  private final String errorTitle;
  private final String errorCode;
  private final String errorMessage;

  @Builder
  public ApiExceptionDto(HttpStatus status, String errorCode, String errorMessage, String errorTitle) {
    this.timestamp = LocalDateTime.now();
    this.status = status.value();
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errorTitle = errorTitle;
  }
}
