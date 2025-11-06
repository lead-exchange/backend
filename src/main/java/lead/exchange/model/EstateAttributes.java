package lead.exchange.model;

import java.util.List;
import java.util.Objects;

public class EstateAttributes {
    // TODO: это примеры полей, конкретные поля нужно указать при реализации логики /create для объекта
    private String title;
    private String description;
    private String address;
    private Double price;
    private Integer area;
    private Integer bedrooms;
    private List<String> photos;

    public EstateAttributes() {
    }

    public EstateAttributes(String title, String description, String address, Double price,
                            Integer area, Integer bedrooms, List<String> photos) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.price = price;
        this.area = area;
        this.bedrooms = bedrooms;
        this.photos = photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EstateAttributes that = (EstateAttributes) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address) &&
                Objects.equals(price, that.price) &&
                Objects.equals(area, that.area) &&
                Objects.equals(bedrooms, that.bedrooms) &&
                Objects.equals(photos, that.photos);
    }

    @Override
    public String toString() {
        return "EstateAttributes{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", bedrooms=" + bedrooms +
                ", photos=" + photos +
                '}';
    }
}
