import java.io.*;

public class testGeneration {
	public static void main(String[] args) throws IOException {
		Calculator testClass = new Calculator();
		testClass.sub(3,2);
		testClass.div(8,2);
		testClass.mult(5,2);
		testClass.other();
		testClass.multiIfElse();
	}
}
