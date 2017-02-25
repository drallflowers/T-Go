package com.may1722.t_go.model;

/**
 * Created by Jacob on 2/24/2017.
 */

public class ProductObject {

    private int product_id;
    private String product_name;
    private String product_description;
    private int update_count;
    private double price_deviation;
    private double avg_price;

    public ProductObject(int id, String name, String desc, int updates, double dev, double price){
        product_id = id;
        product_name = name;
        product_description = desc;
        update_count = updates;
        price_deviation = dev;
        avg_price = price;
    }

    public ProductObject(int id, String name, double price){
        product_id = id;
        product_name = name;
        avg_price = price;
    }

    public int getProduct_id(){return product_id;}
    public String getProduct_name(){return product_name;}
    public String getProduct_description(){return product_description;}
    public int getUpdate_count(){return update_count;}
    public double getPrice_deviation(){return price_deviation;}
    public double getAvg_price(){return avg_price;}
}
