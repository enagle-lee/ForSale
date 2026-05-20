import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private double balance;
    private final List<Property> properties;
    private double currentRoundBid;
    private boolean hasPassedThisRound;

    public Player(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.properties = new ArrayList<>();
        this.currentRoundBid = 0;
        this.hasPassedThisRound = false;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public List<Property> getProperties() {
        return new ArrayList<>(properties);
    }

    public int getPropertyValue() {
        int total = 0;
        for (Property p : properties) {
            total += p.getValue();
        }
        return total;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void pay(double amount) {
        this.balance -= amount;
    }

    public void resetRoundState() {
        currentRoundBid = 0;
        hasPassedThisRound = false;
    }

    public void bid(double amount) {
        currentRoundBid += amount;
    }

    public double getCurrentRoundBid() {
        return currentRoundBid;
    }

    public void markAsPassedThisRound() {
        hasPassedThisRound = true;
    }

    public boolean hasPassedThisRound() {
        return hasPassedThisRound;
    }

    @Override
    public String toString() {
        return name + " - Balance: $" + String.format("%.0f", balance) + 
               ", Properties: " + properties.size();
    }
}
