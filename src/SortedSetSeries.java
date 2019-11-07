import java.util.Iterator;
import java.util.SortedSet;

public interface SortedSetSeries<E> {
    public boolean add(int s, E e);

    public boolean remove(int s, E e);

    public boolean contains(int s, E e);

    public int contains(E e);

    public int size(int s);

    public boolean isEmpty(int s);

    public SortedSet<E> subSet(int s, E from, E to);

    public Iterator<E> iterator(int s);
}
