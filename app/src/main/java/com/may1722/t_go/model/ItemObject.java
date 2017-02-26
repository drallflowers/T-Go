package com.may1722.t_go.model;

/**
 * Created by Jacob on 2/25/2017.
 */

public class ItemObject {

    private int item_id;
    private String item_name;
    private String item_description;
    private double price;
    private int job_id;
    private int location_id;
    private int fragility;
    private int quantity;
    private int product_id;

    public ItemObject(int id, String name, String desc, double price, int job, int loc, int frag, int number, int product){
        item_id = id;
        item_name = name;
        item_description = desc;
        this.price = price;
        job_id = job;
        location_id = loc;
        fragility = frag;
        quantity = number;
        product_id = product;
    }

    public ItemObject(int id, String name, double price, int number, int product){
        item_id = id;
        item_name = name;
        this.price = price;
        quantity = number;
        product_id = product;
    }

    public int getItem_id(){return item_id;}
    public String getItem_name(){return item_name;}
    public String getItem_description(){return item_description;}
    public double getPrice(){return price;}
    public int getJob_id(){return job_id;}
    public int getLocation_id(){return location_id;}
    public int getFragility(){return fragility;}
    public int getQuantity(){return quantity;}
    public int getProduct_id(){return product_id;}
}

