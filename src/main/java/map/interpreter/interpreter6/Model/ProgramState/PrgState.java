package map.interpreter.interpreter6.Model.ProgramState;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.*;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.Statements.IStatement;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PrgState {

    private static AtomicInteger threadCount = new AtomicInteger(0);

    private final int programId;
    private IStack<IStatement> exeStack;
    private IDictionary<String, IValue> symTable;

    private IHeap<IValue> heap;

    private IDictionary<StringValue, BufferedReader> fileTable;
    private IList<IValue> out;
    private ISem<Integer, Pair<Integer, List<Integer>>> semaphoreTable;

    private IStatement originalProgram;
    public PrgState(IStack<IStatement> stk, IDictionary<String, IValue> symTable,
                     IHeap<IValue> heap, IDictionary<StringValue, BufferedReader> fileTable,
                    IList<IValue> out, IStatement prg, ISem<Integer, Pair<Integer, List<Integer>>> semTbl) {
        int id =  PrgState.threadCount.incrementAndGet();
        this.programId = id;
        PrgState.threadCount = new AtomicInteger(id);
        this.exeStack = stk;
        this.symTable = symTable;
        this.heap = heap;
        this.fileTable = fileTable;
        this.out = out;
        this.originalProgram = prg.deepCopy();
        this.exeStack.push(this.originalProgram);
        this.semaphoreTable = semTbl;
    }

    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty().getValue();
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty().getValue())
            throw new MyException("Program execution stack is empty");
        IStatement crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        String result = "---------------------------\n";
        result+= "Execution Stack:\n";
        result+= this.exeStack.toString() + "\n";
        result+= "Symbol Table:\n";
        result+= this.symTable.toString() + "\n";
        result+= "Heap:\n";
        result+= this.heap.toString() + "\n";
        result+= "File Table:\n";
        result+= this.fileTable.toString() + "\n";
        result+= "Output:\n";
        result+= this.out.toString() + "\n";
        result+= "---------------------------\n";
        result+= "Semaphore table:\n";
        result+= this.semaphoreTable.toString() + "\n";
        result+= "---------------------------\n";
        return result;
    }

    public int getProgramId() {
        return this.programId;
    }

    public IStack<IStatement> getExeStack() {
        return this.exeStack;
    }

    public void setExeStack(IStack<IStatement> stk) {
        this.exeStack = stk;
    }

    public IDictionary<String, IValue> getSymTable() {
        return this.symTable;
    }

    public void setSymTable(IDictionary<String, IValue> tbl) {
        this.symTable = tbl;
    }

    public IHeap<IValue> getHeap() {
        return this.heap;
    }

    public void setHeap(Map<Integer, IValue> heap) {
        this.heap.setHeap(heap);
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return this.fileTable;
    }

    public void setFileTable(IDictionary<StringValue, BufferedReader> tbl) {
        this.fileTable = tbl;
    }

    public IList<IValue> getOut() {
        return this.out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IStatement getOriginalProgram() {
        return this.originalProgram;
    }

    public void setOriginalProgram(IStatement stmt) {
        this.originalProgram = stmt;
    }

    public ISem<Integer, Pair<Integer, List<Integer>>> getSemaphoreTable() {
        return this.semaphoreTable;
    }

    public void setSemaphoreTable(ISem<Integer, Pair<Integer, List<Integer>>> tbl) {
        this.semaphoreTable = tbl;
    }

    public void changeProgram(IStatement program) {
        this.originalProgram = program.deepCopy();
        this.exeStack.clear();
        this.symTable.clear();
        this.out.clear();
        this.fileTable.clear();
        this.heap.clear();
        this.semaphoreTable.clear();
        this.exeStack.push(this.originalProgram);
    }
}
