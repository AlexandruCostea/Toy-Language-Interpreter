package map.interpreter.interpreter6.Model.DataStructures.ADTs;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ADTDictionary<K, V> implements IDictionary<K, V> {
    private final HashMap<K, V> dictionary;
    public ADTDictionary() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public synchronized void add(K key, V value) throws MyException{
        if(this.dictionary.containsKey(key))
            throw new MyException("Key "+key+" is already in the dictionary");
        this.dictionary.put(key, value);
    }

    @Override
    public BoolValue containsKey(K key) {
        return new BoolValue(this.dictionary.containsKey(key));
    }

    @Override
    public BoolValue isEmpty() {
        return new BoolValue(this.dictionary.isEmpty());
    }

    @Override
    public synchronized void update(K key, V value) throws MyException {
        if(!this.dictionary.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        this.dictionary.put(key,value);
    }

    @Override
    public synchronized void remove(K key) throws MyException {
        if(!this.dictionary.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        this.dictionary.remove(key);
    }

    @Override
    public V getValue(K key) throws MyException {
        if(!this.dictionary.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        return this.dictionary.get(key);
    }

    @Override
    public HashMap<K,V> getDictionary() {
        return this.dictionary;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.dictionary.entrySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<Map.Entry<K,V>> entries = this.dictionary.entrySet();
        for (Map.Entry<K,V> entry : entries) {
            result.append(entry.getKey().toString()).append("-->").append(entry.getValue().toString()).append(";\n");
        }
        return result.toString();
    }

    @Override
    public synchronized void clear() {
        this.dictionary.clear();
    }
}
