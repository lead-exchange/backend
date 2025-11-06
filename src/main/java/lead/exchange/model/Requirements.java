package lead.exchange.model;

import java.util.List;
import java.util.Objects;

public class Requirements {
    // TODO: это примеры полей, конкретные поля нужно указать при реализации логики /create для лида
    private String propertyType;
    private Double minPrice;
    private Double maxPrice;
    private Integer minArea;
    private Integer maxArea;
    private List<String> locations;
    private Integer bedrooms;

    public Requirements() {
    }

    public Requirements(String propertyType, Double minPrice, Double maxPrice, Integer minArea,
                        Integer maxArea, List<String> locations, Integer bedrooms) {
        this.propertyType = propertyType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.locations = locations;
        this.bedrooms = bedrooms;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinArea() {
        return minArea;
    }

    public void setMinArea(Integer minArea) {
        this.minArea = minArea;
    }

    public Integer getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(Integer maxArea) {
        this.maxArea = maxArea;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requirements that = (Requirements) o;
        return Objects.equals(propertyType, that.propertyType)
                && Objects.equals(minPrice, that.minPrice)
                && Objects.equals(maxPrice, that.maxPrice)
                && Objects.equals(minArea, that.minArea)
                && Objects.equals(maxArea, that.maxArea)
                && Objects.equals(locations, that.locations)
                && Objects.equals(bedrooms, that.bedrooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyType, minPrice, maxPrice, minArea, maxArea, locations, bedrooms);
    }

    @Override
    public String toString() {
        return "Requirements{"
                + "propertyType='" + propertyType + '\''
                + ", minPrice=" + minPrice
                + ", maxPrice=" + maxPrice
                + ", minArea=" + minArea
                + ", maxArea=" + maxArea
                + ", locations=" + locations
                + ", bedrooms=" + bedrooms
                + '}';
    }
}
