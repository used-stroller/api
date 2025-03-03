package team.three.usedstroller.api.common.jwt;

// TODO...MSJ 2024.12.18
public class EndPointConf {

    // 인증제외 ENDPOINT
    public static final String[] NOT_JWT_AUTH_ENDPOINT_LIST = {
            "/product/list/**",
            "/product/get/**",
    };
}
