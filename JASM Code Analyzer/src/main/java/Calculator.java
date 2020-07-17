import src.main.java.OpcodeExtractor;

public class Calculator {
   public Calculator() {
      OpcodeExtractor.opcodeReturn("6");
      super();
   }

   public int add(int x, int y) {
      OpcodeExtractor.opcodeReturn("12");
      int result = x + y;
      OpcodeExtractor.opcodeReturn("14");
      return result;
   }

   public int sub(int x, int y) {
      OpcodeExtractor.opcodeReturn("22");
      int result = x - y;
      OpcodeExtractor.opcodeReturn("24");
      return result;
   }

   public int mult(int x, int y) {
      OpcodeExtractor.opcodeReturn("32");
      int result = x * y;
      OpcodeExtractor.opcodeReturn("34");
      return result;
   }

   public int div(int x, int y) {
      OpcodeExtractor.opcodeReturn("42");
      int result = x / y;
      OpcodeExtractor.opcodeReturn("44");
      return result;
   }

   public void other() {
      OpcodeExtractor.opcodeReturn("52");
      int x = 3;
      OpcodeExtractor.opcodeReturn("54");
      int y = 5;
      OpcodeExtractor.opcodeReturn("56");
      if (x < 10) {
         OpcodeExtractor.opcodeReturn("58");
         System.out.println("X is less than 10");
      } else {
         OpcodeExtractor.opcodeReturn("62");
         System.out.println("X is greater than 10");
      }

      OpcodeExtractor.opcodeReturn("66");
      if (x == 3) {
         OpcodeExtractor.opcodeReturn("68");
         x = 10;
      } else {
         OpcodeExtractor.opcodeReturn("72");
         x = 20;
      }

      OpcodeExtractor.opcodeReturn("78");
      if (y > x) {
         OpcodeExtractor.opcodeReturn("80");
         System.out.println(" y is greater than x");
      } else {
         OpcodeExtractor.opcodeReturn("84");
         System.out.println(" y is greater than y");
      }

      OpcodeExtractor.opcodeReturn("90");
   }

   public void uncovered() {
      OpcodeExtractor.opcodeReturn("96");
      int x = 0;
      OpcodeExtractor.opcodeReturn("98");

      for(int i = 1; i < 100; ++i) {
         OpcodeExtractor.opcodeReturn("100");
         ++x;
         OpcodeExtractor.opcodeReturn("98");
      }

      while(true) {
         OpcodeExtractor.opcodeReturn("104");
         if (x <= 0) {
            OpcodeExtractor.opcodeReturn("110");
            return;
         }

         OpcodeExtractor.opcodeReturn("106");
         --x;
      }
   }

   public void multiIfElse() {
      OpcodeExtractor.opcodeReturn("115");
      int x = 3;
      OpcodeExtractor.opcodeReturn("117");
      if (x < 10) {
         OpcodeExtractor.opcodeReturn("119");
         System.out.println("X is less than 10");
      } else {
         OpcodeExtractor.opcodeReturn("121");
         if (x == 10) {
            OpcodeExtractor.opcodeReturn("123");
            System.out.println("X is equal than 10");
         } else {
            OpcodeExtractor.opcodeReturn("127");
            System.out.println("X is greater than 10");
         }
      }

      OpcodeExtractor.opcodeReturn("131");
   }

   public void deneme(int x) {
      OpcodeExtractor.opcodeReturn("137");
      if (x != 0) {
         OpcodeExtractor.opcodeReturn("139");
         ++x;
      }

      OpcodeExtractor.opcodeReturn("143");
   }
}
