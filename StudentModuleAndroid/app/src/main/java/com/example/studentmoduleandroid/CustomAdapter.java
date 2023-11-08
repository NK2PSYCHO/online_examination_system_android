package com.example.studentmoduleandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;

    private static class ViewHolder {
        TextView txtdate;
        TextView txttype;
        TextView txtstartTime;
        TextView txtendTime;
        TextView txtsubject;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtdate = convertView.findViewById(R.id.date);
            viewHolder.txttype = convertView.findViewById(R.id.type);
            viewHolder.txtstartTime = convertView.findViewById(R.id.startTime);
            viewHolder.txtendTime= convertView.findViewById(R.id.endTime);
            viewHolder.txtsubject= convertView.findViewById(R.id.subj);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtdate.setText(dataModel.getdate());
        viewHolder.txttype.setText(dataModel.gettype());
        viewHolder.txtstartTime.setText(dataModel.getstarttime());
        viewHolder.txtendTime.setText(dataModel.getendtime());
        viewHolder.txtsubject.setText(dataModel.getsubject());
        return convertView;
    }
}