package team.three.usedstroller.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterType {
	Ongoing("판매중"),
	Reserved("예약중"),
	Closed("판매완료");

	private final String status;
}
