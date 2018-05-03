package com.jundger.carservice.bean;

/**
 * Title: CarService
 * Date: Create in 2018/5/3 16:41
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class SiteLocation {

    private String id;

    private String name;

    private String address;

    private Double longitude;

    private Double latitude;

    public SiteLocation(String id, String name, String address, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
