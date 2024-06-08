package pro.osin.tools.impl;

import pro.osin.tools.common.Cache;
import pro.osin.tools.common.Frequency;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache<K, V extends Serializable> implements Cache<K, V>, Frequency<K> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Integer> frequency = new ConcurrentHashMap<>();
    private final int capacity;

    public MemoryCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        frequency.computeIfPresent(key, (k, v) -> v + 1);
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        frequency.put(key, 1);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void clear() {
        cache.clear();
        frequency.clear();
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        frequency.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return cache.containsValue(value);
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean isFull() {
        return cache.size() >= capacity;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getItemFrequency(K item) {
        return frequency.getOrDefault(item, 0);
    }

    @Override
    public K getTopFrequencyItem() {
        return frequency.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public K getLowFrequencyItem() {
        return frequency.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
