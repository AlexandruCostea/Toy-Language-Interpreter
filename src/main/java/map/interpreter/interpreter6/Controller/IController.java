package map.interpreter.interpreter6.Controller;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Repository.IRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface IController {

    void shutDownExecutor();

    void oneStepForAllPrograms(List<PrgState> programs) throws MyException;
    void allStep() throws MyException;

    void setDisplayFlag(BoolValue val);


    Stream<Integer> getAddressesFromRefVal(IValue val, IHeap<IValue> heap);

    Stream<Integer> getAddressesFromProgram(IDictionary<String, IValue> symTable, IHeap<IValue> heap);

    List<Integer> getAddressesFromAllPrograms(List<PrgState> programs);



    Map<Integer, IValue> garbageCollector(List<Integer> adrList, IHeap<IValue> heap);

    List<PrgState> removeCompletedPrograms(List<PrgState> inPrgList);

    IRepository getRepo();
}
