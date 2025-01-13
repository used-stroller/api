package team.three.usedstroller.api.common.jwt;

// TODO...MSJ 2024.12.18
public class EndPointConf {

    // 인증제외 ENDPOINT
    public static final String[] NOT_JWT_AUTH_ENDPOINT_LIST = {
            "/auth/**",
            "/terms/**",
            "/car/buy/**",
            "/car/common/**",
            "/fcm/token/**",
            "/servers/openmile/**",
            "/api/faq/**",
            "/api/notification/**",
            "/api/qna/non_member/**",
            "/api/appversion/**",
            "/api/banner/**",
            "/api/popup/**",
            "/api/care/transfer/v1/result",
            "/api/care/transfer/v1/updateresult",
            "/api/cs/v1/main",
            "/api/home/**",

            "/api/event/**",
            "/api/comment/v*/list",
            "/api/operationpolicy/**",
            "/api/comment/v*/complain/codes",
            "/cache/**",
            "/etc/api/**",

            "/api/care/diagnosis/price/v*/price-table",
            "/api/care/diagnosis-center/client/v1/cancel-by-timeout",
            "/api/payment/v*/push/completed", // PG 결제 완료 후 PUSH 등
            "/api/care/transfer/common/v1/cancel-by-admin", // 명의이전 - 관리자 취소

            "/api/t/messages/**", // 앱 임시 메시지 등

            "/test/**",
            "/health**",
            "/port"
    };
}
