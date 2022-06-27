package visualization;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("enter mode...");
        new GUI().init(new Scanner(System.in).nextInt());
    }
}
