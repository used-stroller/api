package team.three.usedstroller.api.common.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ImageDownloader {

  public static void convertToFile(String imageUrl, String model, int seq) throws IOException {
    String fileName = model + "_" + seq + ".jpg";
    String dir = "E:/stroller_img/"+model+"/";
    String destinationFile = dir + fileName;

    //폴더생성
    Path directoryPath = Paths.get(dir);
    Files.createDirectories(directoryPath);
    try {
      URL url = new URL(imageUrl);
      InputStream inputStream = url.openStream();
      //BufferedInputStream bis = new BufferedInputStream(inputStream);
      OutputStream outputStream = new FileOutputStream(destinationFile);
      //BufferedOutputStream bos = new BufferedOutputStream(outputStream);

      byte[] buffer = new byte[2048];
      int length;

      while ((length = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, length);
      }
      inputStream.close();
      outputStream.close();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
