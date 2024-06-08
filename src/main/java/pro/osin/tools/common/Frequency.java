package pro.osin.tools.common;

public interface Frequency<K> {

    /**
     * Retrieves the frequency of the given item.
     *
     * @param  item  the item for which to retrieve the frequency
     * @return       the frequency of the item
     */
    int getItemFrequency(K item);

    /**
     * Retrieves the item with the highest frequency.
     *
     * @return the item with the highest frequency
     */
    K getTopFrequencyItem();

    /**
     * Retrieves the item with the lowest frequency.
     *
     * @return the item with the lowest frequency
     */
    K getLowFrequencyItem();

}
