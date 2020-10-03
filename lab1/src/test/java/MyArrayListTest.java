import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyArrayListTest {

    MyArrayList <Integer> myArrayList;
    ArrayList <Integer> arrayList;

    @Before
    public void getTestExampleOfMyArrayList(){
        myArrayList = new MyArrayList<>(Arrays.asList(null, 2, 3, null, 4, null));
    }

    @Before
    public void getTestExampleOfArrayList (){
        arrayList = new ArrayList<>(Arrays.asList(null, 2, 3, null, 4, null));
    }

    @Test
    public void testToArray(){
        Assert.assertArrayEquals(myArrayList.toArray(), arrayList.toArray());
    }

    @Test
    public void testToArrayWithParam(){
        Assert.assertArrayEquals(myArrayList.toArray(new Integer[30]), arrayList.toArray(new Integer[30]));
    }

    @Test
    public void testContains() {
        Assert.assertEquals(arrayList.contains(3), myArrayList.contains(3));
        Assert.assertEquals(arrayList.contains(30), myArrayList.contains(30));
    }

    @Test
    public void testAdd() {
        arrayList.add(6);
        arrayList.add(7);

        myArrayList.add(6);
        myArrayList.add(7);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testGet() {
        for (int i = 0; i < myArrayList.size(); i++){
            Assert.assertEquals(arrayList.get(i), myArrayList.get(i));
        }
    }

    @Test
    public void testSet() {
        myArrayList.set(0, 5);
        myArrayList.set(3, 12);

        arrayList.set(0, 5);
        arrayList.set(3, 12);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testPosAdd() {
        arrayList.add(1, 6);
        arrayList.add(3, 7);

        myArrayList.add(1, 6);
        myArrayList.add(3,7);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testRemove() {
        arrayList.remove(0);
        arrayList.remove(3);

        myArrayList.remove(0);
        myArrayList.remove(3);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testListIterator() {
        var myIt = myArrayList.listIterator();
        var it = arrayList.listIterator();

        while (it.hasNext() || myIt.hasNext()){
            Assert.assertEquals(myIt.next(), it.next());
        }
        while (it.hasPrevious() || myIt.hasPrevious()){
            Assert.assertEquals(myIt.previous(), it.previous());
        }

        it.set(30);
        it.remove();
        it.add(50);
        it.add(50);

        myIt.set(30);
        myIt.remove();
        myIt.add(50);
        myIt.add(50);

        while (it.hasNext() || myIt.hasNext()){
            Assert.assertEquals(myIt.next(), it.next());
        }
        while (it.hasPrevious() || myIt.hasPrevious()){
            Assert.assertEquals(myIt.previous(), it.previous());
        }

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testRemoveAll() {

        List <Integer> removableList = Arrays.asList(2, 3, 50);

        myArrayList.removeAll(removableList);
        arrayList.removeAll(removableList);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testRetainAll(){
        List <Integer> removableList = Arrays.asList(2, 3, 50);

        myArrayList.removeAll(removableList);
        arrayList.removeAll(removableList);

        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }


    @Test
    public void testTestHashCode() {
        Assert.assertEquals(myArrayList.hashCode(), new MyArrayList<>(myArrayList).hashCode());
    }

    @Test
    public void testTestEquals() {
        List<Integer> newMyArrayList = new MyArrayList<>(myArrayList);
        Assert.assertEquals(myArrayList, newMyArrayList);
        newMyArrayList.add(0);
        Assert.assertNotEquals(myArrayList, newMyArrayList);
    }

    @Test
    public void testSubList(){
        Assert.assertArrayEquals(myArrayList.subList(1, 4).toArray(), arrayList.subList(1, 4).toArray());
        Assert.assertArrayEquals(myArrayList.subList(0, 4).toArray(), arrayList.subList(0, 4).toArray());
    }

    @Test
    public void testLastIndexOf(){
        Assert.assertEquals(myArrayList.lastIndexOf(2), arrayList.lastIndexOf(2));
        Assert.assertEquals(myArrayList.lastIndexOf(1), arrayList.lastIndexOf(1));
    }

    @Test
    public void testAddAll(){
        Collection<Integer> addColl = Arrays.asList(50, 90, 100);
        myArrayList.addAll(addColl);
        arrayList.addAll(addColl);
        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }

    @Test
    public void testAddAllByIndex(){
        Collection<Integer> addColl = Arrays.asList(50, 90, 100);
        myArrayList.addAll(0, addColl);
        arrayList.addAll(0, addColl);
        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());

        myArrayList.addAll(5, addColl);
        arrayList.addAll(5, addColl);
        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());


        myArrayList.addAll(myArrayList.size() - 1, addColl);
        arrayList.addAll(arrayList.size() - 1, addColl);
        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());

        myArrayList.addAll(myArrayList.size(), addColl);
        arrayList.addAll(arrayList.size(), addColl);
        Assert.assertArrayEquals(arrayList.toArray(), myArrayList.toArray());
    }
}