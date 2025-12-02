package lead.exchange.security;

import java.util.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PathMatcher {
    private final List<String> prefixes;

    boolean matchesAny(String path) {
        if (path == null) {
            return false;
        }

        for (String p : prefixes) {
            String prefix = p.trim();
            if (prefix.isEmpty()) {
                continue;
            }
            String normalized = prefix.replace("/**", "");
            if (normalized.isEmpty()) {
                continue;
            }
            if (path.startsWith(normalized)) {
                return true;
            }
        }
        return false;
    }
}
