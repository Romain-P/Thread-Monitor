package fr.romain.assignment3.shared;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom array wrapper by Romain PILLOT
 *
 * When an element is removed, all other elements keep their indexes.
 * Done for GUI indexes synchronization
 *
 * @param <T>   Type of each element on this array
 */
public class ArrayWrapper<T> implements Collection<T> {
    private final T[] elems;
    private int size;

    public ArrayWrapper(int n) {
        elems = (T[]) new Object[n];
        size = 0;
        Arrays.fill(elems, null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (T elem: elems) {
            if (elem.equals(o))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int pos = 0;
            private int nextPosition = 0;

            @Override
            public boolean hasNext() {
                if (pos != nextPosition) return true;

                for (int i=pos; i < elems.length - 1; ++i)
                    if (elems[i] != null) {
                        nextPosition = i;
                        return true;
                    }
                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                pos = nextPosition;
                return elems[nextPosition];
            }
        };
    }

    @Override
    public Object[] toArray() {
        T[] cpy = (T[]) new Object[elems.length];

        for (int i=0; i < elems.length; ++i)
            cpy[i] = elems[i];

        return cpy;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        for (int i=0; i < elems.length; ++i)
            a[i] = (T1) elems[i];
        return a;
    }

    public int indexOf(Object o) {
        for (int i=0; i < elems.length; ++i)
            if (elems[i] != null && elems[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public boolean add(T t) {
        if (size == elems.length) return false;

        for (int i=0; i < elems.length; ++i)
            if (elems[i] == null) {
                elems[i] = t;
                ++size;
                return true;
            }

        return false;
    }

    public int addIndexed(T t) {
        if (size == elems.length) return -1;

        for (int i=0; i < elems.length; ++i)
            if (elems[i] == null) {
                elems[i] = t;
                ++size;
                return i;
            }

        return -1;
    }

    @Override
    public boolean remove(Object o) {
        for (int i=0; i < elems.length; ++i)
            if (elems[i] != null && elems[i].equals(o)) {
                elems[i] = null;
                --size;
                return true;
            }
        return false;
    }

    public int removeIndexed(Object o) {
        for (int i=0; i < elems.length; ++i)
            if (elems[i] != null && elems[i].equals(o)) {
                elems[i] = null;
                --size;
                return i;
            }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c)
            if (contains(o))
                return true;
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.size() > elems.length - size) return false;
        for (T elem: c)
            add(elem);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o: c)
            remove(o);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (T elem: elems) {
            if (!c.contains(elem))
                remove(elem);
        }
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(elems, null);
    }
}
