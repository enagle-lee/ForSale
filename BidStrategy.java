import java.util.List;

public interface BidStrategy {
    BidDecision makeBidDecision(Player player, List<Property> tableProperties, 
                                 double currentHighestBid, List<Player> allPlayers);
}
