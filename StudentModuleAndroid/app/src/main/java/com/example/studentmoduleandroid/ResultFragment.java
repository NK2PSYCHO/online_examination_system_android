package com.example.studentmoduleandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class ResultFragment extends Fragment {

    ListView lv;
    DBOperations dbo;
    ArrayList<DataModel> dataModels = new ArrayList<>();
    Button b1;
    SharedPreferences obj;
    String email,pass,schid;


    public static ResultFragment newInstance() {
        return new ResultFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        b1 = view.findViewById(R.id.log_out);
        lv = view.findViewById(R.id.reslst);

        //noinspection ConstantConditions
        obj = this.getActivity().getSharedPreferences("Myfile", Context.MODE_PRIVATE);
        email=obj.getString("email","def");
        pass=obj.getString("password","def");
        schid = obj.getString("sch","def");

        dbo = new DBOperations();
        dataModels = dbo.getResult(email,pass);
        CustomAdapter1 adapter = new CustomAdapter1(dataModels,
                getActivity().getApplicationContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this::onItemClick);

        final Intent[] intent = new Intent[1];

        b1.setOnClickListener(v -> {
            obj = this.getActivity().getSharedPreferences("Myfile", Context.MODE_PRIVATE);
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
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Keep credentials ?")
                    .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                            dialogClickListener)
                    .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                            dialogClickListener).show();
        });

        return view;
    }

    private void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
        lv.setOnItemClickListener(this::onItemClick);
        DataModel data = (DataModel) parent.getItemAtPosition(position);
        String sid ;
        sid = data.getsId();
        String type = dbo.chktype(sid);
        String extype = "Descriptive";
        obj = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Myfile",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = obj.edit();
        editor.putString("sch",sid);
        editor.apply();
        Intent myIntent;
        if(!type.equals(extype)) {
            myIntent = new Intent(this.getActivity(), ResultActivity.class);
        }
        else
        {
            myIntent = new Intent(this.getActivity(), ResultActivityDes.class);
        }
        this.startActivity(myIntent);
    }
}