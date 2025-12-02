package lead.exchange.samolet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "topnlab")
public interface TopnlabApi {

    @RequestMapping(method = RequestMethod.GET, value = "/public/get-entities")
    RealtyEstateApiModel getRealtyEstateIds(
        @RequestParam("id") Long id,
        @RequestParam("key") String key,
        @RequestParam("type") String type,
        @RequestParam("short") Integer shortParam
    );

}
