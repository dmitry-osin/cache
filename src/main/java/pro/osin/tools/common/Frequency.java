package pro.osin.tools.common;

public interface Frequency<K> {

    int getItemFrequency(K item);

    K getTopFrequencyItem();

    K getLowFrequencyItem();

}
