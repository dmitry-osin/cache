package pro.osin.tools.impl;

import pro.osin.tools.common.Cache;

public class StandardCache<K, V> implements Cache<K, V>{

    public static final String DEFAULT_CACHE_FOLDER = "cache";
    public static final int DEFAULT_MEMORY_CACHE_CAPACITY = 100;
    public static final int DEFAULT_DISK_CACHE_CAPACITY = 1000;

    private final MemoryCache<K, V> memoryCache = new MemoryCache<>(DEFAULT_MEMORY_CACHE_CAPACITY);
    private final DiskCache<K, V> diskCache = new DiskCache<>(DEFAULT_CACHE_FOLDER, DEFAULT_DISK_CACHE_CAPACITY);

    public StandardCache() {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getCapacity() {
        return 0;
    }
}
