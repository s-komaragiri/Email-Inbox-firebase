package com.vveginati.inclass13.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vveginati.inclass13.R;
import com.vveginati.inclass13.beans.Mails;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Mails> {

    List<Mails> mailsArrayList;


    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Mails> objects) {
        super(context, resource, objects);
        this.mailsArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Mails mail = getItem(position);
        ViewHolder viewHolder = null;


        if (null == convertView) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_mails_list, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.textView_msgPreview = convertView.findViewById(R.id.textView_msgPreview);

            viewHolder.textView_senderName = convertView.findViewById(R.id.textView_senderName);

            viewHolder.textView_timeStamp = convertView.findViewById(R.id.textView_timeStamp);

            viewHolder.imageView_read = convertView.findViewById(R.id.imageView_isRead);

            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }


        if (mail.getRead()) {

            viewHolder.imageView_read.setImageResource(R.drawable.circle_grey);
        } else if(!mail.getRead()){
                viewHolder.imageView_read.setImageResource(R.drawable.circle_blue);
            }

            viewHolder.textView_senderName.setText(mail.getSenderName());

            viewHolder.textView_msgPreview.setText(mail.getDetailedMessage());

        try {

            Long timestampObj = (Long) mail.getMailTimeStamp().get("timestamp");
            Date messageDate = new Date(timestampObj);

            String stringDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(messageDate);
            viewHolder.textView_timeStamp.setText(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }


    public static class ViewHolder {

        TextView textView_senderName;
        TextView textView_msgPreview;
        TextView textView_timeStamp;
        ImageView imageView_read;

        public ViewHolder() {


        }

    }
}
