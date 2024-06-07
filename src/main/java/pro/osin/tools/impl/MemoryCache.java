package pro.osin.tools.impl;

import pro.osin.tools.common.Cache;
import pro.osin.tools.common.Frequency;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemoryCache<K, V> implements Cache<K, V>, Frequency<K> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Integer> frequency = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final int capacity;

    public MemoryCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        try {
            lock.readLock().lock();
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.writeLock().lock();
            cache.put(key, value);
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.readLock().lock();
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.writeLock().lock();
            cache.clear();
            frequency.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        try {
            lock.writeLock().lock();
            cache.remove(key);
            frequency.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsKey(K key) {
        try {
            lock.readLock().lock();
            return cache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsValue(V value) {
        try {
            lock.readLock().lock();
            return cache.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            lock.readLock().lock();
            return cache.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isFull() {
        try {
            lock.readLock().lock();
            return cache.size() >= capacity;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getCapacity() {
        try {
            lock.readLock().lock();
            return capacity;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getItemFrequency(K item) {
        try {
            lock.readLock().lock();
            return frequency.getOrDefault(item, 0);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public K getTopFrequencyItem() {
        try {
            lock.readLock().lock();
            return frequency
                    .entrySet()
                    .stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public K getLowFrequencyItem() {
        try {
            lock.readLock().lock();
            return frequency
                    .entrySet()
                    .stream()
                    .min(Comparator.comparingInt(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }
}
