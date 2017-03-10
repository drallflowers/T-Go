package com.may1722.t_go.ui;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;
import com.may1722.t_go.model.MessageObject;

import java.util.List;

import static com.may1722.t_go.R.color.colorReceived;


public class ChatAdapter extends BaseAdapter{
    private Context context;
    private List<MessageObject> msgs;

    public ChatAdapter(Context context, List<MessageObject> msgs) {
        this.context = context;
        this.msgs = msgs;
    }

    public int getCount(){
        return msgs.size();
    }

    @Override
    public MessageObject getItem(int pos) {
        return msgs.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MessageObject m = msgs.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(msgs.get(position).isSelf()){
            convertView = mInflater.inflate(R.layout.list_item_chat_sent, null);
        }
        else {
            convertView = mInflater.inflate(R.layout.list_item_chat_received, null);
        }

        TextView msgFrom = (TextView) convertView.findViewById(R.id.msgFrom);
        TextView msg = (TextView) convertView.findViewById(R.id.msg);
        if(msgs.get(position).isSelf()) {
            msg.setTextColor(ContextCompat.getColor(context, R.color.colorReceived));
        }
        msgFrom.setText(m.getUser());
        msg.setText(m.getMessage());
        return convertView;
    }
}
