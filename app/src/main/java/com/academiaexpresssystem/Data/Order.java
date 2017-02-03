package com.academiaexpresssystem.Data;

import java.util.ArrayList;
import java.util.Calendar;

public class Order {
    private String userName;
    private String userPhone;

    String id;

    private String time;
    private String location;

    private int price;

    private Calendar calendar;

    private ArrayList<String> ingridients;

    private boolean status;

    private String position;

    public Order(String userName, String userPhone, String time, String location,
                 ArrayList<String> ingridients, boolean status, String position, String id, int price) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.time = time;
        this.location = location;
        this.ingridients = ingridients;
        this.status = status;
        this.position = position;
        this.id = id;
        this.price = price;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getIngridients() {
        return ingridients;
    }

    public void setIngridients(ArrayList<String> ingridients) {
        this.ingridients = ingridients;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
