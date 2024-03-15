package map.interpreter.interpreter6.Model.DataStructures.ADTs;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Values.BoolValue;

import java.util.Iterator;
import java.util.Stack;

public class ADTStack<T> implements IStack<T>{
    private final Stack<T> stack;
    public ADTStack() {
        stack = new Stack<>();
    }

    @Override
    public T pop() throws MyException {
        BoolValue empty = this.isEmpty();
        if(empty.getValue())
            throw new MyException("Stack is empty!");
        return this.stack.pop();
    }

    @Override
    public T peek() throws MyException {
        BoolValue empty = this.isEmpty();
        if(empty.getValue())
            throw new MyException("Stack is empty!");
        return this.stack.peek();
    }

    @Override
    public void push(T element) {
        this.stack.push(element);
    }

    @Override
    public BoolValue isEmpty() {
        return new BoolValue(this.stack.isEmpty());
    }

    @Override
    public Stack<T> getStack() {
        return this.stack;
    }

    @Override
    public Iterator<T> iterator() {
        return this.stack.iterator();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(int i = this.stack.size() - 1; i >= 0; i--)
            result.append(this.stack.get(i).toString()).append("\n");
        return result.toString();
    }

    @Override
    public void clear() {
        this.stack.clear();
    }

}
