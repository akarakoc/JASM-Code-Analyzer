/*

 * Class which contains the method to analyze

 */
public class CalculatorOrg {



    public int add(int x, int y) {

        int result = x + y;

        return result;

    }



    public int sub(int x, int y) {

        int result = x - y;

        return result;

    }



    public int mult(int x, int y) {

        int result = x * y;

        return result;

    }



    public int div(int x, int y) {

        int result = x / y;

        return result;

    }



    public void other() {

        int x = 3;

        int y = 5;

        if (x < 10) {

            System.out.println("X is less than 10");

        } else {

            System.out.println("X is greater than 10");

        }

        if (x == 3) {

            x = 10;

        } else {

            x = 20;

        }



        if (y > x) {

            System.out.println(" y is greater than x");

        } else {

            System.out.println(" y is greater than y");

        }



    }



    public void uncovered() {

        int x = 0;

        for (int i = 1; i < 100; i++) {

            x++;

        }

        while (x > 0) {

            x--;

        }

    }


    public void multiIfElse() {

        int x = 3;

        if (x < 10) {

            System.out.println("X is less than 10");

        } else if (x == 10) {

            System.out.println("X is equal than 10");

        } else {

            System.out.println("X is greater than 10");

        }

    }



    public void deneme(int x){;

        if (x != 0){

            x++;

        }

        return;

    }

}