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
    private Player roundStartPlayer;

    public Game() {
        this.players = new ArrayList<>();
        this.activityLog = new ActivityLog();
        this.strategies = new BidStrategy[4];
        this.currentRoundNumber = 0;
        this.availableProperties = new ArrayList<>();
        this.roundStartPlayer = null;
        
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
        
        roundStartPlayer = user;
        
        activityLog.log("Game started! All players begin with $18,000");
        activityLog.log("Players: " + String.join(", ", players.stream().map(Player::getName).toArray(String[]::new)));
    }

    public void playPhase1() {
        System.out.println("\n\n===== PHASE I: BIDDING =====\n");
        activityLog.log("\n===== PHASE I: BIDDING =====");
        
        for (int i = 0; i < 5 && !availableProperties.isEmpty(); i++) {
            playRound();
            if (i < 4) {
                System.out.println("\nPress Enter to continue to next round...");
                scanner.nextLine();
            }
        }
    }

    public void playPhase2() {
        System.out.println("\n\n===== PHASE II: SELLING =====\n");
        System.out.println("Phase I Complete! All players carry over their properties and balance.");
        System.out.println("Press Enter to begin Phase II (Selling)...");
        scanner.nextLine();
        
        activityLog.log("\n===== PHASE II: SELLING =====");
        
        for (int round = 1; round <= 5; round++) {
            CheckSet checks;
            if (round == 1) {
                checks = CheckSet.createRound1();
            } else {
                checks = CheckSet.createRandomRound();
            }
            
            Phase2Round phase2Round = new Phase2Round(round, players, checks, activityLog);
            phase2Round.play(scanner);
            
            if (round < 5) {
                System.out.println("\nPress Enter to continue to next round...");
                scanner.nextLine();
            }
        }
    }

    private void playRound() {
        currentRoundNumber++;
        System.out.println("\n========== ROUND " + currentRoundNumber + " ==========");
        activityLog.log("========== ROUND " + currentRoundNumber + " ==========");
        
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
        
        List<Player> activePlayers;
        int playerIndex = players.indexOf(roundStartPlayer);
        
        while (round.getActivePlayers().size() > 1) {
            activePlayers = round.getActivePlayers();
            if (activePlayers.isEmpty()) break;
            
            boolean foundPlayer = false;
            for (int i = 0; i < players.size(); i++) {
                int currentPlayerIdx = (playerIndex + i) % players.size();
                Player candidate = players.get(currentPlayerIdx);
                
                if (activePlayers.contains(candidate)) {
                    playerIndex = (currentPlayerIdx + 1) % players.size();
                    foundPlayer = true;
                    
                    int strategyIndex = currentPlayerIdx;
                    BidStrategy strategy = strategies[strategyIndex];
                    
                    BidDecision decision = strategy.makeBidDecision(candidate, round.getTableProperties(),
                                                                     round.getCurrentHighestBid(), players);
                    
                    if (decision.isBid()) {
                        round.handleBid(candidate, decision.getAmount());
                        System.out.println(candidate.getName() + " BIDS $" + String.format("%.0f", decision.getAmount()));
                    } else {
                        round.handlePass(candidate);
                        System.out.println(candidate.getName() + " PASSES");
                    }
                    break;
                }
            }
            
            if (!foundPlayer) break;
        }
        
        round.completeRound();
        
        if (round.getRoundWinner() != null) {
            roundStartPlayer = round.getRoundWinner();
        }
        
        displayGameState();
    }

    public void play() {
        System.out.println("===== BUY AND SELL GAME =====\n");
        
        playPhase1();
        playPhase2();
        
        displayFinalStandings();
    }

    private void displayGameState() {
        System.out.println("\n--- GAME STATE ---");
        
        Player user = players.get(0);
        System.out.println(user);
        if (!user.getProperties().isEmpty()) {
            System.out.println("  Your properties:");
            for (Property p : user.getProperties()) {
                System.out.println("    - " + p);
            }
        }
        
        for (int i = 1; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.println(p);
        }
        
        System.out.println("------------------");
    }

    private void displayFinalStandings() {
        System.out.println("\n\n===== GAME OVER =====");
        System.out.println("\nFinal Standings:\n");
        
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((a, b) -> Double.compare(b.getTotalMoney(), a.getTotalMoney()));
        
        String[] ordinals = {"1st", "2nd", "3rd", "4th"};
        
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player p = sortedPlayers.get(i);
            System.out.println(emoji + ordinals[i] + " - " + p.getName() +             String emoji = (i == 0) ? "
                             ": $" + String.format("%.0f", p.getBalance()) + 
                             " (balance) + $" + String.format("%.0f", p.getChecksEarned()) + 
                             " (checks) = $" + String.format("%.0f", p.getTotalMoney()) + 
                             " (total)");
        }
        
        System.out.println("\n" + sortedPlayers.get(0).getName().toUpperCase() + " WINS!\n");
        activityLog.display();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
