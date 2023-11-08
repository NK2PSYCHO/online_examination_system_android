package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyResultDesActivity extends AppCompatActivity {

    ArrayList<DataModel3> responses = new ArrayList<>();
    DBOperations dbo;
    ListView lv;
    SharedPreferences obj;
    TextView txtexm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_result_des);

        lv = findViewById(R.id.reslst);
        txtexm = findViewById(R.id.totalMarks);

        obj = getSharedPreferences("fMyfile", MODE_PRIVATE);
        String schid = obj.getString("sch", "def");
        String sid = obj.getString("sid", "def");

        dbo = new DBOperations();
        responses = dbo.fdispResultDes(sid,schid);
        CustomAdapter3 adapter = new CustomAdapter3(responses,
                getApplicationContext());
        lv.setAdapter(adapter);

        String tot = dbo.fgettotmarks(sid,schid);

        txtexm.setText("Total Marks Obtained :\t\t" + tot);
    }
}