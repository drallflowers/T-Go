package com.may1722.t_go.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_board);

        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating("higgins", 5.00));
        ratings.add(new Rating("grumpy_old_men", 3.00));
        ratings.add(new Rating("grumpier_old_men", 1.00));
        ratings.add(new Rating("apocrypha", 4.00));
        ratings.add(new Rating("the_mouse_that_roared", 4.50));
        ratings.add(new Rating("the_wheel_in_the_sky", 4.25));

        double userRatingSum = 0;
        for(Rating r : ratings) {
            userRatingSum += r.rating;
        }
        double userRating = userRatingSum/ratings.size();

        RatingAdapter adapter = new RatingAdapter(this, ratings);
        ListView lv = (ListView)this.findViewById(R.id.list_review_board);
        lv.setAdapter(adapter);

        TextView username = (TextView)this.findViewById(R.id.review_board_username);
        TextView rating = (TextView)this.findViewById(R.id.review_board_rating);

        username.setText("Courier");
        rating.setText(String.format("%.2f", userRating));
        if(userRating < 2){
            rating.setTextColor(Color.RED);
        } else if(userRating < 3.5){
            rating.setTextColor(Color.YELLOW);
        } else {
            rating.setTextColor(Color.GREEN);
        }
    }

    public class Rating {
        public String username;
        public double rating;

        public Rating(String name, double rate){
            username = name;
            rating = rate;
        }
    }

    public class RatingAdapter extends ArrayAdapter<Rating> {

        public RatingAdapter(Context context, ArrayList<Rating> ratings){
            super(context, 0, ratings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Rating rate = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review_board, parent, false);
            }

            TextView ratingUsername = (TextView)convertView.findViewById(R.id.review_board_item_username);
            TextView ratingRating = (TextView) convertView.findViewById(R.id.review_board_item_rating);

            ratingUsername.setText(rate.username);
            ratingRating.setText(String.format("%.2f", rate.rating));

            if(rate.rating < 2){
                ratingRating.setTextColor(Color.RED);
            } else if(rate.rating < 3.5){
                ratingRating.setTextColor(Color.YELLOW);
            } else {
                ratingRating.setTextColor(Color.GREEN);
            }

            return convertView;
        }
    }
}
