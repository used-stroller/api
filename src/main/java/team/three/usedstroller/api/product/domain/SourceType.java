package team.three.usedstroller.api.product.domain;

import lombok.Getter;

@Getter
public enum SourceType {
	NAVER("naver"),
	CARROT("carrot"),
	SECOND("second"),
	BUNJANG("bunjang"),
	JUNGGO("junggo"),
	JUNGMOCHA("jungmocha");

	private String source;

	SourceType(String source) {
		this.source = source;
	}
}
