package util.sql.type;

public abstract class DataType {
    private String name;

    public DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
