package lead.exchange.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lead.exchange.exception.ForbiddenException;
import lead.exchange.security.dto.Parsed;
import lead.exchange.service.AuthService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;


public class TelegramAuthFilter extends OncePerRequestFilter {

    private final boolean enabled;
    private final String botToken;
    private final long maxAgeSeconds;
    private final PathMatcher pathMatcher;
    private final AuthService authService;

    public TelegramAuthFilter(
            boolean enabled,
            String botToken,
            long maxAgeSeconds,
            List<String> publicPaths,
            AuthService authService
    ) {
        this.enabled = enabled;
        this.botToken = botToken;
        this.maxAgeSeconds = maxAgeSeconds;
        this.pathMatcher = new PathMatcher(publicPaths);
        this.authService = authService;
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
        if (!enabled) {
            return true;
        }

        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String path = request.getRequestURI();
        return pathMatcher.matchesAny(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (!TelegramInitDataValidator.hasTmaScheme(auth)) {
            throw new ForbiddenException("Missing or invalid Authorization: expected 'tma {initDataRaw}'");
        }

        String initDataRaw = TelegramInitDataValidator.extractInitDataRaw(auth);
        if (initDataRaw == null || initDataRaw.isBlank()) {
            throw new ForbiddenException("Empty initDataRaw");
        }

        try {
            boolean ok = TelegramInitDataValidator.validateHmac(initDataRaw, botToken);
            if (!ok) {
                throw new ForbiddenException("InitData signature invalid");
            }

            Parsed parsed = TelegramInitDataValidator.parseInitData(initDataRaw);

            long now = Instant.now().getEpochSecond();
            if (!TelegramInitDataValidator.isAuthDateFresh(parsed.params(), maxAgeSeconds, now)) {
                throw new ForbiddenException("InitData expired");
            }

            var currentUser = authService.ensureAndLoadCurrentUser(parsed.user(), parsed.chat());
            request.setAttribute("currentUser", currentUser);

            filterChain.doFilter(request, response);
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ForbiddenException("Authorization failed: " + e.getMessage());
        }
    }
}
