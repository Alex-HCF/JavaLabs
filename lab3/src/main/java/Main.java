import dnl.utils.text.table.TextTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static TextTable showTestGroup(int count) {


        String[] collums = {"", "Add 30% in the Begin", "Add 30% in the Middle", "Add 30% in the End",
                "Get 30% from the Begin ", "Get 30% from the Middle", "Get 30% from the End", "Iterator",
                "Remove 30% from the Begin", "Remove 30% from the Middle", "Remove 30% from the End"};
        Object[][] elements = new Object[2][11];
        elements[0][0] = "ArrayList";
        elements[1][0] = "LinkedList";

        List<Object> arrayList = new ArrayList<>();
        List<Object> linkedList = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        int countForTest = count / 3;

        // Add
        elements[0][1] = BenchTests.addInBeginTimeTest(arrayList, countForTest);
        elements[1][1] = BenchTests.addInBeginTimeTest(linkedList, countForTest);
        arrayList = arrayList.subList(0, count);
        linkedList = linkedList.subList(0, count);

        elements[0][2] = BenchTests.addInMiddleTimeTest(arrayList, countForTest);
        elements[1][2] = BenchTests.addInMiddleTimeTest(linkedList, countForTest);
        arrayList = arrayList.subList(0, count);
        linkedList = linkedList.subList(0, count);

        elements[0][3] = BenchTests.addInEndTimeTest(arrayList, countForTest);
        elements[1][3] = BenchTests.addInEndTimeTest(linkedList, countForTest);
        arrayList = arrayList.subList(0, count);
        linkedList = linkedList.subList(0, count);

        // Get
        elements[0][4] = BenchTests.getFromBeginTimeTest(arrayList, countForTest);
        elements[1][4] = BenchTests.getFromBeginTimeTest(linkedList, countForTest);

        elements[0][5] = BenchTests.getFromMiddleTimeTest(arrayList, countForTest);
        elements[1][5] = BenchTests.getFromMiddleTimeTest(linkedList, countForTest);

        elements[0][6] = BenchTests.getFromEndTimeTest(arrayList, countForTest);
        elements[1][6] = BenchTests.getFromEndTimeTest(linkedList, countForTest);

        // Iterator
        elements[0][7] = BenchTests.iteratorTimeTest(arrayList, countForTest);
        elements[1][7] = BenchTests.iteratorTimeTest(linkedList, countForTest);


        // Delete
        elements[0][8] = BenchTests.removeFromBeginTimeTest(arrayList, countForTest);
        elements[1][8] = BenchTests.removeFromBeginTimeTest(linkedList, countForTest);

        elements[0][9] = BenchTests.removeFromMiddleTimeTest(arrayList, countForTest);
        elements[1][9] = BenchTests.removeFromMiddleTimeTest(linkedList, countForTest);

        elements[0][10] = BenchTests.removeFromEndTimeTest(arrayList, countForTest);
        elements[1][10] = BenchTests.removeFromEndTimeTest(linkedList, countForTest);

        return new TextTable(collums, elements);
    }

    public static void main(String[] args) {
        System.out.println("Execution time in nanoseconds ");

        System.out.println("\nFor 10^3:");
        showTestGroup(1000).printTable();

        System.out.println("\nFor 10^4:");
        showTestGroup(10000).printTable();

        System.out.println("\nFor 10^5:");
        showTestGroup(100000).printTable();

    }
}
