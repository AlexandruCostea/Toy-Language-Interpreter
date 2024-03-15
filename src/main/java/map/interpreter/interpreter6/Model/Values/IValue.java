package map.interpreter.interpreter6.Model.Values;

import map.interpreter.interpreter6.Model.Types.IType;

public interface IValue {
    IType getType();

    IValue deepCopy();

    @Override
    String toString();
}
