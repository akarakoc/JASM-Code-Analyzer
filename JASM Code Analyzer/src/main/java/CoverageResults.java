import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;



public class CoverageResults {
    public static void main(String[] args) throws IOException {
        InputStream in=TestData.class.getResourceAsStream("Calculator.class");
        File file = new File("C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\Calculator.java");
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
        List<List> totalList = new ArrayList<List>();
        List<String> methodList = new ArrayList<String>();
        for(MethodNode methods:classNode.methods) {
            CFGC cComplex = new CFGC();
            try {
                System.out.println(methods.name+"################ METHOD BEGIN ################");
                List<Integer> coverageList = new ArrayList<Integer>();
                methodList.add(methods.name);
                coverageList = cComplex.getCFG(classNode.name,methods,listFileFinal);
                totalList.add(coverageList);
                System.out.println(methods.name+"################ METHOD END  #################");
            }
            catch(Exception e) {
            }
        }

        System.out.println("+-----------------+--------------+-----------------+---------------+--------------------------+-------------------------+");
        System.out.println("|  Visited Edges  |   All Edges  |  Visited Lines  |   All Lines   |       Edge Coverage      |      Line Coverage      |");
        System.out.println("+-----------------+--------------+-----------------+---------------+--------------------------+-------------------------+");
        Integer totvisitedEdge = 0;
        Integer totallEdges = 0;
        Integer totexistentLine = 0;
        Integer totfullLines = 0;
        Integer visitedMethods = 0;
        Integer totalMethods = 0;
        for(Integer i = 0; i < methodList.size(); i++){
            Integer visitedEdge = (Integer) totalList.get(i).get(0);
            Integer allEdges = (Integer) totalList.get(i).get(1);
            Integer existentLine = (Integer) totalList.get(i).get(2);
            Integer fullLines = (Integer) totalList.get(i).get(3);
            if( existentLine != 0){
                visitedMethods = visitedMethods + 1;
            }
            totalMethods = totalMethods + 1;
            totvisitedEdge = totvisitedEdge + visitedEdge;
            totallEdges = totallEdges + allEdges;
            totexistentLine = totexistentLine + existentLine;
            totfullLines = totfullLines + fullLines;
            System.out.printf("|      %03d        |      %03d     |        %03d      |      %03d      |      %10.2f%%         |    %10.2f%%          |   %s\n",visitedEdge,allEdges,existentLine,fullLines,(100 * Double.valueOf(visitedEdge)/allEdges),(100 * Double.valueOf(existentLine)/(fullLines)),methodList.get(i));
            System.out.println("+-----------------+--------------+-----------------+---------------+--------------------------+-------------------------+");
        }
        System.out.printf("|      %03d        |      %03d     |        %03d      |      %03d      |      %10.2f%%         |    %10.2f%%          |   %s\n",totvisitedEdge,totallEdges,totexistentLine+1,totfullLines-1,(100 * Double.valueOf(totvisitedEdge)/totallEdges),(100 * Double.valueOf(totexistentLine+1)/(totfullLines-1)),"Total");
        System.out.println("+-----------------+--------------+-----------------+---------------+--------------------------+-------------------------+");
        System.out.printf("   Visited Node: %10d \n",visitedMethods);
        System.out.printf("     Total Node: %10d \n",totalMethods-1);
        System.out.printf("Method Coverage:  %10.2f%% \n",(100 * Double.valueOf(visitedMethods)/(totalMethods-1)));
    }

}