import java.util.List;

public class BenchTests {

    public static long addInRandTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add((int) (Math.random() * (list.size())), i);
        }
        return System.nanoTime() - startTime;
    }

    public static long addInBeginTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(0, i);
        }
        return System.nanoTime() - startTime;
    }

    public static long addInMiddleTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(list.size() / 2, i);
        }
        return System.nanoTime() - startTime;
    }

    public static long addInEndTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(list.size() - 1, i);
        }
        return System.nanoTime() - startTime;
    }

    public static long iteratorTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (var it : list) {
        }
        return System.nanoTime() - startTime;
    }


    public static long getRandTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i--) {
            list.get((int) (Math.random() * (list.size())));
        }
        return System.nanoTime() - startTime;
    }

    public static long getFromBeginTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.get(i);
        }
        return System.nanoTime() - startTime;
    }

    public static long getFromMiddleTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = list.size() / 2; i < list.size() / 2 + count - 1; i++) {
            list.get(i);
        }
        return System.nanoTime() - startTime;
    }

    public static long getFromEndTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = list.size() - 1; i > list.size() - count; i--) {
            list.get(i);
        }
        return System.nanoTime() - startTime;
    }


    public static long removeRandTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.remove((int) (Math.random() * (list.size())));
        }
        return System.nanoTime() - startTime;
    }


    public static long removeFromBeginTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.remove(0);
        }
        return System.nanoTime() - startTime;
    }


    public static long removeFromMiddleTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.remove(list.size() / 2);
        }
        return System.nanoTime() - startTime;
    }


    public static long removeFromEndTimeTest(List<Object> list, int count) {
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.remove(list.size() - 1);
        }
        return System.nanoTime() - startTime;
    }

}
