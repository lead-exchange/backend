package lead.exchange.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum RenovationType {
    NO_RENOVATION,
    FINISHING,
    NEEDS_REPAIR,
    COSMETIC_REPAIR,
    EURO_REPAIR,

    @JsonEnumDefaultValue
    ANY
}
