package lead.exchange.model;

import java.util.List;
import lead.exchange.samolet.RealtyEstateApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class EstateAttributes {

    private final RealtyEstateApiModel.Address address;
    private final Object animals;
    private final Double areaCommon;
    private final Double areaCorridor;
    private final Double areaHallway;
    private final Double areaKitchen;
    private final Double areaLand;
    private final Integer areaLandType;
    private final Double areaLiving;
    private final String areaRoom;
    private final Object areaSecurity;
    private final Object basement;
    private final Object bath;
    private final List<Long> bathroom;
    private final Long bedsCount;
    private final Object building;
    private final Integer buildingType;
    private final Integer builtYear;
    private final Integer canCalculateCommission;
    private final Boolean commissionOwnerPaysToMe;
    private final String commissionOwnerPaysToMeValue;
    private final Integer commissionOwnerPaysToMeValueSum;
    private final Double communal;
    private final Object conditioner;
    private final String externalName;
    private final String externalType;
    private final String flat;
    private final Integer flatType;
    private final Integer floor;
    private final Integer floors;
    private final Integer floorsFlat;
    private final Object furniture;
    private final Object garageType;
    private final Boolean garbageChute;
    private final Object gasLocation;
    private final Integer gaz;
    private final Boolean hasDishwasher;
    private final Boolean hasElectricity;
    private final Boolean hasEquipment;
    private final Integer heating;
    private final Object hotWater;
    private final String house;
    private final Object houseSerial;
    private final Object houseType;
    private final Object icebox;
    private final Long id;
    private final Object internet;
    private final Integer intersections;
    private final Integer intersectionsNew;
    private final Boolean isArchiveActive;
    private final Boolean isAuction;
    private final Boolean isDeposit;
    private final Boolean isFeed;
    private final Boolean isFirstSale;
    private final Boolean isInHiddenCianBase;
    private final Boolean isMansard;
    private final Boolean isNewHouse;
    private final Object metro;
    private final Object metroIds;
    private final Object minAreaCommon;
    private final Object monthlyIncome;
    private final Integer monthlyIncomeType;
    private final Boolean mortgage;
    private final String mydescription;
    private final Object parking;
    private final Object parkingPrice;
    private final Integer partSize;
    private final Integer passStatus;
    private final List<RealtyEstateApiModel.Photo> photos;
    private final Object placeId;
    private final Object placeName;
    private final Object placeType;
    private final Integer portfolioCommission;
    private final Integer prepayment;
    private final Integer price;
    private final Integer pricePerMeter;
    private final String realtyType;
    private final String regionId;
    private final String regionName;
    private final String regionType;
    private final Integer roofHeight;
    private final Integer rooms;
    private final String saleType;
    private final Object sauna;
    private final Object streetId;
    private final Object streetName;
    private final Object streetType;
    private final Object task;
    private final Integer totalCommissionMls;
    private final Object tv;
    private final Object typeLand;
    private final Object video;
    private final Object wallMaterial;
    private final Object washer;
    private final Object water;
    private final Object wc;
    private final Integer windowView;
    private final Object withOwner;
    private final Object workStartDeadline;

    @RequiredArgsConstructor
    @Data
    public class Address {

        private final Double latitude;
        private final Double longitude;
        private final String coordinates;
        private final String regionName;
        private final String regionId;
        private final String regionType;
        private final Object countyName;
        private final Object countyId;
        private final Object countyType;
        private final Object cityName;
        private final Object cityId;
        private final Object cityType;
        private final Object placeName;
        private final Object placeId;
        private final Object placeType;
        private final String externalName;
        private final String externalId;
        private final String externalType;
        private final Object districtName;
        private final Object districtId;
        private final Object districtType;
        private final Object streetName;
        private final Object streetId;
        private final Object streetType;
        private final String house;
        private final Object corpus;
        private final Object litera;
        private final Object building;
        private final Object avitoCityId;
        private final Object metro;
        private final String flat;
        private final Object directionId;
        private final Object directionName;
        private final Object directionType;
        private final Object cityDistance;
        private final Object complexName;
        private final Object office;
        private final Object folkDistrictName;
        private final Object folkDistrictId;
        private final Object folkDistrictType;
        private final Object zipCode;
        private final Object regionDistrictName;
        private final Object regionDistrictId;
        private final Object regionDistrictType;
        private final Object countryName;
        private final Object countryId;
        private final Object countryType;
        private final Object coordsSetManually;
        private final Object districtSetManually;
    }

    @RequiredArgsConstructor
    @Data
    public class Photo {

        private final String big;
        private final String small;
    }
}
