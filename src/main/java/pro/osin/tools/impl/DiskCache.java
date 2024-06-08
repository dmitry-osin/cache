package pro.osin.tools.impl;

import pro.osin.tools.common.Cache;
import pro.osin.tools.common.Frequency;
import pro.osin.tools.util.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class DiskCache<K, V extends Serializable> implements Cache<K, V>, Frequency<K> {

    public static final String DATA_FOLDER = "data";
    private final ConcurrentHashMap<K, String> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Integer> frequency = new ConcurrentHashMap<>();
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
        String fileName = cache.get(key);
        if (fileName == null) {
            return null;
        }
        frequency.computeIfPresent(key, (k, v) -> v + 1);
        return FileUtil.readFile(cacheFolder + File.separator + DATA_FOLDER + File.separator + fileName);
    }

    @Override
    public void put(K key, V value) {
        FileUtil.writeFile(cacheFolder + File.separator + DATA_FOLDER + File.separator + key, value);
        cache.put(key, key.toString());
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
        FileUtil.deletePath(cacheFolder);
    }

    @Override
    public void remove(K key) {
        FileUtil.deleteFile(cache.get(key));
        cache.remove(key);
        frequency.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        throw new UnsupportedOperationException();
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
        return cache.keySet().stream()
                .max(Comparator.comparingInt(frequency::get))
                .orElse(null);
    }

    @Override
    public K getLowFrequencyItem() {
        return cache.keySet().stream()
                .min(Comparator.comparingInt(frequency::get))
                .orElse(null);
    }
}
