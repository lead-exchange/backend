package lead.exchange.samolet;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "topnlab")
public interface TopnlabApi {

    @Retryable(
        value = {SocketTimeoutException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000L, multiplier = 2)
    )
    @RequestMapping(method = RequestMethod.GET, value = "/public/get-entities")
    @RateLimiter(name = "topnlabApieRateLimiter")
    Map<Object, RealtyEstateApiModel> getRealtyEstateIds(
        @RequestParam("id") Long id,
        @RequestParam("key") String key,
        @RequestParam("type") String type,
        @RequestParam("short") Integer shortParam
    );

}
