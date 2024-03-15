package map.interpreter.interpreter6;
import javafx.beans.property.SimpleStringProperty;

public class TableEntry {
    private final SimpleStringProperty key;
    private final SimpleStringProperty value;

    public TableEntry(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public SimpleStringProperty getKey() {
        return key;
    }

    public SimpleStringProperty getValue() {
        return value;
    }
}
