package lead.exchange.security.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUser {
    public Long id;
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;
    public String username;
    @JsonProperty("language_code")
    public String languageCode;
    @JsonProperty("is_premium")
    public Boolean isPremium;
    @JsonProperty("is_bot")
    public Boolean isBot;
    @JsonProperty("allows_write_to_pm")
    public Boolean allowsWriteToPm;
    @JsonProperty("photo_url")
    public String photoUrl;
}
