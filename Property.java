public class Property {
    private final int id;
    private final int value;

    public Property(int id) {
        this.id = id;
        this.value = calculateValue(id);
    }

    private int calculateValue(int id) {
        // Property values range from 1000 to 20000 based on property number
        return 1000 + (id * 950);
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Property " + id;
    }

    public String toStringWithValue() {
        return "Property " + id + " ($" + value + ")";
    }
}
