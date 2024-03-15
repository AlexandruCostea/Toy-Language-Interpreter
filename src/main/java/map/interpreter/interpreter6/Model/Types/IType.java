package map.interpreter.interpreter6.Model.Types;

import map.interpreter.interpreter6.Model.Values.IValue;

//interface implemented by all of ToyLang's data types
public interface IType {
    IValue defaultValue();

    IType deepCopy();
}
