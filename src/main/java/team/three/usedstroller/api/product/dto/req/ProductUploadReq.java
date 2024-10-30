package team.three.usedstroller.api.product.dto.req;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.product.domain.Model;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.SourceType;
import team.three.usedstroller.api.product.dto.ImageDto;
import team.three.usedstroller.api.product.dto.OptionDto;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUploadReq implements Serializable {

  private Long id;
  private SourceType sourceType;
  private String title;
  private Long price;
  private String imgSrc;
  private String content;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime updatedAt;

  //private String address;
  //private String region;
  private String buyStatus;
  private List<String> options;
  private int usePeriod;
  private int order;
  private List<MultipartFile> imageList;
}
