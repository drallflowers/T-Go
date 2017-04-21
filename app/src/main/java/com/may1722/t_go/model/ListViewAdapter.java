package com.may1722.t_go.model;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;
import com.may1722.t_go.ui.AddItemActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by David on 2/22/2017.
 */

public class ListViewAdapter extends BaseAdapter implements ListAdapter {
    protected static ArrayList<String> list = new ArrayList<>();
    protected static ArrayList<String> description = new ArrayList<>();
    protected static ArrayList<Double> prices = new ArrayList<>();
    protected static ArrayList<Integer> quantities = new ArrayList<>();
    protected static Context context;
    protected static int pos;

    public ListViewAdapter(ArrayList<String> list, ArrayList<String> description, ArrayList<Double> prices,ArrayList<Integer> quantity, Context context) {
        this.list = list;
        this.description = description;
        this.prices = prices;
        this.quantities = quantity;
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
        pos = position;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_delete, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        TextView listItemQuantity = (TextView)view.findViewById(R.id.textAmount);
        TextView listItemPrice = (TextView) view.findViewById(R.id.textPrice);
        listItemText.setText(list.get(position));
        listItemQuantity.setText(quantities.get(position)+"");
        listItemPrice.setText(prices.get(position).toString());
        //listItemText.setWidth((int) (view.getWidth()*.5));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);





        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                DialogFragment newFragment = new EditDeleteFragment();
                newFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "test");


                ListView t = (ListView) ((Activity) context).findViewById(R.id.listView);

                setListViewHeightBasedOnChildren(t);
                notifyDataSetChanged();


            }
        });

        return view;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

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
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height =totalHeight + (listView.getDividerHeight() * (this.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static class EditDeleteFragment extends DialogFragment {
            EditDeleteFragment newInstance(String title) {
            EditDeleteFragment fragment = new EditDeleteFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.view_add_item, null);

            //connect to the edit_quantity to get original amount
            final EditText editName = (EditText) view.findViewById(R.id.edit_name);
            final EditText editDescription = (EditText) view.findViewById(R.id.edit_description);
            final EditText editPrice = (EditText) view.findViewById(R.id.edit_price);
            final EditText editQuantity = (EditText) view.findViewById(R.id.edit_quantity);

            final View view2 = inflater.inflate(R.layout.list_item_delete, null);
            final TextView currentPriceView = (TextView) ((Activity) context).findViewById(R.id.totalPrice);

            final double[] currentPrice = {Double.parseDouble(currentPriceView.getText().toString())};


            editName.setText(list.get(pos)+"");
            editDescription.setText(description.get(pos)+"");
            editPrice.setText(prices.get(pos)+"");
            editQuantity.setText(quantities.get(pos)+"");

            Button b = (Button) view.findViewById(R.id.delete_btn);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(pos); //or some other task
                    double removedPrice = prices.get(pos)*quantities.get(pos);
                    description.remove(pos);
                    prices.remove(pos);
                    quantities.remove(pos);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View temp = inflater.inflate(R.layout.activity_job_submit, null);


                    currentPrice[0] -= removedPrice;
                    DecimalFormat decim = new DecimalFormat("0.00");
                    String s = decim.format(currentPrice[0]);
                    currentPriceView.setText("Price: " + s);
                    ((AddItemActivity)context).fragmentTaskCompleted();
                    dismiss();

                }
            });
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("Edit or Delete Item:")
                    .setView(view)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = inflater.inflate(R.layout.view_add_item, null);
                                    //connect to the edit_quantity to get original amount
                                    list.set(pos, editName.getText().toString());
                                    description.set(pos, editDescription.getText().toString());
                                    prices.set(pos, Double.parseDouble(editPrice.getText().toString()));
                                    quantities.set(pos,Integer.parseInt(editQuantity.getText().toString()));
                                    double cost = 0.0;
                                    for(int i = 0; i < prices.size();i++){
                                        cost+= (prices.get(i)*quantities.get(i));
                                    }
                                    currentPrice[0] = cost;
                                    DecimalFormat decim = new DecimalFormat("0.00");
                                    String s = decim.format(currentPrice[0]);
                                    currentPriceView.setText(s);
                                    ((AddItemActivity)context).fragmentTaskCompleted();

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((AddItemActivity)context).fragmentTaskCompleted();
                                }
                            }).create();
        }
    }

}


