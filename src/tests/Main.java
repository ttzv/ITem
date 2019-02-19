package tests;


import java.util.Random;

public class Main {

    public static void main(String[] args) {

        String password = new Random().ints(8, 64, 122).collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(password);

    }
}
