package team.three.usedstroller.api.common.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SecurityUtil {
	public static Long getAccountId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return Long.parseLong(userDetails.getUsername());
	}
}
