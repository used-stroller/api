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
  REQUEST_FAIL                (HttpStatus.BAD_REQUEST,  "E40001" , "요청이 실패하였습니다."),
  INVALID_DATA                (HttpStatus.BAD_REQUEST,  "E40002" , "유효하지 않는 데이터 입니다."),
  INVALID_REQUEST             (HttpStatus.BAD_REQUEST,  "E40003" , "잘못된 요청입니다."),
  INVALID_NICK_NAME           (HttpStatus.BAD_REQUEST,  "E40010" , "잘못된 닉네임 요청입니다."),
  INVALID_CARETALK_ROOM       (HttpStatus.BAD_REQUEST,  "E40017" , "종료된 케어톡입니다."),
  INVALID_PHONE_NUMBER        (HttpStatus.BAD_REQUEST,  "E40018" , "본인의 휴대전화 번호만 허용됩니다."),
  // 탁송
  DUPLICATE_CONSIGNMENT       (HttpStatus.BAD_REQUEST,  "E40030" , "같은 차량에 대해 중복된 탁송요청입니다."),
  CONSIGNEE_NOT_FOUND         (HttpStatus.BAD_REQUEST,  "E40031" , "인수자를 찾을수 없습니다."),
  ALREADY_CONSIGNMENT_CANCELED(HttpStatus.BAD_REQUEST,  "E40032" , "이미 탁송 취소 상태입니다"),
  CONSIGNMENT_CANCEL_FAIL     (HttpStatus.BAD_REQUEST,  "E40033" , "탁송중 또는 완료시에는 탁송을 취소할수 없습니다."),
  CONSIGNMENT_REQUEST_FAIL    (HttpStatus.BAD_REQUEST,  "E40034" , "탁송 요청이 실패하였습니다."),
  EXPIRED_PAYMENT_TIME        (HttpStatus.NOT_FOUND,    "E40035" , "탁송 결제시간이 초과되었습니다."),
  // 명의이전
  DUPLICATE_TRANSFER          (HttpStatus.BAD_REQUEST,  "E40040" , "같은 차량에 대해 중복된 명의이전 요청입니다."),
  CAN_NOT_CANCEL_TRANSFER     (HttpStatus.BAD_REQUEST,  "E40045" , "명의이전을 취소할 수 없습니다"),
  SAME_TRANSFER_MEMBERS       (HttpStatus.BAD_REQUEST,  "E40046" , "양도인과 양수인이 같습니다. 다시 확인해 주세요."),
  MEMBER_NOT_FOUND            (HttpStatus.BAD_REQUEST,  "E40050" , "존재하지 않은 회원입니다. \n이름과 연락처를 다시 확인해 주세요."),
  // 진단
  DUPLICATE_DIAGNOSIS         (HttpStatus.BAD_REQUEST,  "E40060" , "같은 차량에 대해 중복된 진단 요청입니다."),
  CAN_NOT_CANCEL_DIAGNOSIS    (HttpStatus.BAD_REQUEST,  "E40061" , "진단신청을 취소할 수 없습니다."),
  CAN_NOT_PROCEED_DIAGNOSIS   (HttpStatus.BAD_REQUEST,  "E40062" , "진단 요청을 진행 할 수 없습니다."),
  DIAGNOSIS_ALREADY_REQUESTED (HttpStatus.BAD_REQUEST,  "E40063" , "이미 신청되었습니다."),
  CAN_NOT_MODIFY_STATUS       (HttpStatus.BAD_REQUEST,  "E40065" , "출장진단 진행중인 차량은 차고보관, 판매중으로만 변경이 가능합니다."),
  DIAG_CENTER_CAN_NOT_MODIFY_STATUS (HttpStatus.BAD_REQUEST,  "E40065" , "안심진단 진행중인 차량은 차고보관, 판매중으로만 변경이 가능합니다."),
  CAN_NOT_REMOVE_CAR          (HttpStatus.BAD_REQUEST,  "E40066" , "진단 진행중인 차량은 삭제가 불가능합니다."),
  //CAN_NOT_REMOVE_CAR_CENTER   (HttpStatus.BAD_REQUEST,  "E40066" , "안심진단 진행중인 차량은 삭제가 불가능합니다."),
  CAN_NOT_MODIFY_SOLDOUT      (HttpStatus.BAD_REQUEST,  "E40067" , "출장진단 신청이 진행중인 차량은 판매완료로 변경이 불가능합니다."),
  DIAG_CENTER_CAN_NOT_MODIFY_SOLDOUT      (HttpStatus.BAD_REQUEST,  "E40067" , "안심진단 신청이 진행중인 차량은 판매완료로 변경이 불가능합니다."),
  DIAGNOSIS_NOT_FOUND         (HttpStatus.BAD_REQUEST,  "E40068" , "진단내역이 존재하지 않습니다."),
  //CAN_NOT_REMOVE_CAR_DS2      (HttpStatus.BAD_REQUEST,  "E40069" , "출장진단 진행중인 차량은 삭제가 불가능합니다."),
  RESERVATION_ALREADY_OCCUPIED(HttpStatus.BAD_REQUEST,  "E41000" , "이미 예약완료된 시간입니다"),
  RESERVATION_NOT_FOUND       (HttpStatus.BAD_REQUEST,  "E41001" , "예약내역이 없습니다."),
  RESERVATION_NOT_CHANGEABLE  (HttpStatus.BAD_REQUEST,  "E41002" , "진단 예약일시 24시간 이내에는 예약 변경이 불가합니다."),
  RESERVATION_SAME_AS_BEFORE  (HttpStatus.BAD_REQUEST,  "E41003" , "기존 예약정보와 동일합니다.\n" + "다른 예약시간을 선택해 주세요."),
  TRANSFER_IN_PROGRESS_ERROR  (HttpStatus.BAD_REQUEST,  "E41004" , "명의이전 중에는 \n" + "안심진단을 신청할 수 없습니다."),
  CAR_CENTER_NOT_AVAILABLE    (HttpStatus.BAD_REQUEST,  "E41005" , "해당 정비소의 사정으로 \n진단을 이용할 수 없습니다.\n다른 정비소를 선택해주세요."),
  // 탈퇴
  WITHDRAW_FAIL_RESTRICT      (HttpStatus.BAD_REQUEST,  "E40015" , "차고 상태(예약중) 또는 서비스 이용내역 및 \n케어톡 진행 상태를 확인해주세요.\n지속적으로 탈퇴가 불가할 경우 \n1:1문의를 이용해주세요.", "회원탈퇴를 할 수 없습니다."),
  // 가입, 재가입
  JOIN_FAIL_OFFICIO_WITHDRAW  (HttpStatus.BAD_REQUEST,  "E41017" , "직권 탈퇴로 인해 재가입할 수 없습니다."),

  // PAYMENT
  PAYMENT_NOT_FOUND           (HttpStatus.BAD_REQUEST,  "E42001" , "PAYMENT 내역이 존재하지 않습니다."),
  PAYMENT_RESULT_NOT_FOUND    (HttpStatus.BAD_REQUEST,  "E42002" , "PAYMENT_RESULT 내역이 존재하지 않습니다."),
  PAYMENT_STATUS_NOT_FOUND    (HttpStatus.BAD_REQUEST,  "E42003" , "PAYMENT_STATUS 내역이 존재하지 않습니다."),
  PAYMENT_TIME_OVER           (HttpStatus.BAD_REQUEST,  "E40090" , "결제 가능 시간이 초과되었습니다."),

  // 그 외
  UPDATE_UNAVAILABLE          (HttpStatus.BAD_REQUEST,  "E40064" , "수정이 불가능한 단계입니다."),
  // 댓글
  NOT_OWNER_OF_COMMENT        (HttpStatus.BAD_REQUEST,  "E40070" , "본인이 작성한 댓글이 아닙니다."),
  CANNOT_COMPLAIN_OWN_COMMENT (HttpStatus.BAD_REQUEST,  "E40071" , "본인이 작성한 댓글은 신고할 수 없습니다."),
  ALREADY_COMPLAINED_COMMENT  (HttpStatus.BAD_REQUEST,  "E40072" , "신고한 댓글입니다."),
  ALREADY_DELETED_COMMENT     (HttpStatus.BAD_REQUEST,  "E40073" , "삭제된 댓글입니다."),
  OVER_COMMENT_LIMIT_MINUTE   (HttpStatus.BAD_REQUEST,  "E40074" , "짧은 시간동안 많은 댓글을 등록하셨습니다."),
  REPEAT_COMMENT_LIMIT_HOUR   (HttpStatus.BAD_REQUEST,  "E40075" , "같은 내용을 반복적으로 입력하셨습니다."),
  // 이벤트
  NOT_USED_EVENT              (HttpStatus.BAD_REQUEST,  "E40076" , "미사용 이벤트입니다."),
  ALREADY_DELETED_EVENT       (HttpStatus.BAD_REQUEST,  "E40077" , "삭제된 이벤트입니다."),
  EVENT_NOT_FOUND             (HttpStatus.BAD_REQUEST,  "E40078" , "이벤트 정보가 존재하지 않습니다."),
  // 그 외
  API_RESPONSE_FAIL           (HttpStatus.BAD_REQUEST,  "E40080" , "일시적인 오류가 발생했습니다. \n잠시 후 다시 시도해주세요"),
  PAYMENT_NOT_YET             (HttpStatus.BAD_REQUEST,  "E40081" , "결제가 완료되지 않았습니다."),
  CAR_CENTER_NOT_FOUND        (HttpStatus.BAD_REQUEST,  "E40082" , "정비소를 찾을 수 없습니다."),

  // 401 UNAUTHORIZED
  INVALID_TOKEN               (HttpStatus.UNAUTHORIZED, "E40100" , "토큰이 유효하지 않습니다."),
  INVALID_REFRESH_TOKEN       (HttpStatus.UNAUTHORIZED, "E40101" , "리프레시 토큰이 유효하지 않습니다."),
  MISMATCH_REFRESH_TOKEN      (HttpStatus.UNAUTHORIZED, "E40102" , "리프레시 토큰의 유저 정보가 일치하지 않습니다."),

  // 403 FORBIDDEN
  ACCESS_DENIED_EXCEPTION     (HttpStatus.FORBIDDEN,    "E40300" , "접근할 수 없는 권한 입니다"),
  DUPLICATED_MEMBER           (HttpStatus.FORBIDDEN,    "E40301" , "이미 가입되어 있는 유저입니다."),
  JOIN_FAIL_RECENTLY_WITHDRAW (HttpStatus.FORBIDDEN,    "E40301" , "회원 탈퇴 후 30일이 지나지 않아\n붕붕마켓 이용이 불가능합니다.\n홈으로 이동합니다.", "탈퇴한 회원입니다."),
  RESTRICTED_MEMBER           (HttpStatus.FORBIDDEN,    "E40302" , "로그인이 제한 된 유저입니다."),
  SIGNUP_RESTRICTED           (HttpStatus.FORBIDDEN,    "E40302" , "서비스 이용이 제한된 사용자입니다.\n문제 해결이 필요하시다면 \n아래의 문의하기를 이용해주세요.", "붕붕마켓 이용이 제한되었습니다."),
  LOGIN_RESTRICTED            (HttpStatus.FORBIDDEN,    "E40302" , "서비스 이용이 제한된 사용자입니다.\n문제 해결이 필요하시다면 \n아래의 문의하기를 이용해주세요.", "붕붕마켓 이용이 제한되었습니다."),

  // --- 404 NOT_FOUND
  RESOURCE_NOT_FOUND          (HttpStatus.NOT_FOUND,    "E40400" , "요청한 리소스를 찾을 수 없습니다"),
  WITHDRAW_MEMBER             (HttpStatus.NOT_FOUND,    "E40401" , "탈퇴 한 유저입니다."),
  REFRESH_TOKEN_NOT_FOUND     (HttpStatus.NOT_FOUND,    "E40403" , "로그아웃 된 사용자입니다"),
  PUSH_ALARM_NOT_FOUND        (HttpStatus.NOT_FOUND,    "E40432" , "해당 푸쉬알람 정보를 찾을 수 없습니다"),
  FCM_TOKEN_NOT_FOUNT         (HttpStatus.NOT_FOUND,    "E40450" , "fcm 토큰을 찾을수 없습니다."),
  // 차
  CAR_NOT_FOUND               (HttpStatus.NOT_FOUND,    "E40420" , "해당 자동차 정보를 찾을 수 없습니다"),
  CAR_USER_INFO_NOT_FOUND     (HttpStatus.NOT_FOUND,    "E40421" , "차량 정보를 찾을 수 없습니다. \n소유자 이름과 차량번호를 다시 확인해 주세요."),
  BRAND_TABLE_NOT_FOUND       (HttpStatus.NOT_FOUND,    "E40430" , "브랜드 테이블이 존재하지 않습니다."),
  DISTANCE_DRIVEN_INCORRECT   (HttpStatus.NOT_FOUND,    "E40422" , "주행거리 입력 과정 중 오류가 발생하였습니다\n주행거리 정보를 다시 입력해 주세요"),
  // 탁송
  CONSIGNMENT_NOT_FOUND       (HttpStatus.NOT_FOUND,    "E40431" , "해당 탁송 정보를 찾을 수 없습니다"),
  // 명의이전
  TRANSFER_NOT_FOUND          (HttpStatus.NOT_FOUND,    "E40460" , "명의이전 정보를 찾을수 없습니다."),
  // 진단
  DIAGNOSIS_PRICE_NOT_FOUND   (HttpStatus.NOT_FOUND,    "E40471" , "진단 가격 정보를 찾을수 없습니다."),
  // 그 외
  TALKROOM_NOT_FOUND          (HttpStatus.NOT_FOUND,    "E40470" , "케어톡방 정보를 찾을수 없습니다."),

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
