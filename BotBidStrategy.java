import java.util.List;
import java.util.Random;

public class BotBidStrategy implements BidStrategy {
    private final String botName;
    private final Random random = new Random();
    private final double aggressiveness;

    public BotBidStrategy(String botName, double aggressiveness) {
        this.botName = botName;
        this.aggressiveness = aggressiveness;
    }

    @Override
    public BidDecision makeBidDecision(Player player, List<Property> tableProperties, 
                                        double currentHighestBid, List<Player> allPlayers) {
        double balanceRatio = player.getBalance() / 18000.0;
        double bidRatio = currentHighestBid / player.getBalance();
        
        double threshold = 0.25 + (aggressiveness * 0.15);
        
        if (bidRatio > threshold) {
            return BidDecision.pass();
        }
        
        if (balanceRatio < 0.1) {
            return BidDecision.pass();
        }
        
        if (random.nextDouble() < (0.3 - aggressiveness * 0.2)) {
            return BidDecision.pass();
        }
        
        double nextBid = currentHighestBid + 1000;
        if (player.getBalance() >= nextBid) {
            return BidDecision.bid(nextBid);
        }
        
        return BidDecision.pass();
    }
}
