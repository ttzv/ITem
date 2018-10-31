package tests;

public class Main {

    public static void main(String[] args) {
        String test = "LLabcaWAAbcLL";
        String regex = "(LL.*)(.*LL)";

        if (test.matches(regex)){
            System.out.println(test.replaceAll("", "THIS"));
        } else {
            System.out.println(false);
        }
    }
}
