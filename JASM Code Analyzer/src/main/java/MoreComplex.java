public class MoreComplex {
    public void MoreComplex() {
        int x = 5;
        String str = "";
        for (int i = 0; i < 10; i++, x++) {
            if (x % 2 == 0) {
                str += "E";
            } else {
                str += "O";
            }

            int y = 0;
            while (y < 5) {
                str += y;
                int z = 0;
                while (z < 10) {
                    z *= z;
                    str += z;
                }
            }
        }
    }
}
