package map.interpreter.interpreter6.Model.DataStructures.Interfaces;

import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.HashMap;
import java.util.Map;

public interface ISem<K, V> extends Iterable<Map.Entry<K,V>> {
    void add(K key, V value) throws MyException;

    BoolValue containsKey(K key);

    BoolValue isEmpty();

    void update(K key, V value) throws MyException;

    void remove(K key) throws MyException;

    V getValue(K key) throws MyException;

    HashMap<K,V> getSemTable();

    int getFreeAddress();

    void clear();
}
