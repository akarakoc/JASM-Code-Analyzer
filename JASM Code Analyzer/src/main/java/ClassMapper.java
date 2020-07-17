import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;



public class ClassMapper {
    public static void main(String[] args) throws IOException {
        InputStream in=TestData.class.getResourceAsStream("TestDataDFG.class");
        File file = new File("C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\TestDataDFG.java");
        Scanner input = new Scanner(file);
        List<String> listFile = new ArrayList<String>();
        while (input.hasNextLine()) {
            listFile.add(input.nextLine());
        }
        List<String> listFileFinal = new ArrayList<String>();
        for(int i=0; i < listFile.size(); i++){
            listFileFinal.add(listFile.get(i).trim());
            // System.out.println(listFile.get(i));
        }
        //System.out.println(listFileFinal.toString());
        ClassReader classReader = new ClassReader(in);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        for(MethodNode methods:classNode.methods) {
            CFG cComplex = new CFG();
            try {
                System.out.println(methods.name+"################ METHOD BEGIN ################");
                cComplex.getCFG(classNode.name,methods,listFileFinal);
                System.out.println(methods.name+"################ METHOD END  #################");
            }
            catch(Exception e) {
            }
        }
    }

}
