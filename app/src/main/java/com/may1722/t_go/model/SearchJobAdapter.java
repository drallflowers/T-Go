package com.may1722.t_go.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.may1722.t_go.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by alxdaly on 4/11/2017.
 */

public class SearchJobAdapter extends BaseAdapter {
    private Context context;
    private List<SearchJobObject> jobs;

    public SearchJobAdapter(Context context, List<SearchJobObject> jobs) {
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
        TextView jobId = (TextView) convertView.findViewById(R.id.jobId);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(j.getDate());
        jobDate.setText(date);
        jobId.setText(Integer.toString(j.getJobId()));
        return convertView;
    }
}
