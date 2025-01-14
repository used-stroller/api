package team.three.usedstroller.api.error;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {
	@ExceptionHandler({ApiException.class})
	public ResponseEntity<ApiExceptionDto> exceptionHandler(HttpServletRequest request, final ApiException ex) {
		//ex.printStackTrace();
		return ResponseEntity
			.status(ex.getError().getStatus())
			.body(ApiExceptionDto.builder()
				.status(ex.getError().getStatus())
				.errorTitle(ex.getError().getTitle())
				.errorCode(ex.getError().getCode())
				.errorMessage(ex.getError().getMessage())
				.build());
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ApiExceptionDto> exceptionHandler(HttpServletRequest request, final RuntimeException ex) {
		//ex.printStackTrace();
		return ResponseEntity
			.status(ApiErrorCode.RUNTIME_EXCEPTION.getStatus())
			.body(ApiExceptionDto.builder()
				.status(ApiErrorCode.RUNTIME_EXCEPTION.getStatus())
				.errorCode(ApiErrorCode.RUNTIME_EXCEPTION.getCode())
				.errorMessage(ex.getMessage())
				.build());
	}

	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ApiExceptionDto> exceptionHandler(HttpServletRequest request, final AccessDeniedException ex) {
		//ex.printStackTrace();
		return ResponseEntity
			.status(ApiErrorCode.ACCESS_DENIED_EXCEPTION.getStatus())
			.body(ApiExceptionDto.builder()
				.status(ApiErrorCode.ACCESS_DENIED_EXCEPTION.getStatus())
				.errorCode(ApiErrorCode.ACCESS_DENIED_EXCEPTION.getCode())
				.errorMessage(ex.getMessage())
				.build());
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ApiExceptionDto> exceptionHandler(HttpServletRequest request, final Exception ex) {
		//ex.printStackTrace();
		return ResponseEntity
			.status(ApiErrorCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ApiExceptionDto.builder()
				.status(ApiErrorCode.INTERNAL_SERVER_ERROR.getStatus())
				.errorCode(ApiErrorCode.INTERNAL_SERVER_ERROR.getCode())
				.errorMessage(ex.getMessage())
				.build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionDto> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException ex) {
		//ex.printStackTrace();
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.findFirst()
			.orElse("유효성 검사에 실패하였습니다.");

		// ResponseEntity 객체 생성
		ResponseEntity<ApiExceptionDto> responseEntity = ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiExceptionDto.builder()
				.status(HttpStatus.BAD_REQUEST)
				.errorCode(ApiErrorCode.INVALID_REQUEST.getCode())
				.errorMessage(errorMessage)
				.build());

		// ResponseEntity 객체를 문자열로 변환하여 출력
		String responseEntityAsString = responseEntity.toString();
		System.out.println(responseEntityAsString);


		// ResponseEntity 객체 반환
		return responseEntity;
	}

}
