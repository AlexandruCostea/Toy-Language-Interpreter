package map.interpreter.interpreter6.Model.Values;

import map.interpreter.interpreter6.Model.Types.IType;
import map.interpreter.interpreter6.Model.Types.ReferenceType;

public class ReferenceValue implements IValue {
    private int heapAddress;
    private final IType locationType;

    public ReferenceValue(int address, IType type) {
        this.heapAddress = address;
        this.locationType = type;
    }

    public int getHeapAddress() {
        return this.heapAddress;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    public void setHeapAddress(int adr) {
        this.heapAddress = adr;
    }

    public IType getType() {
        return new ReferenceType(this.locationType.deepCopy());
    }

    @Override
    public String toString() {
        return "Ref(" + Integer.toString(this.heapAddress) + "," + this.locationType.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ReferenceValue)
            return this.locationType == ((ReferenceValue) obj).locationType &&
                    this.heapAddress == ((ReferenceValue) obj).heapAddress;
        return false;
    }

    @Override
    public IValue deepCopy() {
        return new ReferenceValue(this.heapAddress, this.locationType.deepCopy());
    }
}
