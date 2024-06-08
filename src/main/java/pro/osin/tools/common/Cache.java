package pro.osin.tools.common;

public interface Cache<K, V> {

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this cache
     * contains no mapping for the key
     */
    V get(K key);

    /**
     * Inserts the specified value with the specified key into the cache.
     *
     * @param key   the key with which the value is to be associated
     * @param value the value to be associated with the specified key
     */
    void put(K key, V value);

    /**
     * Returns the number of elements in the cache.
     *
     * @return the size of the cache
     */
    int size();

    /**
     * Clears the cache, removing all elements.
     *
     * @implNote This method should be implemented to remove all elements from the cache.
     */
    void clear();

    /**
     * Removes the element with the specified key from the cache.
     *
     * @param key the key of the element to be removed
     */
    void remove(K key);

    /**
     * Checks if the cache contains a specific key.
     *
     * @param key the key to search for
     * @return true if the cache contains the key, false otherwise
     */
    boolean containsKey(K key);

    /**
     * Checks if the cache contains a specific value.
     *
     * @param value the value to search for
     * @return true if the cache contains the value, false otherwise
     */
    boolean containsValue(V value);

    /**
     * Checks if the cache is empty.
     *
     * @return true if the cache is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Checks if the cache is full.
     *
     * @return true if the cache is full, false otherwise
     */
    boolean isFull();

    /**
     * Returns the capacity of the cache.
     *
     * @return the capacity of the cache
     */
    int getCapacity();

}
