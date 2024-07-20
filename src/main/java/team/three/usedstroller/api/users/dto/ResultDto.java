package team.three.usedstroller.api.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultDto {

  private HttpStatus status;
  private int statusCode;
  private boolean success;
  private String message;

  public ResultDto(HttpStatus status, boolean success, String message) {
    this.status = status;
    this.statusCode = status.value();
    this.success = success;
    this.message = message;
  }

  public static ResultDto of(HttpStatus status, boolean success, String message) {
    return new ResultDto(status, success, message);
  }
}
