public class BidDecision {
    private final boolean isBid;
    private final double amount;

    private BidDecision(boolean isBid, double amount) {
        this.isBid = isBid;
        this.amount = amount;
    }

    public static BidDecision bid(double amount) {
        return new BidDecision(true, amount);
    }

    public static BidDecision pass() {
        return new BidDecision(false, 0);
    }

    public boolean isBid() {
        return isBid;
    }

    public double getAmount() {
        return amount;
    }
}
