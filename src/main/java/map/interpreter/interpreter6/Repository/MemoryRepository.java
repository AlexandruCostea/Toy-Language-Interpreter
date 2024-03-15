package map.interpreter.interpreter6.Repository;

import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MemoryRepository implements IRepository {

    private  List<PrgState> prgStates;
    private final String logFilePath;

    public MemoryRepository(PrgState prg, String logFilePath) {
        prgStates = new ArrayList<>();
        prgStates.add(prg);
        this.logFilePath = logFilePath;
    }

    @Override
    public List<PrgState> getPrgList() {
        return this.prgStates;
    }

    @Override
    public void setPrgList(List<PrgState> states) {
        this.prgStates = states;
    }

    @Override
    public void addProgram(PrgState prg) {
        this.prgStates.add(prg);
    }

    @Override
    public void logPrgStateExec(PrgState program) throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter
                    (new FileWriter(this.logFilePath, true)));
            logFile.println("Program id: " + program.getProgramId());
            logFile.println(program.toString());
            logFile.close();
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }
}
