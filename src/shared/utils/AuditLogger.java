
package shared.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private static final String LOG_FILE = "src/database/audit_log.txt";
    
    public enum ActionType{
        ADD_ITEM,
        DELETE_ITEM,
        EDIT_ITEM,
        ADD_SUPPLIER,
        EDIT_SUPPLIER,
        DELETE_SUPPLIER,
        ADD_SALES,
        EDIT_SALES,
        DELETE_SALES,
        ADD_PR,
        EDIT_PR,
        DELETE_PR,
    }

    public static void log(String userId, String username, ActionType action, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = String.format("[%s] [%s - %s] Action: %s - %s", 
            timestamp, userId, username, action.name(), details);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        }
    }

}
