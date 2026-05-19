import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityLog {
    private final List<LogEntry> entries = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    static class LogEntry {
        final LocalDateTime timestamp;
        final String message;

        LogEntry(LocalDateTime timestamp, String message) {
            this.timestamp = timestamp;
            this.message = message;
        }
    }

    public void log(String message) {
        entries.add(new LogEntry(LocalDateTime.now(), message));
    }

    public void display() {
        System.out.println("\n=== ACTIVITY LOG ===");
        for (LogEntry entry : entries) {
            System.out.println("[" + entry.timestamp.format(formatter) + "] " + entry.message);
        }
        System.out.println("====================\n");
    }

    public List<String> getLog() {
        List<String> log = new ArrayList<>();
        for (LogEntry entry : entries) {
            log.add("[" + entry.timestamp.format(formatter) + "] " + entry.message);
        }
        return log;
    }
}
