package map.interpreter.interpreter6.Model.DataStructures.ADTs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ADTHeap<T> implements IHeap<T> {
    private int adr = 0;
    private Map<Integer, T> heap;

    public ADTHeap() {
        this.heap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized int add(T value) {
        this.adr++;
        heap.put(this.adr, value);
        return this.adr;
    }

    @Override
    public synchronized BoolValue containsAddress(int address) {
        return new BoolValue(this.heap.containsKey(address));
    }

    @Override
    public BoolValue isEmpty() {
        return new BoolValue(this.heap.isEmpty());
    }

    @Override
    public synchronized void update(int adr, T value) throws MyException {
        if(!this.heap.containsKey(adr))
            throw new MyException("Address doesn't exist");
        this.heap.put(adr, value);
    }

    @Override
    public synchronized void delete(int adr) throws MyException {
        if(!this.heap.containsKey(adr))
            throw new MyException("Address doesn't exist");
        this.heap.remove(adr);
    }

    @Override
    public T getValue(int adr) throws MyException {
        if(!this.heap.containsKey(adr))
            throw new MyException("Address doesn't exist");
        return this.heap.get(adr);
    }

    @Override
    public Optional<T> get(int adr) {
        if(!this.heap.containsKey(adr))
            return Optional.empty();
        return Optional.of(this.heap.get(adr));
    }

    @Override
    public Map<Integer, T> getHeap() {
        return this.heap;
    }

    @Override
    public synchronized void setHeap(Map<Integer, T> heap) {
        this.heap = heap;
    }

    @Override
    public Iterator<HashMap.Entry<Integer, T>> iterator() {
        return this.heap.entrySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (HashMap.Entry<Integer, T> entry : this.heap.entrySet()) {
            result.append(entry.getKey()).append("->").append(entry.getValue().toString()).append(";\n");
        }
        return result.toString();
    }

    @Override
    public synchronized void clear() {
        this.heap.clear();
    }
}
