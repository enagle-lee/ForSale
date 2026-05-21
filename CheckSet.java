import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CheckSet {
    private final List<Integer> checks;

    public CheckSet(List<Integer> checkValues) {
        this.checks = new ArrayList<>(checkValues);
    }

    public List<Integer> getChecks() {
        return new ArrayList<>(checks);
    }

    public Integer removeHighest() {
        int highest = checks.get(0);
        for (int check : checks) {
            if (check > highest) {
                highest = check;
            }
        }
        checks.remove((Integer) highest);
        return highest;
    }

    public static CheckSet createRound1() {
        List<Integer> checks = new ArrayList<>();
        checks.add(7000);
        checks.add(6000);
        checks.add(5000);
        checks.add(0);
        Collections.sort(checks, Collections.reverseOrder());
        return new CheckSet(checks);
    }

    public static CheckSet createRandomRound() {
        List<Integer> checks = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 4; i++) {
            int check = (random.nextInt(11)) * 1000;
            checks.add(check);
        }
        
        Collections.sort(checks, Collections.reverseOrder());
        return new CheckSet(checks);
    }

    @Override
    public String toString() {
        return checks.toString();
    }
}
