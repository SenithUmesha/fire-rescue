package com.blackeyedghoul.firefighters;

public class ChildEvent {

    String event_name, event_s_time, event_e_time, event_location;

    public ChildEvent() {
    }

    public ChildEvent(String event_name, String event_s_time, String event_e_time, String event_location) {
        this.event_name = event_name;
        this.event_s_time = event_s_time;
        this.event_e_time = event_e_time;
        this.event_location = event_location;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_s_time() {
        return event_s_time;
    }

    public String getEvent_e_time() {
        return event_e_time;
    }

    public String getEvent_location() {
        return event_location;
    }
}
