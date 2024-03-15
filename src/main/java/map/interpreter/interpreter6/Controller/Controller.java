package map.interpreter.interpreter6.Controller;

import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.Exceptions.MyException;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;
import map.interpreter.interpreter6.Model.Values.ReferenceValue;
import map.interpreter.interpreter6.Repository.IRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller implements IController {
    private final IRepository repo;
    private boolean displayFlag;

    private ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
        this.displayFlag = true;
        this.executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void setDisplayFlag(BoolValue value) {
        this.displayFlag = value.getValue();
    }

    @Override
    public void shutDownExecutor() {
        this.executor.shutdownNow();
    }

    @Override
    public void oneStepForAllPrograms(List<PrgState> programs) throws MyException {
        for (PrgState program : programs) {
            this.repo.logPrgStateExec(program);
        }

        List<Callable<PrgState>> callList = programs.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrograms = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    })
                    .filter(p -> p != null)
                    .collect(Collectors.toList());

            programs.addAll(newPrograms);

            for (PrgState program : programs) {
                this.repo.logPrgStateExec(program);
            }

            this.repo.setPrgList(programs);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }

    }

    @Override
    public void allStep() throws MyException {

        List<PrgState> prgList=this.removeCompletedPrograms(repo.getPrgList());
        while(!prgList.isEmpty()){
            PrgState mainPrg = prgList.get(0);
            IHeap<IValue> heap = mainPrg.getHeap();
            heap.setHeap(garbageCollector(getAddressesFromAllPrograms(prgList), heap));
            prgList.forEach(prg -> prg.setHeap(heap.getHeap()));

            this.oneStepForAllPrograms(prgList);
            prgList=this.removeCompletedPrograms(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }
    @Override
    public Stream<Integer> getAddressesFromRefVal(IValue val, IHeap<IValue> heap) {
        return Optional.of(val)
                .filter(v -> v instanceof ReferenceValue)
                .map(refVal -> Stream.concat(
                        Stream.of(((ReferenceValue) refVal).getHeapAddress()),
                        getAddressesFromRefVal(
                                heap.get(((ReferenceValue) refVal).getHeapAddress())
                                        .orElse(new IntValue()),heap)))
                .orElse(Stream.empty())
                .distinct();
    }

    @Override
    public Stream<Integer> getAddressesFromProgram(IDictionary<String, IValue> symTable, IHeap<IValue> heap) {
        return symTable.getDictionary().values().stream()
                .filter(v -> v instanceof ReferenceValue)
                .flatMap(v -> getAddressesFromRefVal(v,heap));
    }

    @Override
    public List<Integer> getAddressesFromAllPrograms(List<PrgState> programs) {
        return programs.stream()
                .flatMap(program -> getAddressesFromProgram(program.getSymTable(), program.getHeap()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, IValue> garbageCollector(List<Integer> adrList, IHeap<IValue> heap) {
        return  heap.getHeap().entrySet().stream()
                .filter(v -> adrList.contains(v.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<PrgState> removeCompletedPrograms(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }
    @Override
    public IRepository getRepo() {
        return this.repo;
    }
}
