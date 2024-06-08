package pro.osin.tools.common;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    int size();

    void clear();

    void remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    boolean isEmpty();

    boolean isFull();

    int getCapacity();

}
