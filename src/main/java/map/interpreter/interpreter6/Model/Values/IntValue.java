package map.interpreter.interpreter6.Model.Values;

import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.IntType;

public class IntValue implements IValue {
    private final int value;
    public IntValue() {
        this.value = 0;
    }

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IntValue)
            return this.value == ((IntValue)obj).value;
        return false;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public IValue deepCopy() {
        return new IntValue(this.value);
    }
}
