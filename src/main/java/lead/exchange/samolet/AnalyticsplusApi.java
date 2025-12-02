package lead.exchange.samolet;

import java.net.SocketTimeoutException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "analyticsplus", configuration = BearerConfiguration.class)
public interface AnalyticsplusApi {

    @Retryable(
        value = { SocketTimeoutException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000L, multiplier = 2)
    )
    @RequestMapping(method = RequestMethod.GET, value = "/users/api/realty/by-phone")
    EstatesIdByRealtyApiModel getRealtyIdsByPhone(@RequestParam("phone") String phone);

}
