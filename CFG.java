import java.io.*;
import java.util.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class CFG {
    public void getCFG(String owner, MethodNode mn, List fileList)  throws IOException, AnalyzerException {

        final InsnList instructions = mn.instructions;
        List<List> lineList = new ArrayList<>();
        List<List> cfgList = new ArrayList<>();
        System.out.println(mn.name);
        Stack lineStack = new Stack();
        lineStack.push(0);
        final HashMap<Integer, Integer> indexToLineMap = new HashMap<Integer, Integer>();
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
                }
                indexToLineMap.put(insn, (Integer) lineStack.peek());
            }
        };
        a.analyze(owner, mn);
        indexToLineMap.put(indexToLineMap.size(), indexToLineMap.get(indexToLineMap.size()-1));
        //System.out.println(indexToLineMap.toString());
        //System.out.println(indexToLineMap.toString());
        //System.out.println(lineList.toString());
        //System.out.println(cfgList.toString());

        for(Integer i=1; i < cfgList.size(); i++){
            if((Integer) indexToLineMap.get(cfgList.get(i).get(0)) != (Integer) indexToLineMap.get(cfgList.get(i).get(1))){
                String FirstItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(0)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))-1);
                String SecondItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(1)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))-1);
                System.out.println("\"" + FirstItem.replaceAll("\"", "\\\\\"") + "\" -> \""  + SecondItem.replaceAll("\"", "\\\\\"") + "\"");
                //System.out.println("\"" + cfgList.get(i).get(0)+ "\" -> \""  + cfgList.get(i).get(1) + "\"");
                //System.out.println("\"" + cfgList.get(i).get(0)+ "\" -> \"" + cfgList.get(i).get(1)+ "\"");
                //System.out.println("\"" + (Integer) indexToLineMap.get(cfgList.get(i).get(0))+ "\" -> \"" + (Integer) indexToLineMap.get(cfgList.get(i).get(1))+ "\"");
            }

        }
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
