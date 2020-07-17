
/*
 * Class which contains the method to analyze
 */
public class TestDataDFG {

    public void test1(){
        int x = 3;
        int y = 5;

        if(x <= 2){
            if(y >= 4){
                System.out.println("x <=2 and y >= 4");
            }else{
                System.out.println("x <= 2 and y < 4");
            }
        }else{
            if(y >= 4){
                System.out.println("x > 2 and y >= 4");
            }else{
                System.out.println("x < 2 and y < 4");
            }
        }
    }



    public void test2(){
        int x = 4;
        int y = 4;

        if(x < y){
            System.out.println("x < y");
        }else
        if(y < x){
            System.out.println("y < x");
        }else{
            System.out.println("x = y");
        }
    }



    public void test3(){
        int x = 5;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }

    public void test4(){
        int x = 4;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }

    public void test5(){
        int x = 3;
        int y = 5;

        while(x < y){
            System.out.println("x < y");
            ++x;
        }
    }





    public void IfElse(int x) {
        if (x < 3) {
            x = 6;
        } else {
            x = 7;
        }
    }

    public int IfElseIf(int y) {

        int x = 1;

        if (x == y) {
            y = 6;
        } else
        if (x == 8) {
            System.out.println("x == 7");
        } else {
            y = 1;
        }

        return x;
    }

    public int ManyIfElse(int y) {

        int x = 1;

        if (x == y) {
            y = 6;
        } else
        if (x == 8) {
            y = 7;
        } else
        if (x == 6) {
            y = 1;
        } else
        if (x == 5) {
            y = 4;
        }

        return x;
    }

    public void ForTest(){

        int x = 3;

        for(int i = 0;
            i < x;
            i++){

            System.out.println("i");

        }

    }

    public void Complex() {
        int x= 2;
        int y= 3;

        for(int a = 1 , b = 3; a < 4 && b < 4; a++, b++) {
            x++;
            y++;
        }

        while(x > 0 || y < 0) {
            x--;
        }
    }

    public void DeadCode() {
        int x = 3;
        int y = 1;
        int z = 0;

        if(x > 3) {
            y = 2;
            z = 4;
        } else {
            z = 5;
        }
        x = z;
        System.out.println(x);
    }

    public int doWhile(){
        int b = 0;
        int c = 0;
        int a = 0;
        do  {
            b = a + 1;
            c = c + b;
            a = b * 2;
        }while(a < 9);
        return c;
    }

}
