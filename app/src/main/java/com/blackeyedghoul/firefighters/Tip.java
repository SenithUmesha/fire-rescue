package com.blackeyedghoul.firefighters;

public class Tip {
    private String topic;
    private String sub_topic;

    public Tip(String topic, String sub_topic) {
        this.topic = topic;
        this.sub_topic = sub_topic;
    }

    public String getTopic() {
        return topic;
    }

    public String getSub_topic() {
        return sub_topic;
    }
}
