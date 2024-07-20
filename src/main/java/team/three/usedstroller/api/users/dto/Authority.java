package team.three.usedstroller.api.users.dto;

import org.springframework.util.StringUtils;

public enum Authority {
  ROLE_USER,
  ROLE_ADMIN
  ;

  public static Authority findByName(String name) {
    if (!StringUtils.hasText(name)) {
      return null;
    }

    for (Authority value : Authority.values()) {
      if (name.equals(value.name())) {
        return value;
      }
    }

    return null;
  }
}
