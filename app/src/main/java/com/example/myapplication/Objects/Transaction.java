package com.example.myapplication.Objects;

import android.media.Image;

public class Transaction {
    private String name, date, price;
    private String url;

    public Transaction(String name, String date, String price, String url) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
