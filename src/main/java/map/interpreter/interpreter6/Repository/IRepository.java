package map.interpreter.interpreter6.Repository;

import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;

import java.util.List;

public interface IRepository {
    List<PrgState> getPrgList();

    void setPrgList(List<PrgState> prgList);

    void addProgram(PrgState prg);

    void logPrgStateExec(PrgState state) throws MyException;
}
