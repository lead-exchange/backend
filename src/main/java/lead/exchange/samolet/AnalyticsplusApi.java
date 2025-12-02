package lead.exchange.samolet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "analyticsplus", configuration = BearerConfiguration.class)
public interface AnalyticsplusApi {

    @RequestMapping(method = RequestMethod.GET, value = "/users/api/realty/by-phone")
    EstatesIdByRealtyApiModel getRealtyIdsByPhone(@RequestParam("phone") String phone);

}
