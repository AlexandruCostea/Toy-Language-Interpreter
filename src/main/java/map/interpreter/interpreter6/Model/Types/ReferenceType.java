package map.interpreter.interpreter6.Model.Types;

import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.ReferenceValue;

public class ReferenceType implements IType {
    private final IType inner;

    public ReferenceType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Object another) {
        if(another instanceof ReferenceType) {
            return this.inner.equals(((ReferenceType) another).inner);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ref(" + this.inner.toString() + ")";
    }

    @Override
    public IValue defaultValue() {
        return new ReferenceValue(0, this.inner);
    }

    @Override
    public IType deepCopy() {
        return new ReferenceType(this.inner.deepCopy());
    }
}
