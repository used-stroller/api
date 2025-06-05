package team.three.usedstroller.api.util;

import java.net.URI;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebIntegrationTest {

  @LocalServerPort
  int port;

  public URI uri(String path) {
    try {
      return new URI(String.format("http://localhost:%d%s", port, path));
    } catch (Exception e) {
      throw new IllegalArgumentException();
    }
  }



}
