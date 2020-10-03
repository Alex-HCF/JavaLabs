import java.util.*;

public class MyArrayList<T> implements List<T> {

    private Object[] array;
    private int size = 0;


    public MyArrayList() {
        array = new Object[10];
    }

    public MyArrayList(T[] othArray) {
        array = Arrays.copyOf(othArray, othArray.length);
        size = array.length;
    }

    public MyArrayList(List<T> list) {
        size = list.size();
        array = new Object[size];

        Collections.copy(this, list);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null && array[i] == null || array[i] != null && array[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ListIteratorImpl iterator() {
        return new ListIteratorImpl();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public boolean add(Object o) {
        if (array.length == size) {
            extendArray();
        }
        array[size++] = o;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);

        if (index == -1)
            return false;

        remove(index);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index != size) {
            validationIndex(index);
        }
        if ((c.size() + size()) >= array.length) {
            extendArray(c.size());
        }

        System.arraycopy(array, index, array, index + c.size(), size() - index);
        System.arraycopy(c.toArray(), 0, array, index, c.size());

        size += c.size();

        return true;
    }

    @Override
    public void clear() {
        size = 0;
        array = null;
    }

    @Override
    public T get(int index) {
        validationIndex(index);

        return (T) array[index];
    }

    @Override
    public T set(int index, T element) {
        T oldValue = get(index);
        array[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, Object element) {
        validationIndex(index);

        if (size == array.length) {
            extendArray();
        }

        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        validationIndex(index);
        Object removedValue = array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        size--;
        return (T) removedValue;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null)
                    return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (array[i] == null)
                    return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (array[i] != null && array[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIteratorImpl();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIteratorImpl(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (!(fromIndex >= 0 && toIndex > fromIndex && size > toIndex)) {
            throw new ArrayIndexOutOfBoundsException("fromIndex: " + fromIndex + " toIndex: " + toIndex);
        }
        int subListLen = toIndex - fromIndex;
        Object[] subList = new Object[subListLen];
        System.arraycopy(array, fromIndex, subList, 0, subListLen);

        return new MyArrayList<>((T[]) subList);
    }

    @Override
    public boolean retainAll(Collection c) {
        int countRemovedVal = 0;
        int indexToCopyNextVal = 0;

        for (int i = 0; i < size; i++) {
            array[indexToCopyNextVal] = array[i];
            if (!c.contains(get(i))) {
                indexToCopyNextVal--;
                countRemovedVal++;
            }
            indexToCopyNextVal++;
        }

        size -= countRemovedVal;

        return countRemovedVal != 0;
    }

    @Override
    public boolean removeAll(Collection c) {

        int countRemovedVal = 0;
        int indexToCopyNextVal = 0;
        for (int i = 0; i < size; i++) {
            array[indexToCopyNextVal] = array[i];
            if (c.contains(get(i))) {
                indexToCopyNextVal--;
                countRemovedVal++;
            }
            indexToCopyNextVal++;
        }

        size -= countRemovedVal;

        return countRemovedVal != 0;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object obj : c) {
            if (!contains(obj))
                return false;
        }
        return false;
    }

    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            return (E[]) Arrays.copyOf(array, size);
        } else {
            System.arraycopy(array, 0, a, 0, size);
            return a;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    @Override
    public int hashCode() {
        int hashCode = 0;

        for (int i = 0; i < size; i++) {
            hashCode = 31 * hashCode + (array[i] == null ? 0 : array[i].hashCode());
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof List) || ((List<?>) obj).size() != size()) {
            return false;
        }

        int i = 0;
        for (Object o : (List<?>) obj) {
            if (o == null && array[i] != null || o != null && array == null || o != null && !o.equals(array[i]))
                return false;
            i++;
        }
        return true;
    }

    private void extendArray() {
        extendArray(array.length);
    }

    private void extendArray(int countAddCells) {
        Object[] oldArray = array;
        array = new Object[oldArray.length + countAddCells];

        System.arraycopy(oldArray, 0, array, 0, size);
    }

    private void validationIndex(int index) {
        if (!(index >= 0 && index < size))
            throw new IndexOutOfBoundsException("index: " + index);
    }

    private class ListIteratorImpl implements ListIterator<T> {

        int cursor = 0;
        int indexOfLastReturnedVal = -1;

        public ListIteratorImpl() {
        }

        public ListIteratorImpl(int index) {
            validationIndex(index);
            cursor = index;
        }


        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Index: " + (cursor + 1));
            }
            indexOfLastReturnedVal = cursor;
            return get(cursor++);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public T previous() {
            if (cursor - 1 < 0) {
                throw new NoSuchElementException("Index: " + (cursor - 1));
            }
            cursor--;
            indexOfLastReturnedVal = cursor;
            return get(cursor);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (indexOfLastReturnedVal < 0) {
                throw new IllegalStateException();
            }
            MyArrayList.this.remove(indexOfLastReturnedVal);
            cursor = indexOfLastReturnedVal;
            indexOfLastReturnedVal = -1;
        }

        @Override
        public void set(T t) {
            if (indexOfLastReturnedVal < 0) {
                throw new IllegalStateException();
            }
            MyArrayList.this.set(indexOfLastReturnedVal, t);
        }

        @Override
        public void add(T t) {
            MyArrayList.this.add(cursor++, t);
            indexOfLastReturnedVal = -1;
        }
    }

}
