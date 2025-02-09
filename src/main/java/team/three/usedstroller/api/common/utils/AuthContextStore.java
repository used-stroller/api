package team.three.usedstroller.api.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@RequiredArgsConstructor
public class AuthContextStore {
  // 인증된 사용자 데이터를 저장할 ConcurrentHashMap
  private static final ConcurrentHashMap<String, Long> contextStore = new ConcurrentHashMap<>();

  // 인증 완료 후 사용자 데이터를 저장
  public static void setUserContext(String key, Long accountId) {
    contextStore.put(key, accountId);
  }

  // 사용자 데이터 조회
  public static Long getUserContext(String key) {
    return contextStore.get(key);
  }

  // 데이터 삭제 (Optional)
  public static void removeUserContext(String key) {
    contextStore.remove(key);
  }
}
