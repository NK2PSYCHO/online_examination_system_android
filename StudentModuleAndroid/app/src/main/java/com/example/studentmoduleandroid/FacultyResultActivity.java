package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyResultActivity extends AppCompatActivity {

    ArrayList<DataModel2> responses = new ArrayList<>();
    DBOperations dbo;
    ListView lv;
    SharedPreferences obj;
    TextView txtexm;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_result);

        lv = findViewById(R.id.reslst);
        txtexm = findViewById(R.id.totalMarks);

        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        String sid = obj.getString("sid","def");
        String schid = obj.getString("sch","def");

        dbo = new DBOperations();
        responses = dbo.fdispResult(sid,schid);
        CustomAdapter2 adapter = new CustomAdapter2(responses,
                getApplicationContext());
        lv.setAdapter(adapter);

        String tot = dbo.fgettotmarks(sid,schid);

        txtexm.setText("Total Marks Obtained :\t\t" + tot);
    }
}