package pro.osin.tools.impl;

import lombok.SneakyThrows;
import pro.osin.tools.common.Cache;
import pro.osin.tools.common.CacheException;
import pro.osin.tools.util.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StandardCache<K, V extends Serializable> implements Cache<K, V>{

    public static final String DEFAULT_CACHE_FOLDER = "cache";
    public static final int DEFAULT_MEMORY_CACHE_CAPACITY = 10_000;
    public static final int DEFAULT_DISK_CACHE_CAPACITY = 10_000;

    private final MemoryCache<K, V> memoryCache = new MemoryCache<>(DEFAULT_MEMORY_CACHE_CAPACITY);
    private final DiskCache<K, V> diskCache = new DiskCache<>(DEFAULT_CACHE_FOLDER, DEFAULT_DISK_CACHE_CAPACITY);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public StandardCache() {
        createCacheStructure();
    }

    @SneakyThrows
    @Override
    public V get(K key) {
        lock.writeLock().lock();
        try {
            if (memoryCache.containsKey(key)) {
                return memoryCache.get(key);
            } else if (diskCache.containsKey(key)) {
                return diskCache.get(key);
            } else {
                throw new CacheException("cache doesn't contain element with key " + key);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            if (memoryCache.isFull()) {
                K memoryCacheLowFrequencyItemKey = memoryCache.getLowFrequencyItem();
                V memoryCacheLowFrequencyItemValue = memoryCache.get(memoryCacheLowFrequencyItemKey);

                if (diskCache.isFull()) {
                    K diskCacheLowFrequencyItem = diskCache.getLowFrequencyItem();
                    diskCache.remove(diskCacheLowFrequencyItem);
                }

                diskCache.put(memoryCacheLowFrequencyItemKey, memoryCacheLowFrequencyItemValue);
                memoryCache.remove(memoryCacheLowFrequencyItemKey);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return memoryCache.size() + diskCache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            memoryCache.clear();
            diskCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SneakyThrows
    @Override
    public void remove(K key) {
        lock.writeLock().lock();
        try {
            if (memoryCache.containsKey(key)) {
                memoryCache.remove(key);
            } else if (diskCache.containsKey(key)) {
                diskCache.remove(key);
            } else {
                throw new CacheException("element with key " + key + " not found");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return memoryCache.containsKey(key) || diskCache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * This method will check only memory cache because disk cache stores data to disk and check is expensive operation
     * @param value value to check
     * @return contains or not
     */
    @Override
    public boolean containsValue(V value) {
        lock.readLock().lock();
        try {
            return memoryCache.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return memoryCache.isEmpty() && diskCache.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isFull() {
        lock.readLock().lock();
        try {
            return memoryCache.isFull() || diskCache.isFull();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getCapacity() {
        lock.readLock().lock();
        try {
            return memoryCache.getCapacity() + diskCache.getCapacity();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void createCacheStructure() {
        FileUtil.createPath(DEFAULT_CACHE_FOLDER + File.separator + "data");
    }
}
