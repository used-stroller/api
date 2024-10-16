package team.three.usedstroller.api.common.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ImageUploader {

  private static final String UPLOAD_DIR = "F:stroller/img/product/";

  public void uploadFile(List<MultipartFile> files) {
    StringBuilder sb = new StringBuilder();

    for(MultipartFile file : files) {
      if(file.isEmpty()){
        sb.append("Fail to upload").append(file.getOriginalFilename());
        continue;
      }
      try {
        // 파일 저장 경로 생성
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR+uniqueFileName);
        Files.createDirectories(path.getParent()); //디렉토리 생성
        Path write = Files.write(path, file.getBytes());// 파일 저장
        log.info("Successfully uploaded image {}",file.getOriginalFilename());
      } catch (Exception e){
        e.printStackTrace();
        sb.append("Fail to upload").append(file.getOriginalFilename());
      }
    }
  }

  public void deleteFile (String fileName) {
    try {
      Path path = Paths.get(UPLOAD_DIR+fileName);
      File file = path.toFile();

      if(file.exists()){
        Files.delete(path);
        log.info("Successfully deleted image {}",fileName);
      }else{
        log.info("File does not exist");
      }
    } catch (Exception e){
      e.printStackTrace();
      log.info("Failed to delete image {}",fileName);
    }
  }
}