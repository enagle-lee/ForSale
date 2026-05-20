public class Property {
    private final int id;

    public Property(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return id;  // Value is just the property number (1-20)
    }

    @Override
    public String toString() {
        return "Property " + id;
    }

    public String toStringWithValue() {
        return "Property " + id;
    }
}
