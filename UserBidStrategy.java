import java.util.List;
import java.util.Scanner;

public class UserBidStrategy implements BidStrategy {
    private final Scanner scanner;

    public UserBidStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public BidDecision makeBidDecision(Player player, List<Property> tableProperties, 
                                        double currentHighestBid, List<Player> allPlayers) {
        while (true) {
            System.out.println("\n--- " + player.getName() + "'s Turn ---");
            System.out.println("Your balance: $" + String.format("%.0f", player.getBalance()));
            System.out.println("Your current bid this round: $" + String.format("%.0f", player.getCurrentRoundBid()));
            System.out.println("Properties on table: " + tableProperties);
            System.out.println("Current highest bid: $" + String.format("%.0f", currentHighestBid));
            
            System.out.println("\nOptions:");
            System.out.println("1. Bid (enter amount in multiples of 1000, must be higher than highest bid)");
            System.out.println("2. Pass");
            System.out.print("> ");
            
            String choice = scanner.nextLine().trim();
            
            if (choice.equals("2")) {
                return BidDecision.pass();
            } else if (choice.equals("1")) {
                System.out.print("Enter your bid: $");
                try {
                    double bid = Double.parseDouble(scanner.nextLine().trim());
                    
                    if (bid < 1000) {
                        System.out.println("Bid must be at least $1000. Try again.");
                        continue;
                    }
                    if (bid % 1000 != 0) {
                        System.out.println("Bid must be a multiple of $1000. Try again.");
                        continue;
                    }
                    if (bid <= currentHighestBid) {
                        System.out.println("Bid must be higher than $" + String.format("%.0f", currentHighestBid) + ". Try again.");
                        continue;
                    }
                    if (player.getBalance() < bid) {
                        System.out.println("You don't have enough balance (need $" + String.format("%.0f", bid) + 
                                         ", have $" + String.format("%.0f", player.getBalance()) + "). Try again.");
                        continue;
                    }
                    
                    return BidDecision.bid(bid);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            } else {
                System.out.println("Invalid choice. Enter 1 or 2.");
            }
        }
    }
}
