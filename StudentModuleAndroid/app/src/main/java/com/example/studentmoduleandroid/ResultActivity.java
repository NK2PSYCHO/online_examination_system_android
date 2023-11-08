package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ArrayList<DataModel2> responses = new ArrayList<>();
    DBOperations dbo;
    ListView lv;
    SharedPreferences obj;
    TextView txtexm;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        lv = findViewById(R.id.reslst);
        txtexm = findViewById(R.id.totalMarks);

        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String schid = obj.getString("sch","def");

        dbo = new DBOperations();
        responses = dbo.dispResult(email,pass,schid);
        CustomAdapter2 adapter = new CustomAdapter2(responses,
                getApplicationContext());
        lv.setAdapter(adapter);

        String tot = dbo.gettotmarks(email,pass,schid);

        txtexm.setText("Total Marks Obtained :\t\t" + tot);
    }
}