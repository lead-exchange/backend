package lead.exchange.security.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramChat {
    public Long id;
    public String type;
    public String title;
    @JsonProperty("photo_url")
    public String photoUrl;
    public String username;
}
