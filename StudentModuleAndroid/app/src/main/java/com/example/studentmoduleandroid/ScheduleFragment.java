package com.example.studentmoduleandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ScheduleFragment extends Fragment {
        Button b1,b2;
        ListView lv;
        TextView txt;
        String email,pass,id;
        DBOperations dbo;
        ArrayList<DataModel> dataModels = new ArrayList<>();
        SharedPreferences obj;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view =inflater.inflate(R.layout.fragment_schedule, container, false);

            final Intent[] intent = new Intent[1];

            b1 = view.findViewById(R.id.btnstrt);
            b2 = view.findViewById(R.id.log_out);
            lv = view.findViewById(R.id.schlst);
            txt = view.findViewById(R.id.current_txt);
            b1.setEnabled(false);
            b1.setAlpha(.5f);
            b1.setOnClickListener(this::onStart);

            //noinspection ConstantConditions
            obj = this.getActivity().getSharedPreferences("Myfile", Context.MODE_PRIVATE);
            email=obj.getString("email","def");
            pass=obj.getString("password","def");

            dbo = new DBOperations();
            dataModels = dbo.schCHK(email,pass);
            CustomAdapter adapter = new CustomAdapter(dataModels,
                    getActivity().getApplicationContext());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this::onItemClick);
            b2.setOnClickListener(v -> {
                        SharedPreferences.Editor editor = obj.edit();
                        editor.putBoolean("status",false);
                        editor.apply();
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    intent[0] = new Intent(getActivity(), LogInActivity.class);
                                    startActivity(intent[0]);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    editor.clear();
                                    editor.apply();
                                    intent[0] = new Intent(getActivity(), LogInActivity.class);
                                    startActivity(intent[0]);
                                    break;
                                default:
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Keep credentials ?")
                                .setPositiveButton(Html.fromHtml
                                        ("<font color='#FF0000'>Yes</font>"),
                                        dialogClickListener)
                                .setNegativeButton(Html.fromHtml
                                        ("<font color='#FF0000'>No</font>"),
                                        dialogClickListener).show();

            });

            return view;
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
        DataModel data = (DataModel) parent.getItemAtPosition(position);
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String[] get = new String[6];
        get[0] = data.getsubject();
        get[1] = data.gettype();
        get[2] = data.getstarttime();
        get[3] = data.getendtime();
        get[4] = data.getlim();
        get[5] = data.getsId();
        String set = get[0] + " - " + get[1];
        txt.setText(set);
        String cur = formatter.format(cal.getTime());
        LocalTime localTime = LocalTime.parse(cur);
        LocalTime localTime1 = LocalTime.parse(get[3]);
        LocalTime localTime2 = LocalTime.parse(get[2]);
        LocalTime localTime3 = localTime2.plusMinutes(Integer.parseInt(get[4]));
        if(localTime.isAfter(localTime2)&&localTime.isBefore(localTime3)){
            int millis1 = localTime.toSecondOfDay() * 1000;
            int millis2 = localTime1.toSecondOfDay() * 1000;
            int millis = millis2 - millis1;
            obj = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Myfile",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = obj.edit();
            editor.putString("sch",get[5]);
            editor.putInt("Timer",millis);
            editor.apply();
            b1.setAlpha(1f);
            b1.setEnabled(true);
        }
        else
        {
            b1.setAlpha(.5f);
            b1.setEnabled(false);
        }

    }

    private void onStart(View view1){
        boolean togo,togo1;
        obj = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Myfile",
                Context.MODE_PRIVATE);
        email=obj.getString("email","def");
        pass=obj.getString("password","def");
        id = obj.getString("sch","def");
        dbo = new DBOperations();
        togo=dbo.isdone(email,pass,id);
        togo1=dbo.isdoneDes(email,pass,id);
        if(!togo||!togo1){
            Toast.makeText(this.getActivity(),"The test has already been attempted the " +
                    " \n answers were recorded successfully",Toast.LENGTH_LONG).show();

        }
       else{
            String type = dbo.chktype(id);
            String extype = "Descriptive";
            Intent myIntent;
            if(!type.equals(extype)) {
                myIntent = new Intent(this.getActivity(), ExamPageActivity.class);
            }
            else
            {
                myIntent = new Intent(this.getActivity(), DescriptiveExamActivity.class);
            }
            this.startActivity(myIntent);
        }
    }

}