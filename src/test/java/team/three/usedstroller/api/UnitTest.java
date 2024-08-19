package team.three.usedstroller.api;

import org.junit.jupiter.api.Test;


class UnitTest {

  @Test
  void tenOrEleven (){
    class Static{
      public int a =20;
      static int b= 0;
    }
    int a;
    a=10;
    Static.b = a;
    Static st = new Static();
    System.out.println(Static.b++);
  }
}
