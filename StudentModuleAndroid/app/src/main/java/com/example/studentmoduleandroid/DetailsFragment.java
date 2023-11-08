package com.example.studentmoduleandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    Button b1;
    TextView txt;
    String[] gamma ={"For Loop","Name :","Email :","Class :","Enrollment Number :","Seat Number :"};
    String[] beta ={};
    String email,pass;
    DBOperations dbo;
    StringBuilder txtview = new StringBuilder();
    SharedPreferences preferences;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //noinspection ConstantConditions
        preferences = this.getActivity().getSharedPreferences("Myfile", Context.MODE_PRIVATE);
        email=preferences.getString("email","def");
        pass=preferences.getString("password","def");
        dbo = new DBOperations();
        beta=dbo.getStud(email,pass);
        for (int i=1;i<beta.length;i++) {
            txtview.append("\n").append(gamma[i]).append("\n").append(beta[i]).append("\n");
        }
        View view =inflater.inflate(R.layout.fragment_details, container, false);
        txt = view.findViewById(R.id.disp_view);
        b1 = view.findViewById(R.id.log_out);
        txt.setText(txtview.toString());
        final Intent[] intent = new Intent[1];
        b1.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit();
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
}