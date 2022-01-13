package com.blackeyedghoul.firefighters;

public class Station {
    private String name;
    private String address;
    private String phone_number;
    private int total_fighters;
    private int total_vehicles;
    private String lat;
    private String lon;

    public Station(String name, String address, String phone_number, int total_fighters, int total_vehicles, String lat, String lon) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.total_fighters = total_fighters;
        this.total_vehicles = total_vehicles;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getTotal_fighters() {
        return total_fighters;
    }

    public int getTotal_vehicles() {
        return total_vehicles;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
