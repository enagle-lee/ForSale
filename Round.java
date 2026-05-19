import java.util.ArrayList;
import java.util.List;

public class Round {
    private final List<Property> tableProperties;
    private final List<Player> activePlayers;
    private final List<Player> allPlayers;
    private double currentHighestBid;
    private Player currentHighestBidder;
    private final ActivityLog activityLog;
    private int passedCount;

    public Round(List<Property> properties, List<Player> players, ActivityLog activityLog) {
        this.tableProperties = new ArrayList<>(properties);
        this.allPlayers = new ArrayList<>(players);
        this.activePlayers = new ArrayList<>(players);
        this.currentHighestBid = 0;
        this.currentHighestBidder = null;
        this.activityLog = activityLog;
        this.passedCount = 0;
    }

    public List<Property> getTableProperties() {
        return new ArrayList<>(tableProperties);
    }

    public List<Player> getActivePlayers() {
        return new ArrayList<>(activePlayers);
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public Player getCurrentHighestBidder() {
        return currentHighestBidder;
    }

    public void handleBid(Player player, double amount) {
        player.bid(amount);
        currentHighestBid = player.getCurrentRoundBid();
        currentHighestBidder = player;
        passedCount = 0; // Reset pass counter when someone bids
        activityLog.log(player.getName() + " bids $" + String.format("%.0f", amount) + 
                       " (total this round: $" + String.format("%.0f", player.getCurrentRoundBid()) + ")");
    }

    public void handlePass(Player player) {
        player.markAsPassedThisRound();
        activePlayers.remove(player);
        passedCount++;
        
        if (!tableProperties.isEmpty()) {
            Property takenProperty = tableProperties.remove(0);
            player.addProperty(takenProperty);
            
            double payment = 0;
            if (player.getCurrentRoundBid() > 0) {
                payment = player.getCurrentRoundBid() / 2.0;
            }
            
            player.pay(payment);
            activityLog.log(player.getName() + " passes, takes " + takenProperty + 
                           ", pays $" + String.format("%.0f", payment));
        } else {
            activityLog.log(player.getName() + " passes (no properties left on table)");
        }
    }

    public boolean isRoundOver() {
        return passedCount >= 3 || activePlayers.size() <= 1;
    }

    public void completeRound() {
        if (currentHighestBidder != null && activePlayers.contains(currentHighestBidder)) {
            double winAmount = currentHighestBidder.getCurrentRoundBid();
            currentHighestBidder.pay(winAmount);
            
            if (!tableProperties.isEmpty()) {
                Property winProperty = tableProperties.get(tableProperties.size() - 1);
                tableProperties.remove(tableProperties.size() - 1);
                currentHighestBidder.addProperty(winProperty);
                activityLog.log(currentHighestBidder.getName() + " wins with bid of $" + 
                               String.format("%.0f", winAmount) + ", gets " + winProperty);
            }
        }
    }
}
