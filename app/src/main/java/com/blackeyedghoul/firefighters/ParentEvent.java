package com.blackeyedghoul.firefighters;

public class ParentEvent {

    String event_date_month;
    ChildEvent childEvent;

    public ParentEvent() {
    }

    public ParentEvent(String event_date_month, ChildEvent childEvent) {
        this.event_date_month = event_date_month;
        this.childEvent = childEvent;
    }

    public String getEvent_date_month() {
        return event_date_month;
    }

    public ChildEvent getChildEvent() {
        return childEvent;
    }
}
