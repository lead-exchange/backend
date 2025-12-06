package lead.exchange.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lead.exchange.security.dto.Parsed;
import lead.exchange.security.models.TelegramChat;
import lead.exchange.security.models.TelegramUser;

public class TelegramInitDataValidator {

    private static final String AUTH_SCHEME = "tma";
    private static final String HMAC_ALG = "HmacSHA256";
    private static final String WEB_APP_DATA = "WebAppData";

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    private TelegramInitDataValidator() {}

    public static boolean hasTmaScheme(String authorization) {
        if (authorization == null) {
            return false;
        }
        int i = authorization.indexOf(' ');
        String scheme = i > 0 ? authorization.substring(0, i) : authorization;
        return AUTH_SCHEME.equalsIgnoreCase(scheme);
    }

    public static String extractInitDataRaw(String authorization) {
        int i = authorization.indexOf(' ');
        if (i < 0 || i + 1 >= authorization.length()) {
            return null;
        }
        return authorization.substring(i + 1);
    }

    public static Parsed parseInitData(String initDataRaw) throws Exception {
        Map<String, String> params = parseQueryString(initDataRaw);
        String hashStr = "hash";

        String hash = params.get(hashStr);
        if (hash == null) {
            throw new IllegalArgumentException("Missing 'hash' in initData");
        }

        Map<String, String> sorted = new TreeMap<>(params);
        sorted.remove(hashStr);

        List<String> pairs = sorted.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.toList());

        String dataCheckString = String.join("\n", pairs);

        TelegramUser user = extractUser(params);
        TelegramChat chat = extractChat(params);

        return new Parsed(hash, dataCheckString, params, user, chat);
    }

    public static boolean validateHmac(String initDataRaw, String botToken) throws Exception {
        Parsed parsed = parseInitData(initDataRaw);

        Mac mac1 = Mac.getInstance(HMAC_ALG);
        mac1.init(new SecretKeySpec(WEB_APP_DATA.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
        byte[] secret = mac1.doFinal(botToken.getBytes(StandardCharsets.UTF_8));

        Mac mac2 = Mac.getInstance(HMAC_ALG);
        mac2.init(new SecretKeySpec(secret, HMAC_ALG));
        byte[] sig = mac2.doFinal(parsed.dataCheckString().getBytes(StandardCharsets.UTF_8));
        String calculatedHex = toHex(sig);

        return calculatedHex.equalsIgnoreCase(parsed.hash());
    }

    public static boolean isAuthDateFresh(Map<String, String> params, long maxAgeSeconds, long nowEpochSeconds) {
        String authDate = params.get("auth_date");

        if (authDate == null) {
            return false;
        }

        long auth = Long.parseLong(authDate);
        return (nowEpochSeconds - auth) <= maxAgeSeconds;
    }

    public static TelegramUser extractUser(Map<String, String> params) throws Exception {
        String userJson = params.get("user");

        if (userJson == null) {
            return null;
        }

        return MAPPER.readValue(userJson, TelegramUser.class);
    }

    public static TelegramChat extractChat(Map<String, String> params) throws Exception {
        String chatJson = params.get("chat");

        if (chatJson == null) {
            return null;
        }

        return MAPPER.readValue(chatJson, TelegramChat.class);
    }

    private static Map<String, String> parseQueryString(String qs) throws Exception {
        Map<String, String> out = new HashMap<>();
        String[] pairs = qs.split("&");

        for (String p : pairs) {
            int idx = p.indexOf('=');

            String key = idx >= 0 ? p.substring(0, idx) : p;
            String val = idx >= 0 ? p.substring(idx + 1) : "";

            key = URLDecoder.decode(key, StandardCharsets.UTF_8);
            val = URLDecoder.decode(val, StandardCharsets.UTF_8);

            out.put(key, val);
        }

        return out;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            String s = Integer.toHexString(b & 0xff);
            if (s.length() == 1) {
                sb.append('0');
            }
            sb.append(s);
        }

        return sb.toString();
    }
}
