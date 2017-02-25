package com.may1722.t_go.model;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by David on 2/22/2017.
 */

public class ListViewAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Double> prices = new ArrayList<>();
    private Context context;

    public ListViewAdapter(ArrayList<String> list,ArrayList<Double> prices, Context context) {
        this.list = list;
        this.prices = prices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_delete, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));
        //listItemText.setWidth((int) (view.getWidth()*.5));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);





        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                double removedPrice = prices.get(position);
                prices.remove(position);


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View temp = inflater.inflate(R.layout.activity_job_submit, null);

                TextView currentPriceView = (TextView) ((Activity) context).findViewById(R.id.totalPrice);

                double currentPrice = Double.parseDouble(currentPriceView.getText().toString());
                currentPrice -= removedPrice;
                DecimalFormat decim = new DecimalFormat("0.00");
                String s = decim.format(currentPrice);
                currentPriceView.setText(s);

                ListView t = (ListView) ((Activity) context).findViewById(R.id.listView);
                setListViewHeightBasedOnChildren(t);

                notifyDataSetChanged();

            }
        });

        return view;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        System.out.println(listView.getLayoutParams().height);

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < this.getCount(); i++) {
            view = this.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        System.out.println(totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height =totalHeight + (listView.getDividerHeight() * (this.getCount() - 1));
        System.out.println(params.height);
        listView.setLayoutParams(params);
    }

}
