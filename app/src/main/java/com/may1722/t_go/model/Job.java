package com.may1722.t_go.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by David on 1/29/2017.
 */

public class Job {

    UserObject owner;
    private String description;
    private String title;
    private String time;
    private String address;
    private Double price;


    //need to  uncomment the User field after merge
    public Job(UserObject u, String desc, String ttl, Double p, String t, String adr){
        this.price = p;
        this.description = desc;
        this.title = ttl;
        this.time = t;
        this.address = adr;
        this.owner = u;
    }


    public String getDescription(){
        return this.description;
    }

    public String getAddress(){
        return this.address;
    }

    public String getTitle(){
        return this.title;
    }

    public String getTime(){
        return this.time;
    }

    public Double getPrice(){
        return this.price;
    }

    public Double calcGasCost(){
        final String[] avgGasPrice = new String[1];
        // get distance from start to store to destination
        Thread downloadThread = new Thread() {
            public void run() {
                Document doc;
                try {
                    doc = Jsoup.connect("https://www.gasbuddy.com").get();
                    Elements classes = doc.getElementsByClass("gb-price-lg");
                    Element avgPriceDiv = classes.get(0);
                    avgGasPrice[0] = avgPriceDiv.text();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        downloadThread.start();

        while(downloadThread.isAlive()){

        }
        //switch out the 1 for total distance
        //25.5 = avg car mpg
        double totalGalons = 1/25.5;

        double totalPrice = totalGalons * Double.parseDouble(avgGasPrice[0]);

        return totalPrice;

    }
}
