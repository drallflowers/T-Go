package com.may1722.t_go.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.may1722.t_go.R;
import com.may1722.t_go.ui.JobDetailsActivity;
import com.may1722.t_go.ui.ModSearchJobActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by alxdaly on 4/11/2017.
 */

public class SearchJobAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private List<SearchJobObject> jobs;

    public SearchJobAdapter(Activity activity, Context context, List<SearchJobObject> jobs) {
        this.activity = activity;
        this.context = context;
        this.jobs = jobs;
    }

    public int getCount(){
        return jobs.size();
    }

    @Override
    public SearchJobObject getItem(int pos) {
        return jobs.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SearchJobObject j = jobs.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_search_job, null);
        TextView jobDate = (TextView) convertView.findViewById(R.id.jobDate);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(j.getDate());
        jobDate.setText(date);
        convertView.setOnClickListener(new OnItemClickListener(position));
        return convertView;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0){
            ModSearchJobActivity searchJob = (ModSearchJobActivity) activity;
            searchJob.openJobDetailsPage(mPosition);

        }
    }
}
