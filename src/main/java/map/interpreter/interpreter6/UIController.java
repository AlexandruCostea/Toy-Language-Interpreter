package map.interpreter.interpreter6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import map.interpreter.interpreter6.Controller.Controller;
import map.interpreter.interpreter6.Model.DataStructures.ADTs.*;
import map.interpreter.interpreter6.Model.DataStructures.Interfaces.*;
import map.interpreter.interpreter6.Model.Expressions.*;
import map.interpreter.interpreter6.Model.ProgramState.PrgState;
import map.interpreter.interpreter6.Model.Statements.*;
import map.interpreter.interpreter6.Model.Types.*;
import map.interpreter.interpreter6.Model.Values.BoolValue;
import map.interpreter.interpreter6.Model.Values.IValue;
import map.interpreter.interpreter6.Model.Values.IntValue;
import map.interpreter.interpreter6.Model.Values.StringValue;
import map.interpreter.interpreter6.Repository.MemoryRepository;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class UIController {
    @FXML
    protected ListView<IStatement> programList;

    public void initialize() {
        ObservableList<IStatement> programs = generatePrograms();
        programList.setItems(programs);
    }

    public void programSelected(MouseEvent mouseEvent) {
        IStatement selectedProgram = programList.getSelectionModel().getSelectedItem();
        IDictionary<String, IType> typeEnv = new ADTDictionary<>();
        int index = programList.getSelectionModel().getSelectedIndex();

        IStack<IStatement> exeStack = new ADTStack<>();
        IDictionary<String, IValue> symTable = new ADTDictionary<>();
        IHeap<IValue> heap = new ADTHeap<>();
        IDictionary<StringValue, BufferedReader> fileTable = new ADTDictionary<>();
        IList<IValue> out = new ADTList<>();
        ISem<Integer, Pair<Integer, List<Integer>>> semTbl = new ADTSemTable<>();
        PrgState prg = new PrgState(exeStack, symTable, heap, fileTable, out, selectedProgram, semTbl);
        MemoryRepository repo = new MemoryRepository(prg, "logs/log"+(index+1)+".txt");
        Controller ctr = new Controller(repo);

        try {
            selectedProgram.typecheck(typeEnv);
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("programPage.fxml"));
            loader.setControllerFactory(param -> new ProgramController(ctr));
            Scene scene = new Scene(loader.load(), 1200, 450);
            newStage.setTitle("Program" + (index+1));
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            System.out.println("Program launch error: " + e.getMessage());
        }
    }


    public static ObservableList<IStatement> generatePrograms() {
        ArrayList<IStatement> programs = new ArrayList<>();

        //first program: int v; v=2; Print(v)
        IStatement st1 = new VarDecStatement("v", new IntType()); // int v;
        IStatement st2 = new AssignmentStatement("v",new ValueExpression(new IntValue(2))); // v=2;
        IStatement st3 = new PrintStatement(new VariableExpression("v"));// Print(v);
        IStatement st4 = new CompoundStatement(st2, st3); // v=2; Print(v);
        IStatement prog1 = new CompoundStatement(st1, st4); //int v; v=2; Print(v);

        //second program: int a;int b; a=2+3*5;b=a+1;Print(b)
        IStatement st5 = new VarDecStatement("a", new IntType()); // int a;
        IStatement st6 = new VarDecStatement("b", new IntType()); //int b;

        IExpression val1 = new ValueExpression(new IntValue(2)); // 2
        IExpression val2 = new ValueExpression(new IntValue(3)); // 3
        IExpression val3 = new ValueExpression(new IntValue(5)); // 5
        IExpression val4 = new ArithmeticExpression(val2, val3, 3); // 3*5
        IExpression val5 = new ArithmeticExpression(val1, val4, 1); // 2+3*5

        IStatement st7 = new AssignmentStatement("a",val5); // a=2+3*5;

        IExpression val6 = new VariableExpression("a"); //a
        IExpression val7 = new ValueExpression(new IntValue(1)); //1
        IExpression val8 = new ArithmeticExpression(val6, val7, 1); //a+1

        IStatement st8 = new AssignmentStatement("b", val8); // b=a+1;
        IStatement st9 = new PrintStatement(new VariableExpression("b")); //Print(b);

        IStatement st10 = new CompoundStatement(st8, st9); // b=a+1; Print(b);
        IStatement st11 = new CompoundStatement(st7, st10); // a=2+3*5; b=a+1; Print(b);
        IStatement st12 = new CompoundStatement(st6, st11); // int b; a=2+3*5; b=a+1; Print(b);

        IStatement prog2 = new CompoundStatement(st5, st12); // int a; int b; a=2+3*5; b=a+1; Print(b);

        //third program: bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)

        IStatement st13 = new VarDecStatement("a", new BoolType()); // bool a;
        IStatement st14 = new VarDecStatement("v", new IntType()); // int v;
        IExpression ex1 = new ValueExpression(new BoolValue(true)); // true
        IStatement st15 = new AssignmentStatement("a", ex1); // a = true;

        IExpression ex2 = new ValueExpression(new IntValue(2)); // 2
        IExpression ex3 = new ValueExpression(new IntValue(3)); // 3
        IStatement st16 = new AssignmentStatement("v", ex2); // v=2;
        IStatement st17 = new AssignmentStatement("v", ex3); // v=3;
        IStatement st18 = new IfStatement(new VariableExpression("a"), st16, st17);
        // if a then v=2 else v=3;
        IStatement st19 = new PrintStatement(new VariableExpression("v")); // Print(v);

        IStatement st20 = new CompoundStatement(st18, st19); //if a then v=2 else v=3; Print(v);
        IStatement st21 = new CompoundStatement(st15, st20);
        // a=true; if a then v=2 else v=3; Print(v);
        IStatement st22 = new CompoundStatement(st14, st21);
        //int v; a=true; if a then v=2 else v=3; Print(v);
        IStatement prog3 = new CompoundStatement(st13, st22);
        //bool a; int v; a=true; if a then v=2 else v=3; Print(v);

        //fourth program: int a; int b; a = 2; b = 3; if a < b then print(a) else print(b)
        IStatement st23 = new VarDecStatement("a", new IntType()); // int a;
        IStatement st24 = new VarDecStatement("b", new IntType()); // int b;
        IStatement st25 = new AssignmentStatement("a", new ValueExpression(new IntValue(2))); // a = 2;
        IStatement st26 = new AssignmentStatement("b", new ValueExpression(new IntValue(3))); // b = 3;
        IStatement st27 = new IfStatement(new RelationalExpression(new VariableExpression("a"), new VariableExpression("b"), 1),
                new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("b")));
        // if a < b then print(a) else print(b)
        IStatement st28 = new CompoundStatement(st26, st27); // b = 3; if a < b then print(a) else print(b)
        IStatement st29 = new CompoundStatement(st25, st28); // a = 2; b = 3; if a < b then print(a) else print(b)
        IStatement st30 = new CompoundStatement(st24, st29); // int b; a = 2; b = 3; if a < b then print(a) else print(b)
        IStatement prog4 = new CompoundStatement(st23, st30); // int a; int b; a = 2; b = 3; if a < b then print(a) else print(b)

        //fifth program: string varf; varf = "files/test.in"; openRFile(varf); int varc; readFile(varf, varc); print(varc);
        // readFile(varf, varc); print(varc); closeRFile(varf);

        IStatement st31 = new VarDecStatement("varf", new StringType()); // string varf;
        IStatement st32 = new AssignmentStatement("varf", new ValueExpression(new StringValue("files/test.in"))); // varf = "files/test.in";
        IStatement st33 = new OpenRFileStatement(new VariableExpression("varf")); // openRFile(varf);
        IStatement st34 = new VarDecStatement("varc", new IntType()); // int varc;
        IStatement st35 = new ReadFileStatement(new VariableExpression("varf"), new StringValue("varc")); // readFile(varf, varc);
        IStatement st36 = new PrintStatement(new VariableExpression("varc")); // print(varc);
        IStatement st37 = new ReadFileStatement(new VariableExpression("varf"), new StringValue("varc")); // readFile(varf, varc);
        IStatement st38 = new PrintStatement(new VariableExpression("varc")); // print(varc);
        IStatement st39 = new CloseRFileStatement(new VariableExpression("varf")); // closeRFile(varf);
        IStatement st40 = new CompoundStatement(st38, st39); // print(varc); closeRFile(varf);
        IStatement st41 = new CompoundStatement(st37, st40); // readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement st42 = new CompoundStatement(st36, st41); // print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement st43 = new CompoundStatement(st35, st42); // readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement st44 = new CompoundStatement(st34, st43); // int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement st45 = new CompoundStatement(st33, st44); // openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement st46 = new CompoundStatement(st32, st45); // varf = "files/test.in"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf);
        IStatement prog5 = new CompoundStatement(st31, st46); // string varf; varf = "files/test.in"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf);


        //sixth program: int x; x = 5; while(x > 0) { Print(x); x--; }
        IStatement st47 = new VarDecStatement("x", new IntType()); // int x;
        IStatement st48 = new AssignmentStatement("x", new ValueExpression(new IntValue(5))); //x = 5;
        IStatement st49 = new PrintStatement(new VariableExpression("x")); //Print(x);
        IStatement st50 = new AssignmentStatement("x", new ArithmeticExpression(
                new VariableExpression("x"), new ValueExpression(new IntValue(1)), 2)); // x = x-1;
        IStatement st51 = new CompoundStatement(st49, st50); //Print(x); x--;
        IExpression ex4 = new RelationalExpression(new VariableExpression("x"), new ValueExpression(new IntValue(0)), 5);
        // x > 0
        IStatement st52 = new WhileStatement(ex4, st51); // while(x > 0) { Print(x); x--; }
        IStatement st53 = new CompoundStatement(st48, st52); //x = 5; while(x > 0) {Print(x); x--; };
        IStatement prog6 = new CompoundStatement(st47, st53); // int x; x = 5; while(x > 0) {Print(x); x--; };

        //seventh program: Ref(int) v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)
        IStatement st54 = new VarDecStatement("v", new ReferenceType(new IntType())); // Ref(int) v;
        IStatement st55 = new NewStatement("v", new ValueExpression(new IntValue(20))); // new(v,20);
        IStatement st56 = new VarDecStatement("a", new ReferenceType(new ReferenceType(new IntType()))); //Ref(Ref(int)) a;
        IStatement st57 = new NewStatement("a", new VariableExpression("v")); // new(a,v);
        IStatement st58 = new PrintStatement(new VariableExpression("v")); // Print(v);
        IStatement st59 = new PrintStatement(new VariableExpression("a")); // Print(a);
        IStatement st60 = new CompoundStatement(st58,st59);
        IStatement st61 = new CompoundStatement(st57,st60);
        IStatement st62 = new CompoundStatement(st56,st61);
        IStatement st63 = new CompoundStatement(st55,st62);
        IStatement prog7 = new CompoundStatement(st54,st63);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
        IStatement st64 = new VarDecStatement("v", new ReferenceType(new IntType())); // Ref(int) v;
        IStatement st65 = new NewStatement("v", new ValueExpression(new IntValue(20))); // new(v,20);
        IStatement st66 = new VarDecStatement("a", new ReferenceType(new ReferenceType(new IntType()))); //Ref(Ref(int)) a;
        IStatement st67 = new NewStatement("a", new VariableExpression("v")); // new(a,v);
        IStatement st68 = new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))); // Print(rH(v));
        IStatement st69 = new PrintStatement(
                new ArithmeticExpression(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                        new ValueExpression(new IntValue(5)), 1)); // Print(rH(rH(a))+5);
        IStatement st70 = new CompoundStatement(st68,st69);
        IStatement st71 = new CompoundStatement(st67,st70);
        IStatement st72 = new CompoundStatement(st66,st71);
        IStatement st73 = new CompoundStatement(st65,st72);
        IStatement prog8 = new CompoundStatement(st64,st73);

        //Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
        IStatement st74 = new VarDecStatement("v", new ReferenceType(new IntType())); // Ref(int) v;
        IStatement st75 = new NewStatement("v", new ValueExpression(new IntValue(20))); // new(v,20);
        IStatement st76 = new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))); // Print(rH(v));
        IStatement st77 = new WriteHeapStatement("v", new ValueExpression(new IntValue(30))); // wH(v,30);
        IStatement st78 = new PrintStatement(
                new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v")),
                        new ValueExpression(new IntValue(5)), 1)); // Print(rH(v)+5);
        IStatement st79 = new CompoundStatement(st77,st78);
        IStatement st80 = new CompoundStatement(st76,st79);
        IStatement st81 = new CompoundStatement(st75,st80);
        IStatement prog9 = new CompoundStatement(st74,st81);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStatement st82 = new VarDecStatement("v", new ReferenceType(new IntType())); // Ref(int) v;
        IStatement st83 = new NewStatement("v", new ValueExpression(new IntValue(20))); // new(v,20);
        IStatement st84 = new VarDecStatement("a", new ReferenceType(new ReferenceType(new IntType()))); //Ref(Ref(int)) a;
        IStatement st85 = new NewStatement("a", new VariableExpression("v")); // new(a,v);
        IStatement st86 = new NewStatement("v", new ValueExpression(new IntValue(30))); // new(v,30);
        IStatement st87 = new PrintStatement(new ReadHeapExpression(
                new ReadHeapExpression(new VariableExpression("a")))); // Print(rH(rH(a)));Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStatement st88 = new CompoundStatement(st86,st87);
        IStatement st89 = new CompoundStatement(st85,st88);
        IStatement st90 = new CompoundStatement(st84,st89);
        IStatement st91 = new CompoundStatement(st83,st90);
        IStatement prog10 = new CompoundStatement(st82,st91);

        //Ref int v; new(v,20); new(v,50); print(rH(v));
        IStatement st92 = new VarDecStatement("v", new ReferenceType(new IntType())); // Ref(int) v;
        IStatement st93 = new NewStatement("v", new ValueExpression(new IntValue(20))); // new(v,20);
        IStatement st94 = new NewStatement("v", new ValueExpression(new IntValue(50))); // new(v,50);
        IStatement st95 = new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))); // print(rH(v));
        IStatement st96 = new CompoundStatement(st94,st95);
        IStatement st97 = new CompoundStatement(st93,st96);
        IStatement prog11 = new CompoundStatement(st92,st97);

        //int v; Ref int a; v=10;new(a,22);
        //fork(wH(a,30);v=32;print(v);print(rH(a)));
        //print(v);print(rH(a))

        IStatement st98 = new VarDecStatement("v", new IntType()); // int v;
        IStatement st99 = new VarDecStatement("a", new ReferenceType(new IntType())); // Ref(int) a;
        IStatement st100 = new AssignmentStatement("v", new ValueExpression(new IntValue(10))); // v=10;
        IStatement st101 = new NewStatement("a", new ValueExpression(new IntValue(22))); // new(a,22);

        IStatement st102 = new WriteHeapStatement("a", new ValueExpression(new IntValue(30))); // wH(a,30);
        IStatement st103 = new AssignmentStatement("v", new ValueExpression(new IntValue(32))); // v=32;
        IStatement st104 = new PrintStatement(new VariableExpression("v")); // print(v);
        IStatement st105 = new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))); // print(rH(a));
        IStatement st106 = new CompoundStatement(st104,st105); // print(v);print(rH(a));
        IStatement st107 = new CompoundStatement(st103,st106); // v=32;print(v);print(rH(a));
        IStatement st108 = new CompoundStatement(st102,st107); // wH(a,30);v=32;print(v);print(rH(a));

        IStatement st109 = new ForkStatement(st108); // fork(wH(a,30);v=32;print(v);print(rH(a)));

        IStatement st110 = new PrintStatement(new VariableExpression("v")); // print(v);
        IStatement st111 = new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))); // print(rH(a));

        IStatement st112 = new CompoundStatement(st110,st111); // print(v);print(rH(a));
        IStatement st113 = new CompoundStatement(st109,st112); // fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a));
        IStatement st114 = new CompoundStatement(st101,st113); // new(a,22);fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a));
        IStatement st115 = new CompoundStatement(st100,st114); // v=10;new(a,22);fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a));
        IStatement st116 = new CompoundStatement(st99,st115); // Ref int a; v=10;new(a,22);fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a));
        IStatement prog12 = new CompoundStatement(st98,st116); // int v; Ref int a; v=10;new(a,22);fork(wH(a,30);v=32;print(v);print(rH(a)));print(v);print(rH(a));


        //int a; int b; int c;
        //a=1;b=2;c=5;
        //(switch(a*10)
        //(case (b*c) : print(a);print(b))
        //(case (10) : print(100);print(200))
        //(default : print(300)));
        //print(300)

        IStatement st117 = new VarDecStatement("a", new IntType()); // int a;
        IStatement st118 = new VarDecStatement("b", new IntType()); // int b;
        IStatement st119 = new VarDecStatement("c", new IntType()); // int c;
        IStatement st120 = new AssignmentStatement("a", new ValueExpression(new IntValue(1))); // a=1;
        IStatement st121 = new AssignmentStatement("b", new ValueExpression(new IntValue(2))); // b=2;
        IStatement st122 = new AssignmentStatement("c", new ValueExpression(new IntValue(5))); // c=5;
        IExpression ex5 = new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(10)), 3);
        // a*10
        IExpression ex6 = new ArithmeticExpression(new VariableExpression("b"), new VariableExpression("c"), 3);
        // b*c
        IExpression ex7 = new ValueExpression(new IntValue(10)); // 10

        IStatement st123 = new PrintStatement(new VariableExpression("a")); // print(a);
        IStatement st124 = new PrintStatement(new VariableExpression("b")); // print(b);
        IStatement st125 = new CompoundStatement(st123,st124); // print(a);print(b);

        IStatement st126 = new PrintStatement(new ValueExpression(new IntValue(100))); // print(100);
        IStatement st127 = new PrintStatement(new ValueExpression(new IntValue(200))); // print(200);
        IStatement st128 = new CompoundStatement(st126,st127); // print(100);print(200);

        IStatement st129 = new PrintStatement(new ValueExpression(new IntValue(300))); // print(300);

        IStatement st130 = new SwitchStatement(ex5, ex6, st125, ex7, st128, st129);
        // (switch(a*10)

        IStatement st131 = new PrintStatement(new ValueExpression(new IntValue(300))); // print(300);

        IStatement st132 = new CompoundStatement(st130,st131); // (switch(a*10); print(300);

        IStatement st133 = new CompoundStatement(st122,st132); // c=5; (switch(a*10); print(300);
        IStatement st134 = new CompoundStatement(st121,st133); // b=2; c=5; (switch(a*10); print(300);
        IStatement st135 = new CompoundStatement(st120,st134); // a=1; b=2; c=5; (switch(a*10); print(300);
        IStatement st136 = new CompoundStatement(st119,st135); // int c; a=1; b=2; c=5; (switch(a*10); print(300);
        IStatement st137 = new CompoundStatement(st118,st136); // int b; int c; a=1; b=2; c=5; (switch(a*10); print(300);

        IStatement prog13 = new CompoundStatement(st117,st137); // int a; int b; int c; a=1; b=2; c=5; (switch(a*10); print(300);

        //Ref int v1; int cnt;
        //new(v1,1);createSemaphore(cnt,rH(v1));
        //fork(acquire(cnt);wh(v1,rh(v1)*10);print(rh(v1));release(cnt));
        //fork(acquire(cnt);wh(v1,rh(v1)*10);wh(v1,rh(v1)*2);print(rh(v1));release(cnt));acquire(cnt);
        //print(rh(v1)-1);
        //release(cnt)

        IStatement st138 = new VarDecStatement("v1", new ReferenceType(new IntType())); // Ref(int) v1;
        IStatement st139 = new VarDecStatement("cnt", new IntType()); // int cnt;
        IStatement st140 = new NewStatement("v1", new ValueExpression(new IntValue(1))); // new(v1,1);
        IStatement st141 = new CreateSemaphoreStatement("cnt", new ReadHeapExpression(new VariableExpression("v1")));
        // createSemaphore(cnt,rH(v1));

        //stmt inside first fork
        IStatement st142 = new AcquireStatement("cnt"); // acquire(cnt);
        IStatement st143 = new WriteHeapStatement("v1", new ArithmeticExpression(
                new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3));
        IStatement st144 = new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))); // print(rh(v1));
        IStatement st145 = new ReleaseStatement("cnt"); // release(cnt);
        IStatement st146 = new CompoundStatement(st144,st145);
        IStatement st147 = new CompoundStatement(st143,st146);
        IStatement st148 = new CompoundStatement(st142,st147);

        IStatement st149 = new ForkStatement(st148);

        //stmt of second fork
        IStatement st150 = new AcquireStatement("cnt"); // acquire(cnt);
        IStatement st151 = new WriteHeapStatement("v1", new ArithmeticExpression(
                new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3));
        IStatement st152 = new WriteHeapStatement("v1", new ArithmeticExpression(
                new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), 3));
        IStatement st153 = new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))); // print(rh(v1));
        IStatement st154 = new ReleaseStatement("cnt"); // release(cnt);
        IStatement st155 = new CompoundStatement(st153,st154);
        IStatement st156 = new CompoundStatement(st152,st155);
        IStatement st157 = new CompoundStatement(st151,st156);
        IStatement st158 = new CompoundStatement(st150,st157);

        IStatement st159 = new ForkStatement(st158);

        IStatement st160 = new AcquireStatement("cnt"); // acquire(cnt);
        IStatement st161 = new PrintStatement(new ArithmeticExpression(
                new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), 2));

        IStatement st162 = new ReleaseStatement("cnt"); // release(cnt);

        IStatement st163 = new CompoundStatement(st161,st162);
        IStatement st164 = new CompoundStatement(st160,st163);
        IStatement st165 = new CompoundStatement(st159,st164);
        IStatement st166 = new CompoundStatement(st149,st165);
        IStatement st167 = new CompoundStatement(st141,st166);
        IStatement st168 = new CompoundStatement(st140,st167);
        IStatement st169 = new CompoundStatement(st139,st168);
        IStatement prog14 = new CompoundStatement(st138,st169);

        programs.add(prog1);
        programs.add(prog2);
        programs.add(prog3);
        programs.add(prog4);
        programs.add(prog5);
        programs.add(prog6);
        programs.add(prog7);
        programs.add(prog8);
        programs.add(prog9);
        programs.add(prog10);
        programs.add(prog11);
        programs.add(prog12);
        programs.add(prog13);
        programs.add(prog14);
        return FXCollections.observableList(programs);
    }
}