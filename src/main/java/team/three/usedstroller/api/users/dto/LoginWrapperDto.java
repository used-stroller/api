package team.three.usedstroller.api.users.dto;

import lombok.Data;

@Data
public class LoginWrapperDto {
  public LoginResultDto loginResult;

  @Data
  public static class LoginResultDto {
    public UserDto user;
    public String expires;

    @Data
    public static class UserDto {
      private String name;
      private String image;
      private String kakaoId;
      private String profileImage;
    }
  }
}
