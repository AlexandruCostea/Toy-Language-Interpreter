package map.interpreter.interpreter6;

import javafx.beans.property.SimpleStringProperty;

public class TableEntry2 {

    private final SimpleStringProperty key;
    private final SimpleStringProperty value1;

    private final SimpleStringProperty value2;

    public TableEntry2(String key, String value, String value2) {
        this.key = new SimpleStringProperty(key);
        this.value1 = new SimpleStringProperty(value);
        this.value2 = new SimpleStringProperty(value2);
    }

    public SimpleStringProperty getKey() {
        return key;
    }

    public SimpleStringProperty getValue1() {
        return value1;
    }

    public SimpleStringProperty getValue2() {
        return value2;
    }
}
