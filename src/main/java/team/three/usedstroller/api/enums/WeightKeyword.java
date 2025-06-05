
package team.three.usedstroller.api.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeightKeyword {
	SAFE_SCORE(1, "안정성", "safe_score"),
	DRIVE_SCORE(2, "주행감", "drive_score"),
	BRAND_SCORE(3, "인지도", "brand_score"),
	PRICE_SCORE(4, "가성비", "price_score"),
	WEIGHT_SCORE(5, "무게", "weight_score"),
	FLIGHT_SCORE(6, "해외여행", "flight_score");

	private final int code;
	private final String label;
	private final String dbColumn;

	// 코드로 enum 해당 enum 가져옴
	public static Optional<WeightKeyword> fromCode(int code) {
		return Arrays.stream(values())
			.filter(k -> k.code == code)
			.findFirst();
	}

	// 코드로 라벨값만 가져옴
	public static String labelOf(int code) {
		return fromCode(code)
			.map(WeightKeyword::getLabel)
			.orElse("알 수 없음");
	}


}
