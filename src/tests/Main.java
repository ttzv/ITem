package tests;


import java.util.Random;

public class Main {

    public static void main(String[] args) {

      String testTxt = "abc a";
      if(testTxt.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*|\\s|[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+\\s\\w+")){
          System.out.println("true");
      } else {
          System.out.println("false");
      }

    }
}
