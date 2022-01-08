package com.blackeyedghoul.firefighters;

public class Contacts {
    private int id;
    private String name;
    private String position;
    private String station;
    private String phone_number;
    private String email;

    public Contacts(int id, String name, String position, String station, String phone_number, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.station = station;
        this.phone_number = phone_number;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getStation() {
        return station;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }
}
