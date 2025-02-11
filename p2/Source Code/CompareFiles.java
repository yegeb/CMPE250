import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class CompareFiles {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Comparing has been started.");
        File file1 = new File("src/output/otype3_small.txt");
        File file2 = new File("output.txt");
        Scanner sc1 = new Scanner(file1);
        Scanner sc2 = new Scanner(file2);

        int i = 1;
        while (sc1.hasNextLine()){
            if(!Objects.equals(sc1.nextLine(), sc2.nextLine())){
                System.out.println(i + " is different.");
            }
            i++;
        }
        System.out.println("Comparing has been finished.");
    }
}

