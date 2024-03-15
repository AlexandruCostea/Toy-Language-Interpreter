package map.interpreter.interpreter6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import map.interpreter.interpreter6.Controller.IController;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IDictionary;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IHeap;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.ISem;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.IStack;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Statements.IStatement;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.StringValue;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

public class ProgramController {

    private final IController ctrl;

    @FXML
    protected  TableColumn<TableEntry2, String> listCol;

    @FXML
    protected TableColumn<TableEntry2, String> semValCol;

    @FXML
    protected TableColumn<TableEntry2, String> indexCol;

    @FXML
    protected TableView<TableEntry2> semTable;

    @FXML
    protected TextField prgCount;

    @FXML
    protected Button oneStepButton;

    @FXML
    protected Button allStepButton;

    @FXML
    protected TableView<TableEntry> heap;

    @FXML
    protected TableColumn<TableEntry, String> adrCol;

    @FXML
    protected TableColumn<TableEntry, String> valCol;

    @FXML
    protected ListView<IValue> out;

    @FXML
    protected TableView<TableEntry> files;

    @FXML
    protected TableColumn<TableEntry, String> fileNameCol;

    @FXML
    protected TableColumn<TableEntry, String> readerCol;

    @FXML
    protected ListView<String> programs;

    @FXML
    protected TableView<TableEntry> symTable;

    @FXML
    protected TableColumn<TableEntry, String> variableCol;

    @FXML
    protected TableColumn<TableEntry, String> valueCol;

    @FXML
    protected ListView<IStatement> exeStack;

    private IStack<IStatement> currentStack;

    private IDictionary<String, IValue> currentTable;

    public ProgramController(IController ctrl) {
        this.ctrl = ctrl;
    }

    public void initialize() {
        this.prgCount.setText("1");
        this.adrCol.setCellValueFactory(cellData -> cellData.getValue().getKey());
        this.valCol.setCellValueFactory(cellData -> cellData.getValue().getValue());
        this.fileNameCol.setCellValueFactory(cellData -> cellData.getValue().getKey());
        this.readerCol.setCellValueFactory(cellData -> cellData.getValue().getValue());
        this.variableCol.setCellValueFactory(cellData -> cellData.getValue().getKey());
        this.valueCol.setCellValueFactory(cellData -> cellData.getValue().getValue());
        this.listCol.setCellValueFactory(cellData -> cellData.getValue().getValue2());
        this.semValCol.setCellValueFactory(cellData -> cellData.getValue().getValue1());
        this.indexCol.setCellValueFactory(cellData -> cellData.getValue().getKey());
        setProgramView();

        programs.getSelectionModel().selectFirst();
        programs.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null));
    }

    public void oneStep(MouseEvent mouseEvent) {
        List<PrgState> prgList = this.ctrl.removeCompletedPrograms(this.ctrl.getRepo().getPrgList());
        try {
            if (!prgList.isEmpty()) {
                PrgState mainPrg = prgList.getFirst();
                IHeap<IValue> heap = mainPrg.getHeap();
                heap.setHeap(this.ctrl.garbageCollector(this.ctrl.getAddressesFromAllPrograms(prgList), heap));
                prgList.forEach(prg -> prg.setHeap(heap.getHeap()));

                this.ctrl.oneStepForAllPrograms(prgList);
                prgList = this.ctrl.removeCompletedPrograms(this.ctrl.getRepo().getPrgList());
                this.prgCount.setText(Integer.toString(prgList.size()));

                setHeapView();
                setListView();
                setFileView();
                setProgramView();
                setSymTableView(this.currentTable);
                setExeStack(this.currentStack);
                setSemTable();

            } else {
                this.ctrl.shutDownExecutor();
                this.ctrl.getRepo().setPrgList(prgList);
                this.prgCount.setText("0");
                this.oneStepButton.setVisible(false);
                this.allStepButton.setVisible(false);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void allStep(MouseEvent mouseEvent) {
        try {
            List<PrgState> prgList = this.ctrl.removeCompletedPrograms(this.ctrl.getRepo().getPrgList());
            while(!prgList.isEmpty()) {
                this.oneStep(null);
                prgList = this.ctrl.getRepo().getPrgList();
            }
            this.prgCount.setText("0");
            this.oneStepButton.setVisible(false);
            this.allStepButton.setVisible(false);
        } catch (Exception e) {
            System.out.println("All step error: " + e.getMessage());
        }
    }

    public void programSelected(MouseEvent mouseEvent) {
        String selectedProgram = programs.getSelectionModel().getSelectedItem().split(" ")[1];
        for(PrgState program: this.ctrl.getRepo().getPrgList()) {
            if(Integer.parseInt(selectedProgram) == program.getProgramId()) {
                this.currentTable = program.getSymTable();
                this.currentStack = program.getExeStack();
                setSymTableView(program.getSymTable());
                setExeStack(program.getExeStack());
                break;
            }
        }
    }

    void setHeapView() {
        ObservableList<TableEntry> data = FXCollections.observableArrayList();
        for (Map.Entry<Integer, IValue> entry : this.ctrl.getRepo().getPrgList().getFirst().getHeap().getHeap().entrySet())
            data.add(new TableEntry(Integer.toString(entry.getKey()), entry.getValue().toString()));
        this.heap.setItems(data);
    }

    void setListView() {
        ObservableList<IValue> data = FXCollections.observableList(this.ctrl.getRepo().getPrgList().getFirst().getOut().getList());
        this.out.setItems(data);
    }

    void setFileView() {
        ObservableList<TableEntry> data = FXCollections.observableArrayList();
        for (Map.Entry<StringValue, BufferedReader> entry : this.ctrl.getRepo().getPrgList().getFirst().getFileTable().getDictionary().entrySet())
            data.add(new TableEntry(entry.getKey().toString(), entry.getValue().toString()));
        this.files.setItems(data);
    }

    void setProgramView() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for(PrgState program: this.ctrl.getRepo().getPrgList())
            data.add("Program " + program.getProgramId());
        this.programs.setItems(data);
    }

    void setSymTableView(IDictionary<String, IValue> symTable) {
        ObservableList<TableEntry> data = FXCollections.observableArrayList();
        for (Map.Entry<String, IValue> entry : symTable.getDictionary().entrySet())
            data.add(new TableEntry(entry.getKey(), entry.getValue().toString()));
        this.symTable.setItems(data);
    }

    void setExeStack(IStack<IStatement> exeStack) {
        ObservableList<IStatement> data = FXCollections.observableArrayList();
        data.addAll(exeStack.getStack().reversed());
        this.exeStack.setItems(data);
    }

    void setSemTable() {
        ISem<Integer, Pair<Integer, List<Integer>>> semaphoreTable = this.ctrl.getRepo().getPrgList().getFirst().getSemaphoreTable();
        ObservableList<TableEntry2> data = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : semaphoreTable.getSemTable().entrySet())
            data.add(new TableEntry2(entry.getKey().toString(), entry.getValue().getKey().toString(), entry.getValue().getValue().toString()));
        this.semTable.setItems(data);
    }
}
