package team.three.usedstroller.api.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestPage<T> extends PageImpl<T> {
  @JsonCreator(mode = Mode.PROPERTIES)
  public RestPage(@JsonProperty("content")List<T> content,
      @JsonProperty("number") int page,
      @JsonProperty("size") int size,
      @JsonProperty("totalElements") long totalElements) {
    super(content, PageRequest.of(page, size), totalElements);
  }

  public RestPage(Page<T> page) {
    super(page.getContent(), page.getPageable(), page.getTotalElements());
  }
}
