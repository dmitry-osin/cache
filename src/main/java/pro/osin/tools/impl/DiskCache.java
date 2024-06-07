package pro.osin.tools.impl;

import pro.osin.tools.common.Cache;
import pro.osin.tools.common.Frequency;
import pro.osin.tools.util.FileUtil;

import java.io.File;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DiskCache<K, V> implements Cache<K, V>, Frequency<K> {

    private final ConcurrentHashMap<K, String> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Integer> frequency = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final int capacity;
    private final String cacheFolder;

    public DiskCache(String cacheFolder, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.cacheFolder = cacheFolder;
    }


    @Override
    public V get(K key) {
        try {
            lock.readLock().lock();
            String fileName = cache.get(key);
            if (fileName == null) {
                return null;
            }
            frequency.computeIfPresent(key, (k, v) -> v + 1);
            return FileUtil.readFile(cacheFolder + File.separator + fileName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.writeLock().lock();
            FileUtil.createPath(cacheFolder);
            FileUtil.writeFile(cacheFolder + File.separator + key, value);
            cache.put(key, key.toString());
            frequency.put(key, 1);
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
            FileUtil.deletePath(cacheFolder);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        try {
            lock.writeLock().lock();
            FileUtil.deleteFile(cache.get(key));
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
        throw new UnsupportedOperationException();
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
            return cache.keySet().stream()
                    .max(Comparator.comparingInt(frequency::get))
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public K getLowFrequencyItem() {
        try {
            lock.readLock().lock();
            return cache.keySet().stream()
                    .min(Comparator.comparingInt(frequency::get))
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }
}
