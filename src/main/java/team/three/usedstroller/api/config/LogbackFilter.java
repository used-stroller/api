package team.three.usedstroller.api.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogbackFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        // 모든 로그를 출력
        return FilterReply.ACCEPT;

        /* 이 부분을 사용하면 로그 메시지에 "log_password"가 포함된 로그가 출력되지 않음
        if (event.getMessage().contains("login_password")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.ACCEPT;
        }
        */
    }
}