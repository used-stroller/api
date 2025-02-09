package team.three.usedstroller.api.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
public class SecurityUtil {
	public static Long getAccountId() {
		return AuthContextStore.getUserContext("accountId");
	}
}
