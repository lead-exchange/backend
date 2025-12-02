package lead.exchange.samolet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RealtyEstateApiModel {

    private Address address;
    private Object animals;

    @JsonProperty("ad_title")
    private String title;
    @JsonProperty("area_common")
    private Double areaCommon;

    @JsonProperty("area_corridor")
    private Double areaCorridor;

    @JsonProperty("area_hallway")
    private Double areaHallway;

    @JsonProperty("area_kitchen")
    private Double areaKitchen;

    @JsonProperty("area_land")
    private Double areaLand;

    @JsonProperty("area_land_type")
    private Integer areaLandType;

    @JsonProperty("area_living")
    private Double areaLiving;

    @JsonProperty("area_room")
    private String areaRoom;

    @JsonProperty("area_security")
    private Object areaSecurity;
    private Object basement;
    private Object bath;
    private List<Long> bathroom;

    @JsonProperty("beds_count")
    private Long bedsCount;
    private Object building;

    @JsonProperty("building_type")
    private Integer buildingType;

    @JsonProperty("built_year")
    private Integer builtYear;

    @JsonProperty("can_calculate_commission")
    private Integer canCalculateCommission;

    @JsonProperty("commission_owner_pays_to_me")
    private Boolean commissionOwnerPaysToMe;

    @JsonProperty("commission_owner_pays_to_me_value")
    private String commissionOwnerPaysToMeValue;

    @JsonProperty("commission_owner_pays_to_me_value_sum")
    private Integer commissionOwnerPaysToMeValueSum;
    private Double communal;
    private Object conditioner;

    @JsonProperty("external_name")
    private String externalName;

    @JsonProperty("external_type")
    private String externalType;
    private String flat;

    @JsonProperty("flat_type")
    private Integer flatType;
    private Integer floor;
    private Integer floors;

    @JsonProperty("floors_flat")
    private Integer floorsFlat;
    private Object furniture;

    @JsonProperty("garage_type")
    private Object garageType;

    @JsonProperty("garbage_chute")
    private Boolean garbageChute;

    @JsonProperty("gas_location")
    private Object gasLocation;
    private Integer gaz;

    @JsonProperty("has_dishwasher")
    private Boolean hasDishwasher;

    @JsonProperty("has_electricity")
    private Boolean hasElectricity;

    @JsonProperty("has_equipment")
    private Boolean hasEquipment;
    private Integer heating;

    @JsonProperty("hot_water")
    private Object hotWater;
    private String house;

    @JsonProperty("house_serial")
    private Object houseSerial;

    @JsonProperty("house_type")
    private Object houseType;
    private Object icebox;
    private Long id;
    private Object internet;
    private Integer intersections;

    @JsonProperty("intersections_new")
    private Integer intersectionsNew;

    @JsonProperty("is_archive_active")
    private Boolean isArchiveActive;

    @JsonProperty("is_auction")
    private Boolean isAuction;

    @JsonProperty("is_deposit")
    private Boolean isDeposit;

    @JsonProperty("is_feed")
    private Boolean isFeed;

    @JsonProperty("is_first_sale")
    private Boolean isFirstSale;

    @JsonProperty("is_in_hidden_cian_base")
    private Boolean isInHiddenCianBase;

    @JsonProperty("is_mansard")
    private Boolean isMansard;

    @JsonProperty("is_new_house")
    private Boolean isNewHouse;
    private Object metro;

    @JsonProperty("metro_ids")
    private Object metroIds;

    @JsonProperty("min_area_common")
    private Object minAreaCommon;

    @JsonProperty("monthly_income")
    private Object monthlyIncome;

    @JsonProperty("monthly_income_type")
    private Integer monthlyIncomeType;
    private Boolean mortgage;
    @JsonProperty("my_description")
    private String description;
    private Object parking;

    @JsonProperty("parking_price")
    private Object parkingPrice;

    @JsonProperty("part_size")
    private Integer partSize;

    @JsonProperty("pass_status")
    private Integer passStatus;

    @JsonInclude(Include.NON_NULL)
    private List<Photo> photos;

    @JsonProperty("place_id")
    private Object placeId;

    @JsonProperty("place_name")
    private Object placeName;

    @JsonProperty("place_type")
    private Object placeType;

    @JsonProperty("portfolio_commission")
    private Integer portfolioCommission;
    private Integer prepayment;
    private Integer price;

    @JsonProperty("price_per_meter")
    private Integer pricePerMeter;

    @JsonProperty("realty_type")
    private String realtyType;

    @JsonProperty("region_id")
    private String regionId;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("region_type")
    private String regionType;

    @JsonProperty("roof_height")
    private Integer roofHeight;
    private Integer rooms;

    @JsonProperty("sale_type")
    private String saleType;
    private Object sauna;

    @JsonProperty("street_id")
    private Object streetId;

    @JsonProperty("street_name")
    private Object streetName;

    @JsonProperty("street_type")
    private Object streetType;
    private Object task;

    @JsonProperty("total_commission_mls")
    private Integer totalCommissionMls;
    private Object tv;

    @JsonProperty("type_land")
    private Object typeLand;
    private Object video;

    @JsonProperty("wall_material")
    private Object wallMaterial;
    private Object washer;
    private Object water;
    private Object wc;

    @JsonProperty("window_view")
    private Integer windowView;

    @JsonProperty("with_owner")
    private Object withOwner;

    @JsonProperty("work_start_deadline")
    private Object workStartDeadline;

    @RequiredArgsConstructor
    @Data
    public static class Address {

        private Double latitude;
        private Double longitude;
        private String coordinates;

        @JsonProperty("region_name")
        private String regionName;

        @JsonProperty("region_id")
        private String regionId;

        @JsonProperty("region_type")
        private String regionType;

        @JsonProperty("county_name")
        private Object countyName;

        @JsonProperty("county_id")
        private Object countyId;

        @JsonProperty("county_type")
        private Object countyType;

        @JsonProperty("city_name")
        private Object cityName;

        @JsonProperty("city_id")
        private Object cityId;

        @JsonProperty("city_type")
        private Object cityType;

        @JsonProperty("place_name")
        private Object placeName;

        @JsonProperty("place_id")
        private Object placeId;

        @JsonProperty("place_type")
        private Object placeType;

        @JsonProperty("external_name")
        private String externalName;

        @JsonProperty("external_id")
        private String externalId;

        @JsonProperty("external_type")
        private String externalType;

        @JsonProperty("district_name")
        private Object districtName;

        @JsonProperty("district_id")
        private Object districtId;

        @JsonProperty("district_type")
        private Object districtType;

        @JsonProperty("street_name")
        private Object streetName;

        @JsonProperty("street_id")
        private Object streetId;

        @JsonProperty("street_type")
        private Object streetType;
        private String house;
        private Object corpus;
        private Object litera;
        private Object building;

        @JsonProperty("avito_city_id")
        private Object avitoCityId;
        private Object metro;
        private String flat;

        @JsonProperty("direction_id")
        private Object directionId;

        @JsonProperty("direction_name")
        private Object directionName;

        @JsonProperty("direction_type")
        private Object directionType;

        @JsonProperty("city_distance")
        private Object cityDistance;

        @JsonProperty("complex_name")
        private Object complexName;
        private Object office;

        @JsonProperty("folk_district_name")
        private Object folkDistrictName;

        @JsonProperty("folk_district_id")
        private Object folkDistrictId;

        @JsonProperty("folk_district_type")
        private Object folkDistrictType;

        @JsonProperty("zip_code")
        private Object zipCode;

        @JsonProperty("region_district_name")
        private Object regionDistrictName;

        @JsonProperty("region_district_id")
        private Object regionDistrictId;

        @JsonProperty("region_district_type")
        private Object regionDistrictType;

        @JsonProperty("country_name")
        private Object countryName;

        @JsonProperty("country_id")
        private Object countryId;

        @JsonProperty("country_type")
        private Object countryType;

        @JsonProperty("coords_set_manually")
        private Object coordsSetManually;

        @JsonProperty("district_set_manually")
        private Object districtSetManually;
    }

    @Data
    @NoArgsConstructor
    public static class Photo {

        private String big;
        private String small;
    }
}
