package com.may1722.t_go.model;

/**
 * Created by Jacob on 2/1/2017.
 */

public class ReviewObject {
    /**
     * Username of person giving review
     */
    private String reviewer;
    /**
     * Username of person being reviewed
     */
    private String reviewee;
    /**
     * Rating from 0-5 (?) to summarize review
     */
    private double reviewRating;
    /**
     * Written review by reviewer
     */
    private String commentary;

    public ReviewObject(String userReviewing, String userBeingReviewed, double rating, String text){
        reviewer = userReviewing;
        reviewee = userBeingReviewed;
        reviewRating = rating;
        commentary = text;
    }

    /**
     * Return reviewer username
     * @return reviewer
     */
    public String getReviewer(){return reviewer;}

    /**
     * return reviewee username
     * @return reviewee
     */
    public String getReviewee(){return  reviewee;}

    /**
     * return text commentary
     * @return commentary
     */
    public String getCommentary(){return commentary;}

    /**
     * Return numeric rating
     * @return reviewRating
     */
    public double getReviewRating(){return reviewRating;}
}
