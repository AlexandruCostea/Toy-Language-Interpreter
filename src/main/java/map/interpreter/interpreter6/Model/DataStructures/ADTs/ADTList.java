package map.interpreter.interpreter6.Model.DataStructures.ADTs;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IList;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ADTList<T> implements IList<T> {
    private final List<T> list;

    public ADTList() {
        this.list = new ArrayList<>();
    }

    @Override
    public synchronized void add(T element) {
        list.add(element);
    }

    @Override
    public synchronized T pop() throws MyException {
        if(this.list.isEmpty())
            throw new MyException("The list is empty!");
        T element = this.list.get(0);
        this.list.remove(0);
        return element;
    }

    @Override
    public synchronized void remove(IntValue index) throws MyException {
        if(index.getValue() >= this.list.size())
            throw new MyException("Index out of bounds");
        this.list.remove(index.getValue());
    }

    @Override
    public BoolValue isEmpty() {
        return new BoolValue(this.list.isEmpty());
    }

    @Override
    public List<T> getList() {
        return this.list;
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    @Override
    public T get(IntValue index) throws MyException {
        if(index.getValue() >= this.list.size())
            throw new MyException("Index out of bounds");
        return this.list.get(index.getValue());
    }

    @Override
    public IntValue size() {
        return new IntValue(this.list.size());
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (T t : this.list) {
            result.append(t.toString()).append(";\n");
        }
        return result.toString();
    }

    @Override
    public synchronized void clear() {
        this.list.clear();
    }
}
