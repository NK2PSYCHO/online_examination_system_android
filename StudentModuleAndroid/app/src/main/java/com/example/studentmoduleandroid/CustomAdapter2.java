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

public class CustomAdapter2 extends ArrayAdapter<DataModel2> {
    Context mContext;

    private static class ViewHolder {
        TextView txtque;
        TextView coropt;
        TextView youropt;
        TextView obtmark;
    }

    public CustomAdapter2(ArrayList<DataModel2> data, Context context) {
        super(context, R.layout.row_item2, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel2 dataModel = getItem(position);

        CustomAdapter2.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapter2.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item2, parent, false);
            viewHolder.txtque = convertView.findViewById(R.id.txtque);
            viewHolder.coropt = convertView.findViewById(R.id.coropt);
            viewHolder.youropt= convertView.findViewById(R.id.youropt);
            viewHolder.obtmark= convertView.findViewById(R.id.obtmark);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapter2.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtque.setText("Question :\n" + dataModel.getquestion());
        viewHolder.coropt.setText("Correct Option :\n"+dataModel.getcoropt());
        viewHolder.youropt.setText("Selected Option :\n"+dataModel.getyouropt());
        if(dataModel.getcoropt().equals(dataModel.getyouropt())){
            viewHolder.obtmark.setText("Marks Obtained :\t 2");
        }
        else if(!dataModel.getyouropt().equals("Not Selected"))
        {
            viewHolder.obtmark.setText("Marks Obtained :\t -1");
        }
        else{
            viewHolder.obtmark.setText("Marks Obtained :\t 0");
        }
        return convertView;
    }
}