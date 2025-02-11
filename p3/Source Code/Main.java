import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String nodeFile = args[0];
        String edgeFile = args[1];
        String objFile = args[2];
        String outputFile = args[3];

        Land land = new Land();
        Logger logger = new Logger(outputFile);

        MyFileReader myFileReader = new MyFileReader();
        myFileReader.nodeFileParser(nodeFile, land);
        myFileReader.edgeFileParser(edgeFile, land);
        myFileReader.objFileParser(objFile, land, logger);
        logger.close();
        
    }
}
