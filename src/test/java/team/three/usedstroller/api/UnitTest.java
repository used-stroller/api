package team.three.usedstroller.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

public class UnitTest {

  @Test
  void DateFormatter(){
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
    String date = now.format(formatter);
    System.out.println("date = " + date);
    
  }
}
