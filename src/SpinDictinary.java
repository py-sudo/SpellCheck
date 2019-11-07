import java.util.*;

/*
 * Peiyi Guan
 * 215328917
 *
 * series
 * of
 * sorted
 * set
 * of
 * dictionaries
 *
 */

public class SpinDictinary implements SortedSetSeries<String> {
    // map obj ??
    private Map<Integer,SortedSet<String>> spinDictinaryMap;
    public SpinDictinary(){
        this.spinDictinaryMap = new HashMap<>();
    }

    public boolean add(int s,String e){
        SortedSet<String> set = new TreeSet<>();
        if(isEmpty(s)){
            set.add(e);
            this.spinDictinaryMap.put(s,set);
            return false;
        }
        this.spinDictinaryMap.get(s).add(e);
        return true;
    };

    public boolean remove(int s, String e){
        if(!isEmpty(s))
            this.spinDictinaryMap.get(s).remove(e);
        return false;
    };

    public boolean contains(int s, String e){
        if(!isEmpty(s))
            return this.spinDictinaryMap.get(s).contains(e);
        return false;
    };

    public int contains(String e){
        Set keyset =  this.spinDictinaryMap.keySet();
        Iterator it = keyset.iterator();
        while(it.hasNext()){
            int key = (int) it.next();
            if(this.spinDictinaryMap.get(key).contains(e))
                    return key;
        }
        return 0;
    };

    public int size(int s){
        if(!isEmpty(s))
            return this.spinDictinaryMap.get(s).size();
        return 0;
    };

    public boolean isEmpty(int s){
        SortedSet currentSet = this.spinDictinaryMap.get(s);
        if(currentSet != null){
            return  false;
        }
        return true;
    };

    public SortedSet<String> subSet(int s, String from, String to){
        SortedSet<String> currentSet = this.spinDictinaryMap.get(s);
        return currentSet.subSet(from,to);
    };

    public Iterator<String> iterator(int s){
        if(!isEmpty(s))
            return this.spinDictinaryMap.get(s).iterator();
        return Collections.emptyIterator();
    };
}
