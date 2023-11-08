package com.example.studentmoduleandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter4 extends ArrayAdapter<DataModel4> {
    Context mContext;

    private static class ViewHolder {
        TextView dmsid;
        TextView date;
        TextView subj;
        TextView classname;
        TextView schid;
        TextView stud;
    }

    public CustomAdapter4(ArrayList<DataModel4> data, Context context) {
        super(context, R.layout.row_item4, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel4 dataModel = getItem(position);

        CustomAdapter4.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapter4.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item4, parent, false);
            viewHolder.dmsid = convertView.findViewById(R.id.dmsid);
            viewHolder.date = convertView.findViewById(R.id.date);
            viewHolder.subj = convertView.findViewById(R.id.subj);
            viewHolder.classname = convertView.findViewById(R.id.class_name);
            viewHolder.schid = convertView.findViewById(R.id.schid);
            viewHolder.stud=convertView.findViewById(R.id.stud_name);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapter4.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if(dataModel.getdmdte().equals("Not Declared/Available Yet")){
            viewHolder.dmsid.setText("");
            viewHolder.date.setText(dataModel.getdmdte());
            viewHolder.subj.setText("");
            viewHolder.classname.setText("");
            viewHolder.schid.setText("");
            viewHolder.stud.setText("");
        }
        else {
            viewHolder.dmsid.setText("Student Seat No. :-\n" + dataModel.getdmseat());
            viewHolder.date.setText("Date :-\n" + dataModel.getdmdte());
            viewHolder.subj.setText("Subject :-\n" + dataModel.getdmsname());
            viewHolder.classname.setText("Class :-\n" + dataModel.getdmcname());
            viewHolder.schid.setText("Schedule ID :-\n" + dataModel.getdmschid());
            viewHolder.stud.setText("Student name:-\n" + dataModel.getdmstuname());
        }
        return convertView;
    }
}