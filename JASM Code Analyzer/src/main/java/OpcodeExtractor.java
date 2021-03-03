import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OpcodeExtractor {
    public static void opcodeReturn(String linenumber){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\ALI\\IdeaProjects\\src\\main\\java\\visited.txt", true)));
            out.println(linenumber);
            out.close();
        } catch (IOException e) {
            //exception
        }

        System.out.println(linenumber);
    }
}
