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

public class CustomAdapter3 extends ArrayAdapter<DataModel3> {
    Context mContext;

    private static class ViewHolder {
        TextView txtque;
        TextView yourans;
        TextView remark;
        TextView mark;
    }

    public CustomAdapter3(ArrayList<DataModel3> data, Context context) {
        super(context, R.layout.row_item3, data);
        this.mContext=context;

    }

    private int lastPosition = -1;

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataModel3 dataModel = getItem(position);

        CustomAdapter3.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapter3.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item3, parent, false);
            viewHolder.txtque = convertView.findViewById(R.id.txtque);
            viewHolder.yourans = convertView.findViewById(R.id.yourans);
            viewHolder.remark= convertView.findViewById(R.id.remarks);
            viewHolder.mark= convertView.findViewById(R.id.obtmark);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapter3.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtque.setText("Question :\n" + dataModel.getquestion());
        viewHolder.yourans.setText("Your Answer :\n"+dataModel.getyourans());
        viewHolder.remark.setText("Remarks :\n"+dataModel.getremark());
        viewHolder.mark.setText("Marking :\n"+dataModel.getmark());
        return convertView;
    }
}