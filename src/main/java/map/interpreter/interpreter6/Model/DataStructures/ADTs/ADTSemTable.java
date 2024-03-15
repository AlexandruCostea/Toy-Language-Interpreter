package map.interpreter.interpreter6.Model.DataStructures.ADTs;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.ISem;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ADTSemTable<K,V> implements ISem<K,V> {
    private final HashMap<K, V> semTable;

    private int freeLocation = 0;
    public ADTSemTable() {
        this.semTable = new HashMap<>();
    }

    @Override
    public synchronized void add(K key, V value) throws MyException {
        if(this.semTable.containsKey(key))
            throw new MyException("Key "+key+" is already in the semaphore table");
        this.semTable.put(key, value);
    }

    @Override
    public synchronized BoolValue containsKey(K key) {
        return new BoolValue(this.semTable.containsKey(key));
    }

    @Override
    public synchronized BoolValue isEmpty() {
        return new BoolValue(this.semTable.isEmpty());
    }

    @Override
    public synchronized void update(K key, V value) throws MyException {
        if(!this.semTable.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        this.semTable.put(key,value);
    }

    @Override
    public synchronized void remove(K key) throws MyException {
        if(!this.semTable.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        this.semTable.remove(key);
    }

    @Override
    public synchronized V getValue(K key) throws MyException {
        if(!this.semTable.containsKey(key))
            throw new MyException("Key "+key+" not found!");
        return this.semTable.get(key);
    }

    @Override
    public HashMap<K,V> getSemTable() {
        return this.semTable;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.semTable.entrySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<Map.Entry<K,V>> entries = this.semTable.entrySet();
        for (Map.Entry<K,V> entry : entries) {
            result.append(entry.getKey().toString()).append("-->").append(entry.getValue().toString()).append(";\n");
        }
        return result.toString();
    }

    @Override
    public synchronized int getFreeAddress() {
        this.freeLocation++;
        return freeLocation;
    }

    @Override
    public synchronized void clear() {
        this.semTable.clear();
    }
}
