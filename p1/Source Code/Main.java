import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        String inputFile = args[0];
        String outputFile = args[1];


        Actions actions = new Actions(outputFile); 
        actions.fileReader(inputFile);

    }

}

