import java.io.*;
import java.util.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class CFG {
    public void getCFG(String owner, MethodNode mn, List fileList)  throws IOException, AnalyzerException {

        final InsnList instructions = mn.instructions;
        List<List> cfgList = new ArrayList<>();
        System.out.println(mn.name);
        Stack lineStack = new Stack();
        lineStack.push(0);
        final HashMap<Integer, Integer> indexToLineMap = new HashMap<Integer, Integer>();
        final HashMap<Integer, String> localVarMap = new HashMap<>();
        final HashMap<AbstractInsnNode,Integer> nodeIndexHashMap = new HashMap<AbstractInsnNode, Integer>();
        final HashMap<AbstractInsnNode,List<AbstractInsnNode>> predecessorNodeMap = new HashMap<AbstractInsnNode, List<AbstractInsnNode>>();
        final List<AbstractInsnNode> endNode = new ArrayList<AbstractInsnNode>();
        for (LocalVariableNode localVarNode : mn.localVariables){
            if (localVarNode.index == 0){
                continue;
            }else{
                localVarMap.put(localVarNode.index,localVarNode.name);
            }
        }
        //System.out.println(localVarMap);
        Analyzer a = new Analyzer(new BasicInterpreter()){
            @Override
            protected void newControlFlowEdge(int insn, int successor) {
                AbstractInsnNode source = instructions.get(insn);
                AbstractInsnNode destination = instructions.get(successor);
                nodeIndexHashMap.put(source,insn); //Node->indexMap
                nodeIndexHashMap.put(destination,successor); //Node->indexMap
                //System.out.println(source+"->"+destination.getPrevious());
                if(predecessorNodeMap.containsKey(destination)){
                    predecessorNodeMap.get(destination).add(source);
                }else{
                    List<AbstractInsnNode> predList = new ArrayList<AbstractInsnNode>();
                    predList.add(source);
                    predecessorNodeMap.put(destination,predList);
                }

                if(insnToString(destination).trim().toLowerCase().contains("return")){
                    endNode.add(destination);
                }

                List<Integer> flowPair = new ArrayList<>();
                flowPair.add(insn);
                flowPair.add(successor);
                cfgList.add(flowPair);
                //System.out.println("prev:"+source.getPrevious());
                if(insnToString(destination).trim().toLowerCase().contains("LINENUMBER".toLowerCase())){
                    lineStack.push(Integer.valueOf(insnToString(destination).trim().split("\\s+")[1]));
                }
                indexToLineMap.put(insn, (Integer) lineStack.peek());
            }
        };
        a.analyze(owner, mn);
        indexToLineMap.put(indexToLineMap.size(), indexToLineMap.get(indexToLineMap.size()-1));
        HashMap<Integer,myNode> lineNodes = new HashMap<Integer, myNode>();
        for (Integer i=0; i < fileList.size(); i++){
            List<String> defVar = new ArrayList<String>();
            List<String> useVar = new ArrayList<String>();
            List<String> inList = new ArrayList<String>();
            List<String> outList = new ArrayList<String>();
            myNode newLine = new myNode(defVar,useVar,inList,outList);
            lineNodes.put(i,newLine);
        }

        /////////////////////////////////////////WORKLIST ALGORITHM//////////////////////////////////////////
        AbstractInsnNode exitNode = endNode.get(0);
        Stack<AbstractInsnNode> workList = new Stack<AbstractInsnNode>();
        workList.push(exitNode);
        while(workList.size() != 0){
            AbstractInsnNode currentNode = workList.pop();

            List<String> oList = lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).outList;
            List<String> iList = lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList;

            for (String str: oList){
                if (!iList.contains(str)){
                    if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.contains(str)){
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList.add(str);
                    }
                }
            }

            // || insnToString(currentNode).trim().toLowerCase().contains("iinc".toLowerCase())

            if(insnToString(currentNode).trim().toLowerCase().contains("iload".toLowerCase()) || insnToString(currentNode).trim().toLowerCase().contains("istore".toLowerCase()) || insnToString(currentNode).trim().toLowerCase().contains("iinc".toLowerCase())){
                String varName = localVarMap.get(Integer.valueOf(insnToString(currentNode).trim().toLowerCase().split("\\s+")[1]));
                if(insnToString(currentNode).trim().toLowerCase().contains("iload".toLowerCase())){
                    if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).useVar.contains(varName)){
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).useVar.add(varName);
                        if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.contains(varName)){
                          if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList.contains(varName)){
                               lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList.add(varName);
                          }
                        }
                    }
                }else if(insnToString(currentNode).trim().toLowerCase().contains("istore".toLowerCase())){
                    if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.contains(varName)){
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.add(varName);
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList.remove(varName);
                    }
                }else if(insnToString(currentNode).trim().toLowerCase().contains("iinc".toLowerCase())){
                    if(!lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.contains(varName)){
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).useVar.add(varName);
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).defVar.add(varName);
                        lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(currentNode))).inList.remove(varName);
                    }
                }
            }

            if(predecessorNodeMap.get(currentNode) != null){
                List<AbstractInsnNode> prevNodes = predecessorNodeMap.get(currentNode);
                for (AbstractInsnNode n:prevNodes){
                    workList.push(n);
                    List<String> outPreList = lineNodes.get(indexToLineMap.get(nodeIndexHashMap.get(n))).outList;
                    for (String str: iList){
                        if (outPreList.contains(str)){
                            continue;
                        }else{
                            if(indexToLineMap.get(nodeIndexHashMap.get(currentNode)) != indexToLineMap.get(nodeIndexHashMap.get(n))){
                                outPreList.add(str);
                            }
                        }
                    }
                }
                prevNodes.clear();
            }

        }

        String initialD ="";
        if(!lineNodes.get((Integer) indexToLineMap.get(cfgList.get(1).get(0))).inList.isEmpty()){
            //initialD = "\\nDEAD";
        }
        List<Integer> indexList = new ArrayList<Integer>();
        for(Integer i=1; i < cfgList.size(); i++){
            if((Integer) indexToLineMap.get(cfgList.get(i).get(0)) != (Integer) indexToLineMap.get(cfgList.get(i).get(1))){
                indexList.add((Integer) indexToLineMap.get(cfgList.get(i).get(0)));
                Integer minIndex = Collections.min(indexList);
                String firstD ="";
                String secondD ="";
                String initialSecondD="";

                if ((Integer) indexToLineMap.get(cfgList.get(i).get(0)) != minIndex){
                    initialD="";
                }

                if(!lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).defVar.isEmpty()){
                    if(!lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).outList.contains(lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).defVar.get(0))){
                        firstD = "\\nDEAD";
                    }
                }
                if(!lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).defVar.isEmpty()){
                    if(!lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).outList.contains(lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).defVar.get(0))){
                        secondD = "\\nDEAD";
                    }
                }


                String firstUse = "\\n use:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).useVar;
                String firstDef = "\\n def:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).defVar;
                String firstIn = "\\n in:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).inList;
                String firstOut = "\\n out:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))).outList;
                String secondUse = "\\n use:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).useVar;
                String secondDef = "\\n def:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).defVar;
                String secondIn = "\\n in:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).inList;
                String secondOut = "\\n out:" + lineNodes.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))).outList;
                String FirstItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(0)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(0))-1)+firstIn+firstUse+firstDef+firstOut+initialD+firstD;
                String SecondItem = "#" + (Integer) indexToLineMap.get(cfgList.get(i).get(1)) + " " +(String) fileList.get((Integer) indexToLineMap.get(cfgList.get(i).get(1))-1)+secondIn+secondUse+secondDef+secondOut+initialSecondD+secondD;
                System.out.println("\"" + FirstItem.replaceAll("\"", "\\\\\"") + "\" -> \""  + SecondItem.replaceAll("\"", "\\\\\"") + "\"");


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

    static class myNode {
        List<String> defVar;
        List<String> useVar;
        List<String> inList;
        List<String> outList;

        myNode(List<String> dVar,List<String> uVar,List<String> ilist,List<String> olist)
        {
            defVar = dVar;
            useVar = uVar;
            inList = ilist;
            outList = olist;
        }
    }
}

