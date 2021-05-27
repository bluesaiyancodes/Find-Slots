package com.strongties.app.findslot.subclasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class VCenter implements Serializable {

    String center_id;
    String name;
    String address;
    String state, district, block, pincode;
    String op_time_from, op_time_to;
    String fee_type;
    ArrayList<VSession> sessions;

    public VCenter() {
        this.sessions = new ArrayList<>();
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getOp_time_from() {
        return op_time_from;
    }

    public void setOp_time_from(String op_time_from) {
        this.op_time_from = op_time_from;
    }

    public String getOp_time_to() {
        return op_time_to;
    }

    public void setOp_time_to(String op_time_to) {
        this.op_time_to = op_time_to;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public ArrayList<VSession> getSessions() {
        return sessions;
    }

    public void setSessions(JSONArray sessionArray) {
        for (int i=0; i<sessionArray.length(); i++){
            VSession vSess = new VSession();
            try {
                JSONObject sess = sessionArray.getJSONObject(i);
                vSess.setSession_id(sess.getString("session_id"));
                vSess.setDate(sess.getString("date"));
                vSess.setAvl_capacity(String.valueOf(sess.getInt("available_capacity")));
                vSess.setMin_age_limit(String.valueOf(sess.getInt("min_age_limit")));
                vSess.setVac_type(sess.getString("vaccine"));
                vSess.setSlots(String.valueOf(sess.getJSONArray("slots")));
                vSess.setAvl_dose1(String.valueOf(sess.getInt("available_capacity_dose1")));
                vSess.setAvl_dose2(String.valueOf(sess.getInt("available_capacity_dose2")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.sessions.add(vSess);
        }
    }

    @Override
    public String toString() {
        return "VCenter{" +
                "center_id='" + center_id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", district='" + district + '\'' +
                ", block='" + block + '\'' +
                ", pincode='" + pincode + '\'' +
                ", op_time_from='" + op_time_from + '\'' +
                ", op_time_to='" + op_time_to + '\'' +
                ", fee_type='" + fee_type + '\'' +
                ", sessions=" + sessions +
                '}';
    }

    public VCenter(String center_id, String name, String address, String state, String district, String block, String pincode, String op_time_from, String op_time_to, String fee_type, JSONArray sessionArray) {
        this.center_id = center_id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.district = district;
        this.block = block;
        this.pincode = pincode;
        this.op_time_from = op_time_from;
        this.op_time_to = op_time_to;
        this.fee_type = fee_type;
        this.sessions = new ArrayList<>();
        for (int i=0; i<sessionArray.length(); i++){
            VSession vSess = new VSession();
            try {
                JSONObject sess = sessionArray.getJSONObject(i);
                vSess.setSession_id(sess.getString("session_id"));
                vSess.setDate(sess.getString("date"));
                vSess.setAvl_capacity(String.valueOf(sess.getInt("available_capacity")));
                vSess.setMin_age_limit(String.valueOf(sess.getInt("min_age_limit")));
                vSess.setVac_type(sess.getString("vaccine"));
                vSess.setSlots(String.valueOf(sess.getJSONArray("slots")));
                vSess.setAvl_dose1(String.valueOf(sess.getInt("available_capacity_dose1")));
                vSess.setAvl_dose2(String.valueOf(sess.getInt("available_capacity_dose2")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.sessions.add(vSess);
        }




    }
}
