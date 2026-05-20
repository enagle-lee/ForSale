import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final List<Player> players;
    private final ActivityLog activityLog;
    private final BidStrategy[] strategies;
    private int currentRoundNumber;
    private final List<Integer> availableProperties;
    private Scanner scanner;

    public Game() {
        this.players = new ArrayList<>();
        this.activityLog = new ActivityLog();
        this.strategies = new BidStrategy[4];
        this.currentRoundNumber = 0;
        this.availableProperties = new ArrayList<>();
        
        initializeAvailableProperties();
        initializePlayers();
    }

    private void initializeAvailableProperties() {
        for (int i = 1; i <= 20; i++) {
            availableProperties.add(i);
        }
        Collections.shuffle(availableProperties);
    }

    private void initializePlayers() {
        this.scanner = new Scanner(System.in);
        
        Player user = new Player("You", 18000);
        players.add(user);
        strategies[0] = new UserBidStrategy(scanner);
        
        Player ava = new Player("Ava", 18000);
        players.add(ava);
        strategies[1] = new BotBidStrategy("Ava", 0.5);
        
        Player boris = new Player("Boris", 18000);
        players.add(boris);
        strategies[2] = new BotBidStrategy("Boris", 0.7);
        
        Player cleo = new Player("Cleo", 18000);
        players.add(cleo);
        strategies[3] = new BotBidStrategy("Cleo", 0.4);
        
        activityLog.log("Game started! All players begin with $18,000");
        activityLog.log("Players: " + String.join(", ", players.stream()
                                                           .map(Player::getName)
                                                           .toArray(String[]::new)));
    }

    public void playRound() {
        currentRoundNumber++;
        activityLog.log("\n========== ROUND " + currentRoundNumber + " ==========");
        
        List<Property> roundProperties = new ArrayList<>();
        for (int i = 0; i < 4 && !availableProperties.isEmpty(); i++) {
            roundProperties.add(new Property(availableProperties.remove(0)));
        }
        
        if (roundProperties.isEmpty()) {
            return;
        }
        
        displayGameState();
        System.out.println("\nProperties on table: " + roundProperties);
        
        Round round = new Round(roundProperties, players, activityLog);
        
        for (Player player : players) {
            player.resetRoundState();
        }
        
        List<Player> activePlayers = new ArrayList<>(players);
        int turnCount = 0;
        
        while (round.getActivePlayers().size() > 1) {
            // Get remaining active players
            activePlayers = round.getActivePlayers();
            if (activePlayers.isEmpty()) break;
            
            // Rotate through active players
            Player currentPlayer = activePlayers.get(turnCount % activePlayers.size());
            turnCount++;
            
            int strategyIndex = players.indexOf(currentPlayer);
            BidStrategy strategy = strategies[strategyIndex];
            
            System.out.println("\n--- " + currentPlayer.getName() + "'s Turn ---");
            System.out.println("Active players remaining: " + activePlayers.size());
            
            BidDecision decision = strategy.makeBidDecision(currentPlayer, 
                                                             round.getTableProperties(),
                                                             round.getCurrentHighestBid(),
                                                             players);
            
            if (decision.isBid()) {
                round.handleBid(currentPlayer, decision.getAmount());
                System.out.println(currentPlayer.getName() + " BIDS $" + String.format("%.0f", decision.getAmount()));
            } else {
                round.handlePass(currentPlayer);
                System.out.println(currentPlayer.getName() + " PASSES");
            }
        }
        
        round.completeRound();
        displayGameState();
    }

    public void play() {
        System.out.println("===== BUY AND SELL GAME =====\n");
        
        for (int i = 0; i < 5 && !availableProperties.isEmpty(); i++) {
            playRound();
            if (i < 4) {
                System.out.println("\nPress Enter to continue to next round...");
                scanner.nextLine();
            }
        }
        
        displayGameSummary();
    }

    private void displayGameState() {
        System.out.println("\n--- GAME STATE ---");
        
        // Display user's information with property values
        Player user = players.get(0);
        System.out.println(user + ", Property Value: $" + user.getPropertyValue());
        if (!user.getProperties().isEmpty()) {
            System.out.println("  Your properties:");
            for (Property p : user.getProperties()) {
                System.out.println("    - " + p.toStringWithValue());
            }
        }
        
        // Display other players' info without property values
        for (int i = 1; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.println(p);
        }
        
        System.out.println("------------------");
    }

    private void displayGameSummary() {
        System.out.println("\n===== GAME OVER =====");
        System.out.println("\nFinal Standings:");
        
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((a, b) -> Integer.compare(b.getProperties().size(), a.getProperties().size()));
        
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player p = sortedPlayers.get(i);
            String info = (i + 1) + ". " + p.getName() + " - Properties: " + p.getProperties().size() + 
                         ", Balance: $" + String.format("%.0f", p.getBalance());
            if (p.getName().equals("You")) {
                info += ", Property Value: $" + p.getPropertyValue();
            }
            System.out.println(info);
        }
        
        System.out.println("\n");
        activityLog.display();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
