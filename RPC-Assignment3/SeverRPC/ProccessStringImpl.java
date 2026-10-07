
import java.util.Date;

public class ProccessStringImpl implements ProccessString {
    public String timeOrUpper(String message) {
        if (message.equalsIgnoreCase("time")) {
            return new Date().toString();
        } else {
            return message.toUpperCase();
        }
    }
}
