import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Wprowadz k: ");
        int k = Integer.parseInt(input.next());

        try {
            new KMeans("iris_training.txt", k);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
