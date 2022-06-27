package visualization;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("""
                enter mode...
                0: null  1: sin()
                ->\040""");
        new GUI().init(new Scanner(System.in).nextInt());
    }
}
