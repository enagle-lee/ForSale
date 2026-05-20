import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private double balance;
    private double checksEarned;
    private final List<Property> properties;
    private double currentBidThisRound;
    private boolean hasPassedThisRound;

    public Player(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.checksEarned = 0;
        this.properties = new ArrayList<>();
        this.currentBidThisRound = 0;
        this.hasPassedThisRound = false;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public double getChecksEarned() {
        return checksEarned;
    }

    public double getTotalMoney() {
        return balance + checksEarned;
    }

    public List<Property> getProperties() {
        return new ArrayList<>(properties);
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }

    public void pay(double amount) {
        this.balance -= amount;
    }

    public void addChecks(double amount) {
        this.checksEarned += amount;
    }

    public void resetRoundState() {
        currentBidThisRound = 0;
        hasPassedThisRound = false;
    }

    public void setBid(double amount) {
        this.currentBidThisRound = amount;
    }

    public double getCurrentRoundBid() {
        return currentBidThisRound;
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
