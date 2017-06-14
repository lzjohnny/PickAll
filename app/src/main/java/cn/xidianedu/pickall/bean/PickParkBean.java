package cn.xidianedu.pickall.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ShiningForever on 2017/5/6.
 */

public class PickParkBean extends BmobObject {
    private String title;
    private float price;
    private String location;
    private float rating;
    private String description;
    private String label;
    private Integer ratingCount;

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    private String usage;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public PickParkBean(String title, float price, String location) {
        super();
        this.location = location;
        this.price = price;
        this.title = title;
    }

    public PickParkBean() {
        super();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
