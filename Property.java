public class Property {
    private final int id;

    public Property(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Property " + id;
    }
}
