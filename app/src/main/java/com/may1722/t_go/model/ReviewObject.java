package com.may1722.t_go.model;

/**
 * Created by Jacob on 2/1/2017.
 */

public class ReviewObject {
    /**
     * Username of person giving review
     */
    private int reviewer;
    /**
     * Username of person being reviewed
     */
    private int reviewee;
    /**
     * Rating from 0-5 (?) to summarize review
     */
    private int reviewRating;
    /**
     * Written review by reviewer
     */
    private String commentary;
    /**
     * ID for this review
     */
    private int review_id;

    public ReviewObject(int ID, int userReviewing, int userBeingReviewed, int rating, String text){
        review_id = ID;
        reviewer = userReviewing;
        reviewee = userBeingReviewed;
        reviewRating = rating;
        commentary = text;
    }

    /**
     * Return reviewer username
     * @return reviewer
     */
    public int getReviewer(){return reviewer;}

    /**
     * return reviewee username
     * @return reviewee
     */
    public int getReviewee(){return  reviewee;}

    /**
     * return text commentary
     * @return commentary
     */
    public String getCommentary(){return commentary;}

    /**
     * Return numeric rating
     * @return reviewRating
     */
    public int getReviewRating(){return reviewRating;}

    /**
     * Return the ID of this review
     * @return
     */
    public int getReview_id(){return review_id;}
}
