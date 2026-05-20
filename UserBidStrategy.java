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
            System.out.println("1. Bid (enter INCREMENT amount in multiples of 1000, minimum total $1000)");
            System.out.println("2. Pass");
            System.out.print("> ");
            
            String choice = scanner.nextLine().trim();
            
            if (choice.equals("2")) {
                return BidDecision.pass();
            } else if (choice.equals("1")) {
                System.out.print("Enter bid INCREMENT amount: $");
                try {
                    double increment = Double.parseDouble(scanner.nextLine().trim());
                    double totalBid = player.getCurrentRoundBid() + increment;
                    
                    if (increment <= 0) {
                        System.out.println("Bid increment must be greater than 0. Try again.");
                        continue;
                    }
                    if (increment % 1000 != 0) {
                        System.out.println("Bid increment must be a multiple of $1000. Try again.");
                        continue;
                    }
                    if (totalBid < 1000) {
                        System.out.println("Total bid must be at least $1000. Try again.");
                        continue;
                    }
                    if (totalBid <= currentHighestBid) {
                        System.out.println("Total bid must be higher than $" + String.format("%.0f", currentHighestBid) + 
                                         ". Your total would be $" + String.format("%.0f", totalBid) + ". Try again.");
                        continue;
                    }
                    if (player.getBalance() < totalBid) {
                        System.out.println("You don't have enough balance (need $" + String.format("%.0f", totalBid) + 
                                         " total, have $" + String.format("%.0f", player.getBalance()) + "). Try again.");
                        continue;
                    }
                    
                    System.out.println("  Total bid this round will be: $" + String.format("%.0f", totalBid));
                    return BidDecision.bid(increment);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            } else {
                System.out.println("Invalid choice. Enter 1 or 2.");
            }
        }
    }
}
