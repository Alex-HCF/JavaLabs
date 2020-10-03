import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        try {
            myArrayList.add(1);
            myArrayList.add(2);
            myArrayList.add(3);
        } catch (Exception e) {
            System.out.println("Duck:" + e.toString());
        }

        System.out.println(myArrayList.toString());

    }
}
