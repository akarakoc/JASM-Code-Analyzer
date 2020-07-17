import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.*;
import java.util.*;

public class TestCoverage {
    public static void main(String[] args) throws IOException {
        InputStream in = TestData.class.getResourceAsStream("Calculator.class");
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
        for(MethodNode methods:classNode.methods) {
            System.out.println("-------BEGIN-------->"+ methods.name);
            Iterator<AbstractInsnNode> insnNodes = methods.instructions.iterator();
            while(insnNodes.hasNext()){
                AbstractInsnNode insn = insnNodes.next();
                //System.out.println(insnNodes.next().getOpcode());
                InsnList opList = new InsnList();
                System.out.println(insnToString(insn));
                if(insnToString(insn).trim().toLowerCase().contains("LINENUMBER".toLowerCase())){
                    opList.add(new LdcInsnNode(insnToString(insn).trim().toLowerCase().split("\\s+")[1]));
                    opList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "src/main/java/OpcodeExtractor", "opcodeReturn", "(Ljava/lang/String;)V"));
                    methods.instructions.insertBefore(insn, opList);
                }
            }
            System.out.println("-------END-------->"+ methods.name);
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        File outDir = new File("src/main/java");
        outDir.mkdirs();
        DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(outDir, "Calculator.class")));
        dout.write(cw.toByteArray());
        dout.flush();
        dout.close();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////INVOKE TEST FILE/////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("TEST PART BEGINS!");
        InputStream inTest = TestData.class.getResourceAsStream("CalculatorTest.class");
        ClassReader classReaderTest = new ClassReader(inTest);
        ClassNode classNodeTest = new ClassNode();
        classReaderTest.accept(classNodeTest, ClassReader.EXPAND_FRAMES);
        File testGen = new File("C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\testGeneration.java");
        testGen.createNewFile();
        FileWriter myWriter = new FileWriter("C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\testGeneration.java");
        myWriter.write("import java.io.*;\n\n");
        myWriter.write("public class testGeneration {\n");
        myWriter.write("\tpublic static void main(String[] args) throws IOException {\n");
        myWriter.write("\t\tCalculator testClass = new Calculator();\n");

        for(MethodNode methodsTest :classNodeTest.methods) {
            System.out.println("-------BEGIN-------->"+ methodsTest.name);
            final InsnList instructionsTest = methodsTest.instructions;
            Stack <String> variables = new Stack<String>();
            for(AbstractInsnNode opcodes: instructionsTest){
                if (insnToString(opcodes).trim().toLowerCase().contains("iconst".toLowerCase())) {
                    System.out.println(insnToString(opcodes).trim().split("_")[1]);
                    variables.push(insnToString(opcodes).trim().split("_")[1]);
                }
                if (insnToString(opcodes).trim().toLowerCase().contains("bipush".toLowerCase())) {
                    System.out.println(insnToString(opcodes).trim().split("\\s+")[1]);
                    variables.push(insnToString(opcodes).trim().split("\\s+")[1]);
                }
                if (insnToString(opcodes).trim().toLowerCase().contains("invokevirtual".toLowerCase())) {
                    String commandSet = insnToString(opcodes).trim().toString().split("\\s+")[1];
                   if(!variables.isEmpty()){
                       String var2 = variables.pop();
                       String var1 = variables.pop();
                       myWriter.write("\t\ttestClass."+commandSet.split("\\.")[1]+"("+var1+","+var2+");\n");
                       System.out.println(commandSet.split("\\.")[0]+ " testClass = new "+ commandSet.split("\\.")[0]+"();");
                       System.out.println("testClass."+commandSet.split("\\.")[1]+"("+var1+","+var2+");");
                   }else{
                       myWriter.write("\t\ttestClass."+commandSet.split("\\.")[1]+"("+");\n");
                       System.out.println(commandSet.split("\\.")[0]+ " testClass = new "+ commandSet.split("\\.")[0]+"();");
                       System.out.println("testClass."+commandSet.split("\\.")[1]+"("+");");
                   }

                }
            }
            System.out.println("-------END-------->"+ methodsTest.name);
        }
        myWriter.write("\t}\n");
        myWriter.write("}\n");
        myWriter.close();
        try {
            //runProcess("javac -cp src C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\testGeneration.java C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\Calculator.java C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\src\\main\\java\\OpcodeExtractor.java");
            //runProcess("\"C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\jbr\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\lib\\idea_rt.jar=55862:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\ALI\\IdeaProjects\\SWE550-HW1\\target\\classes;C:\\Users\\ALI\\.m2\\repository\\org\\ow2\\asm\\asm\\7.3.1\\asm-7.3.1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\ow2\\asm\\asm-tree\\7.3.1\\asm-tree-7.3.1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\ow2\\asm\\asm-util\\7.3.1\\asm-util-7.3.1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\ow2\\asm\\asm-analysis\\7.3.1\\asm-analysis-7.3.1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\junit\\jupiter\\junit-jupiter-api\\5.7.0-M1\\junit-jupiter-api-5.7.0-M1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\apiguardian\\apiguardian-api\\1.1.0\\apiguardian-api-1.1.0.jar;C:\\Users\\ALI\\.m2\\repository\\org\\opentest4j\\opentest4j\\1.2.0\\opentest4j-1.2.0.jar;C:\\Users\\ALI\\.m2\\repository\\org\\junit\\platform\\junit-platform-commons\\1.7.0-M1\\junit-platform-commons-1.7.0-M1.jar;C:\\Users\\ALI\\.m2\\repository\\org\\testng\\testng\\7.1.0\\testng-7.1.0.jar;C:\\Users\\ALI\\.m2\\repository\\com\\beust\\jcommander\\1.72\\jcommander-1.72.jar;C:\\Users\\ALI\\.m2\\repository\\com\\google\\inject\\guice\\4.1.0\\guice-4.1.0-no_aop.jar;C:\\Users\\ALI\\.m2\\repository\\javax\\inject\\javax.inject\\1\\javax.inject-1.jar;C:\\Users\\ALI\\.m2\\repository\\aopalliance\\aopalliance\\1.0\\aopalliance-1.0.jar;C:\\Users\\ALI\\.m2\\repository\\com\\google\\guava\\guava\\19.0\\guava-19.0.jar;C:\\Users\\ALI\\.m2\\repository\\org\\yaml\\snakeyaml\\1.21\\snakeyaml-1.21.jar testGeneration");

        } catch (Exception e) {
            e.printStackTrace();
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

    private static void runProcess(String command) throws Exception {
        try {
            Process pro = Runtime.getRuntime().exec(command);
            pro.waitFor();
            System.out.println(command + " exitValue() " + pro.exitValue());
        }catch (IOException e) {
            //exception
        }
    }
}


