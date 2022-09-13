package visualization;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("""
                enter mode...
                0: null  1: sin()  2: wave&particle
                ->\040""");
        new V().init(new Scanner(System.in).nextInt());
    }
}
