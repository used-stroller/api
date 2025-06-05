package team.three.usedstroller.api.error;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	private final ApiErrorCode error;

	public ApiException(ApiErrorCode e) {
		super(e.getMessage());
		this.error = e;
	}
}
