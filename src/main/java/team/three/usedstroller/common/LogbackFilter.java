package team.three.usedstroller.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogbackFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        return FilterReply.ACCEPT;

        /*
        if (event.getMessage().contains("login_password")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.ACCEPT;
        }
        */
    }
}