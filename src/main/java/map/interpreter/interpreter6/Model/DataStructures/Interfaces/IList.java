package map.interpreter.interpreter6.Model.DataStructures.Interfaces;

import javafx.collections.ObservableList;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IntValue;

import java.util.ArrayList;
import java.util.List;

public interface IList<T> extends Iterable<T>{
    void add(T element);

    T pop() throws MyException;

    void remove(IntValue index) throws MyException;

    BoolValue isEmpty();

    List<T> getList();

    T get(IntValue index) throws MyException;

    IntValue size();

    void clear();
}
