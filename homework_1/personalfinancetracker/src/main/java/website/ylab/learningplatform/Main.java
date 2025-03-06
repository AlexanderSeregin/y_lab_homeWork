package website.ylab.learningplatform;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 'q' to quit, 'c' to continue");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            } else if (input.equals("c")) {
                System.out.println("Continue");
            } else {
                System.out.println("Invalid input");
            }
        }

    }
}