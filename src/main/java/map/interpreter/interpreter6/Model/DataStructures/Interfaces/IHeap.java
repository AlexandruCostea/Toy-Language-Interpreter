package map.interpreter.interpreter6.Model.DataStructures.Interfaces;

import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.Map;
import java.util.Optional;

public interface IHeap<T> extends Iterable<Map.Entry<Integer,T>> {
    int add(T value);

    BoolValue containsAddress(int adr);

    BoolValue isEmpty();

    void update(int address, T value) throws MyException;

    void delete(int adr) throws MyException;

    T getValue(int adr) throws MyException;

    Optional<T> get(int adr);

    Map<Integer,T> getHeap();

    void setHeap(Map<Integer, T> heap);

    void clear();
}
