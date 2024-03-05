package team.three.usedstroller.api.domain;

import lombok.Getter;

@Getter
public enum SourceType {
	NAVER("naver"),
	CARROT("carrot"),
	SECOND("second"),
	BUNJANG("bunjang"),
	JUNGGO("junggo");

	private String source;

	SourceType(String source) {
		this.source = source;
	}
}
