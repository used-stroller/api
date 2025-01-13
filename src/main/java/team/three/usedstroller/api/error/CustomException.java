package team.three.usedstroller.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
  private final ApiErrorCode apiErrorCode;

  @Override
  public Throwable fillInStackTrace() {
    return this;
  }

  @Override
  public String getMessage() {
    return apiErrorCode.getStatus() + " / " + apiErrorCode.getCode() + " / " + apiErrorCode.getMessage();
  }
}
