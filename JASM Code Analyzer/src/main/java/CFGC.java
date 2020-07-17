import java.io.*;
import java.util.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class CFGC {
    public List<Integer> getCFG(String owner, MethodNode mn, List fileList)  throws IOException, AnalyzerException {

        final InsnList instructions = mn.instructions;
        List<List> lineList = new ArrayList<>();
        List<List> cfgList = new ArrayList<>();
        System.out.println(mn.name);
        Stack lineStack = new Stack();
        lineStack.push(0);
        final HashMap<Integer, Integer> indexToLineMap = new HashMap<Integer, Integer>();
        List<String>  coverageFullLines = new ArrayList<>();
        Analyzer a = new Analyzer(new BasicInterpreter()){
            @Override
            protected void newControlFlowEdge(int insn, int successor) {
                AbstractInsnNode source = instructions.get(insn);
                AbstractInsnNode destination = instructions.get(successor);
                List<Integer> flowPair = new ArrayList<>();
                flowPair.add(insn);
                flowPair.add(successor);
                cfgList.add(flowPair);

                if(insnToString(destination).trim().toLowerCase().contains("LINENUMBER".toLowerCase())){
                    lineStack.push(Integer.valueOf(insnToString(destination).trim().split("\\s+")[1]));
                    coverageFullLines.add(insnToString(destination).trim().split("\\s+")[1]);
                }
                indexToLineMap.put(insn, (Integer) lineStack.peek());
            }
        };
        a.analyze(owner, mn);
        indexToLineMap.put(indexToLineMap.size(), indexToLineMap.get(indexToLineMap.size()-1));

        File fileVisited = new File("C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\visited.txt");
        Scanner inputVisited = new Scanner(fileVisited);
        List<String> listFileVisited = new ArrayList<String>();
        while (inputVisited.hasNextLine()) {
            String temp = inputVisited.nextLine();
            if(!listFileVisited.contains(temp)){
                listFileVisited.add(temp);
            }
        }
        Integer existentLine = 0;
        for(String str: listFileVisited){
            if (coverageFullLines.contains(str)){
                existentLine++;
            }
        }

        Integer notVisitedEdges = 0;
        Integer allEdges = 0;
        for(Integer i=1; i < cfgList.size(); i++){
            if((Integer) indexToLineMap.get(cfgList.get(i).get(0)) != (Integer) indexToLineMap.get(cfgList.get(i).get(1))){
                String FirstItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(0)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))-1);
                String SecondItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(1)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))-1);
                if(!listFileVisited.contains(indexToLineMap.get(cfgList.get(i).get(0)).toString())){
                    FirstItem = "* NOT VISITED * "+ FirstItem;
                }else{
                    FirstItem = "* VISITED * "+ FirstItem;
                }
                if(!listFileVisited.contains(indexToLineMap.get(cfgList.get(i).get(1)).toString())){
                    SecondItem = "* NOT VISITED * "+ SecondItem;
                }else{
                    SecondItem = "* VISITED * "+ SecondItem;
                }
                System.out.println("\"" + FirstItem.replaceAll("\"", "\\\\\"") + "\" -> \""  + SecondItem.replaceAll("\"", "\\\\\"") + "\"");

                if(!listFileVisited.contains(indexToLineMap.get(cfgList.get(i).get(0)).toString()) || !listFileVisited.contains(indexToLineMap.get(cfgList.get(i).get(1)).toString())){
                    notVisitedEdges = notVisitedEdges + 1;
                }
                allEdges = allEdges +1;
            }

        }

        System.out.println(notVisitedEdges);
        System.out.println(allEdges);
        System.out.printf("Line Coverage is: %.2f%%\n",(100 * Double.valueOf((allEdges-notVisitedEdges))/allEdges));
        System.out.printf("Node Coverage is: %.2f%%\n",(100 * Double.valueOf(existentLine)/coverageFullLines.size()));
        List<Integer> returnList = new ArrayList<Integer>();
        returnList.add(allEdges-notVisitedEdges);
        returnList.add(allEdges);
        returnList.add(existentLine);
        returnList.add(coverageFullLines.size());
        return returnList;
    }

    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);

}
