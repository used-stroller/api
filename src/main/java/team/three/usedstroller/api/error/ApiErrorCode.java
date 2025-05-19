package team.three.usedstroller.api.error;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ApiErrorCode {
  // 200 OK
  SUCCESS(HttpStatus.OK, "S00000", "성공"),

  // --- 400 BAD_REQUEST
  RUNTIME_EXCEPTION           (HttpStatus.BAD_REQUEST,  "E40000"),
  INVALID_REQUEST             (HttpStatus.BAD_REQUEST,  "E40003" , "잘못된 요청입니다."),
  MEMBER_NOT_FOUND            (HttpStatus.BAD_REQUEST,  "E40050" , "존재하지 않은 회원입니다."),
  PRODUCT_NOT_FOUND            (HttpStatus.BAD_REQUEST,  "E40051" , "상품정보가 없습니다."),
  NOT_PRODUCT_OWNER            (HttpStatus.BAD_REQUEST,  "E40052" , "상품소유주가 아닙니다."),

  // 401 UNAUTHORIZED
  INVALID_TOKEN               (HttpStatus.UNAUTHORIZED, "E40100" , "토큰이 유효하지 않습니다."),
  INVALID_REFRESH_TOKEN       (HttpStatus.UNAUTHORIZED, "E40101" , "리프레시 토큰이 유효하지 않습니다."),
  MISMATCH_REFRESH_TOKEN      (HttpStatus.UNAUTHORIZED, "E40102" , "리프레시 토큰의 유저 정보가 일치하지 않습니다."),

  // 403 FORBIDDEN
  ACCESS_DENIED_EXCEPTION     (HttpStatus.FORBIDDEN,    "E40300" , "접근할 수 없는 권한 입니다"),

  // --- 404 NOT_FOUND
  RESOURCE_NOT_FOUND          (HttpStatus.NOT_FOUND,    "E40400" , "요청한 리소스를 찾을 수 없습니다"),
  MODEL_NOT_FOUND             (HttpStatus.NOT_FOUND,  "E40001" , "조건에 해당하는 모델이 없습니다. \n조건을 다시 입력해주세요"),

  // 408 REQUEST_TIMEOUT
  NETWORK_TIMEOUT_ERROR       (HttpStatus.REQUEST_TIMEOUT, "E40901", "네트워크 오류가 발생했습니다. 다시 시도해주세요"),
  REQUEST_TIMEOUT_ERROR_RETRY (HttpStatus.REQUEST_TIMEOUT, "E40902", "잠시 문제가 발생했어요. 다시 시도해 주세요!"),

  // 409 CONFLICT
  DUPLICATE_RESOURCE          (HttpStatus.CONFLICT,     "E40901" , "데이터가 이미 존재합니다"),
  DUPLICATE_CAR               (HttpStatus.CONFLICT,     "E40902" , "이미 등록된 차량입니다"),
  ALREADY_CANCELED            (HttpStatus.CONFLICT,     "E40903" , "이미 취소된 내역입니다."),
  DUPLICATED_NICKNAME         (HttpStatus.CONFLICT,     "E40904" , "중복된 닉네임입니다."),

  // 500 INTERNAL_SERVER_ERROR:
  INTERNAL_SERVER_ERROR       (HttpStatus.INTERNAL_SERVER_ERROR, "E50000", "요청사항을 수행할 수 없습니다"),
  ;

  private final HttpStatus status;
  private final String code;
  private String message;
  private String title;

  ApiErrorCode(HttpStatus status, String code) {
    this.status = status;
    this.code = code;
  }

  ApiErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  ApiErrorCode(HttpStatus status, String code, String message, String title) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.title = title;
  }
}
