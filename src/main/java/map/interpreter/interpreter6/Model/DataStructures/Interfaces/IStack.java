package map.interpreter.interpreter6.Model.DataStructures.Interfaces;

import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Statements.IStatement;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.Stack;

public interface IStack <T> extends Iterable<T>{

    T pop() throws MyException;

    T peek() throws MyException;
    void push(T value);

    BoolValue isEmpty();

    Stack<T> getStack();

    void clear();

}
