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

public class AllDescriptiveFragment extends Fragment {

    Button b1;
    SharedPreferences obj;
    ListView lv;
    String email,pass;
    DBOperations dbo;
    ArrayList<DataModel4> dataModels = new ArrayList<>();

    public static AllDescriptiveFragment newInstance() {
        return new AllDescriptiveFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_descriptive, container, false);

        b1 = view.findViewById(R.id.log_out);
        lv= view.findViewById(R.id.deslst);

        //noinspection ConstantConditions
        obj = this.getActivity().getSharedPreferences("fMyfile", Context.MODE_PRIVATE);
        email=obj.getString("email","def");
        pass=obj.getString("password","def");

        dbo = new DBOperations();
        dataModels = dbo.getALLdes(email,pass);
        CustomAdapter4 adapter = new CustomAdapter4(dataModels,
                getActivity().getApplicationContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this::onItemClick);

        final Intent[] intent = new Intent[1];

        b1.setOnClickListener(v -> {
            SharedPreferences.Editor editor = obj.edit();
            editor.putBoolean("status",false);
            editor.apply();
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        editor.putBoolean("Mstatus",true);
                        editor.apply();
                        this.showDialogue();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        editor.putBoolean("Mstatus",false);
                        editor.apply();
                        this.showDialogue();
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Keep Faculty Mode ?")
                    .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                            dialogClickListener)
                    .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                            dialogClickListener).show();
        });
        return view;
    }

    public void showDialogue(){
        final Intent[] intent = new Intent[1];
        final boolean[] stat = {false};
        obj = Objects.requireNonNull(this.getActivity()).getSharedPreferences("fMyfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = obj.edit();
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    stat[0] = obj.getBoolean("Mstatus",false);
                    if(stat[0]){
                        intent[0] = new Intent(getActivity(), FacultyLogIn.class);
                    }
                    else
                    {
                        intent[0] = new Intent(getActivity(), LogInActivity.class);
                    }
                    startActivity(intent[0]);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    editor.remove("email");
                    editor.remove("pass");
                    editor.apply();
                    stat[0] = obj.getBoolean("Mstatus",false);
                    if(stat[0]){
                        intent[0] = new Intent(getActivity(), FacultyLogIn.class);
                    }
                    else
                    {
                        intent[0] = new Intent(getActivity(), LogInActivity.class);
                    }
                    startActivity(intent[0]);
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Keep Credentials ?")
                .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                        dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                        dialogClickListener).show();
    }

    private void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
        lv.setOnItemClickListener(this::onItemClick);
        DataModel4 data = (DataModel4) parent.getItemAtPosition(position);
        obj = Objects.requireNonNull(this.getActivity()).getSharedPreferences("fMyfile",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = obj.edit();
        editor.putString("sch",data.getdmschid());
        editor.putString("sid",data.getdmsid());
        editor.apply();
        Intent myIntent;
        myIntent = new Intent(this.getActivity(), FacultyResultDesActivity.class);
        this.startActivity(myIntent);
    }
}