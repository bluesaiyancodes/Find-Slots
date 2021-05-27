package com.strongties.app.findslot.subclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class VSession implements Serializable {
    String session_id;
    String date;
    String avl_capacity;
    String min_age_limit;
    String vac_type;
    String slots;
    String avl_dose1, avl_dose2;

    public VSession() {
    }

    public VSession(String session_id, String date, String avl_capacity, String min_age_limit, String vac_type, String slots, String avl_dose1, String avl_dose2) {
        this.session_id = session_id;
        this.date = date;
        this.avl_capacity = avl_capacity;
        this.min_age_limit = min_age_limit;
        this.vac_type = vac_type;
        this.slots = slots;
        this.avl_dose1 = avl_dose1;
        this.avl_dose2 = avl_dose2;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvl_capacity() {
        return avl_capacity;
    }

    public void setAvl_capacity(String avl_capacity) {
        this.avl_capacity = avl_capacity;
    }

    public String getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(String min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVac_type() {
        return vac_type;
    }

    public void setVac_type(String vac_type) {
        this.vac_type = vac_type;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getAvl_dose1() {
        return avl_dose1;
    }

    public void setAvl_dose1(String avl_dose1) {
        this.avl_dose1 = avl_dose1;
    }

    public String getAvl_dose2() {
        return avl_dose2;
    }

    public void setAvl_dose2(String avl_dose2) {
        this.avl_dose2 = avl_dose2;
    }


    @Override
    public String toString() {
        return "classVSession{" +
                "session_id='" + session_id + '\'' +
                ", date='" + date + '\'' +
                ", avl_capacity='" + avl_capacity + '\'' +
                ", min_age_limit='" + min_age_limit + '\'' +
                ", vac_type='" + vac_type + '\'' +
                ", slots=" + slots +
                ", avl_dose1='" + avl_dose1 + '\'' +
                ", avl_dose2='" + avl_dose2 + '\'' +
                '}';
    }
}
