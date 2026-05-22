import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Phase2Round {
    private final int roundNumber;
    private final List<Player> players;
    private final CheckSet checks;
    private final ActivityLog activityLog;

    public Phase2Round(int roundNumber, List<Player> players, CheckSet checks, ActivityLog activityLog) {
        this.roundNumber = roundNumber;
        this.players = players;
        this.checks = checks;
        this.activityLog = activityLog;
    }

    public void play(Scanner scanner) {
        System.out.println("\n========== PHASE II - ROUND " + roundNumber + " ==========");
        activityLog.log("========== PHASE II - ROUND " + roundNumber + " ==========");
        
        System.out.println("Available checks: " + checks);
        activityLog.log("Available checks: " + checks);

        List<PropertyOffer> offers = new ArrayList<>();

        // Each player offers a property
        for (Player player : players) {
            Property offered = getPlayerProperty(player, scanner);
            if (offered != null) {
                offers.add(new PropertyOffer(player, offered));
                System.out.println(player.getName() + " offers Property " + offered.getId());
                activityLog.log(player.getName() + " offers Property " + offered.getId());
            } else {
                System.out.println(player.getName() + " has no properties to offer!");
                activityLog.log(player.getName() + " has no properties to offer!");
            }
        }

        // Sort offers by property value (highest first)
        offers.sort((a, b) -> Integer.compare(b.getProperty().getId(), a.getProperty().getId()));

        // Distribute checks to properties
        for (PropertyOffer offer : offers) {
            Integer check = checks.removeHighest();
            offer.getPlayer().removeProperty(offer.getProperty());
            offer.getPlayer().addChecks(check);
            
            System.out.println(offer.getPlayer().getName() + " (Property " + offer.getProperty().getId() + 
                             ") receives $" + check);
            activityLog.log(offer.getPlayer().getName() + " (Property " + offer.getProperty().getId() + 
                           ") receives $" + check);
        }

        System.out.println("\n--- END OF PHASE II ROUND " + roundNumber + " ---");
        displayGameState();
    }

    private Property getPlayerProperty(Player player, Scanner scanner) {
        if (player.getProperties().isEmpty()) {
            return null;
        }

        if (player.getName().equals("You")) {
            // User chooses property
            while (true) {
                System.out.println("\nYour properties: " + 
                                 player.getProperties().size() + " properties");
                for (Property p : player.getProperties()) {
                    System.out.println("  - Property " + p.getId());
                }
                System.out.print("Choose property ID to offer: ");
                
                try {
                    int propId = Integer.parseInt(scanner.nextLine().trim());
                    for (Property p : player.getProperties()) {
                        if (p.getId() == propId) {
                            return p;
                        }
                    }
                    System.out.println("Invalid property ID. Try again.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a property ID.");
                }
            }
        } else {
            // Bot chooses random property
            List<Property> props = player.getProperties();
            return props.get((int) (Math.random() * props.size()));
        }
    }

    private void displayGameState() {
        System.out.println("\n--- PHASE II GAME STATE ---");
        
        Player user = players.get(0);
        System.out.println(user + ", Checks: $" + user.getChecksEarned());
        if (!user.getProperties().isEmpty()) {
            System.out.println("  Your remaining properties:");
            for (Property p : user.getProperties()) {
                System.out.println("    - Property " + p.getId());
            }
        }
        
        for (int i = 1; i < players.size(); i++) {
            Player p = players.get(i);
            System.out.println(p + ", Checks: $" + p.getChecksEarned());
        }
        
        System.out.println("------------------");
    }

    static class PropertyOffer {
        private final Player player;
        private final Property property;

        PropertyOffer(Player player, Property property) {
            this.player = player;
            this.property = property;
        }

        Player getPlayer() {
            return player;
        }

        Property getProperty() {
            return property;
        }
    }
}
