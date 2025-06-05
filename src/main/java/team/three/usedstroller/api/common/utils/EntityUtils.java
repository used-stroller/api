package team.three.usedstroller.api.common.utils;

import java.util.Optional;

import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.error.ApiException;

/**
 * 기본 Entity 조회시 사용하는 메서드
 * 값이 없을 경우, ApiErrorCode 예외 발생됨
 */
public class EntityUtils {
	public static<T> T findOrThrow(Optional<T> optional, ApiErrorCode errorCode) {
		return optional.orElseThrow(() -> new ApiException(errorCode));
	}
}
