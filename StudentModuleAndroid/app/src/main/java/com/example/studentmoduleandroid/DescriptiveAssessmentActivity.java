package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class DescriptiveAssessmentActivity extends AppCompatActivity {

    Button btnext,btnprev,btnsub;
    TextView txtans,txtque,txtmaxmark;
    EditText edtremark,edtobtmark;
    String schid,sid;
    SharedPreferences obj;
    ArrayList<DataModel5> questions = new ArrayList<>();
    DBOperations dbo;
    DataModel5 data;
    int index;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptive_assessment);

        txtque = findViewById(R.id.question);
        txtans = findViewById(R.id.answer);
        txtmaxmark = findViewById(R.id.maxmarks);
        edtobtmark = findViewById(R.id.marksgiven);
        edtremark = findViewById(R.id.fremarks);
        btnext = findViewById(R.id.next);
        btnext.setOnClickListener(this::onNext);

        btnprev = findViewById(R.id.prev);
        btnprev.setOnClickListener(this::onPrev);
        btnprev.setEnabled(false);
        btnprev.setAlpha(.5f);

        btnsub = findViewById(R.id.submit);
        btnsub.setOnClickListener(this::onSubmit);

        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        schid = obj.getString("sch","def");
        sid = obj.getString("sid","def");

        dbo = new DBOperations();
        questions = dbo.assessDes(sid,schid);

        if(!questions.isEmpty()){
            index = 0;
            data = questions.get(index);
            txtque.setText(data.getfque());
            txtans.setText(data.getfans());
            txtmaxmark.setText("Maximum marks to give := "+data.getfmark());
        }
    }

    private void onSubmit(View view) {
        data = questions.get(index);
        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        String qid = data.getfqid();
        dbo = new DBOperations();
        Boolean isexist;

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    dbo.updassessDes(edtremark.getText().toString(),edtobtmark.getText().toString(),sid,schid,qid);

                    dbo.fSetResult(sid,schid);

                    Intent myIntent = new Intent(this, HomePageFacultyActivity.class);
                    this.startActivity(myIntent);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to submit ? \n" +
                "Notice : Make sure all the questions were graded  \n \t\t\t\t" +
                " so no problems occurs later later ")
                .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                        dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                        dialogClickListener).show();
    }

    @SuppressLint("SetTextI18n")
    private void onPrev(View view) {
        data = questions.get(index);
        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        String qid = data.getfqid();
        dbo = new DBOperations();
        Boolean isexist;

        btnext.setEnabled(true);
        btnext.setAlpha(1f);

        dbo.updassessDes(edtremark.getText().toString(),edtobtmark.getText().toString(),sid,schid,qid);

        edtremark.setText("");
        edtobtmark.setText("");

        if(index>0) {
            index--;
            data = questions.get(index);
            txtque.setText(data.getfque());
            txtans.setText(data.getfans());
            txtmaxmark.setText("Maximum marks to give := "+data.getfmark());
            qid = data.getfqid();
            isexist = dbo.isassessDes(sid, schid, qid);

            if(isexist){
                String[] get = dbo.ifassessDes(sid,schid,qid);
                edtobtmark.setText(get[1]);
                edtremark.setText(get[0]);
            }

        }

        if(index==0){
            btnprev.setEnabled(false);
            btnprev.setAlpha(.5f);
        }
    }

    @SuppressLint("SetTextI18n")
    private void onNext(View view) {

        data = questions.get(index);
        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        String qid = data.getfqid();
        dbo = new DBOperations();
        Boolean isexist;

        btnprev.setEnabled(true);
        btnprev.setAlpha(1f);

        dbo.updassessDes(edtremark.getText().toString(),edtobtmark.getText().toString(),sid,schid,qid);

        edtremark.setText("");
        edtobtmark.setText("");

        if(index<(questions.size()-1)) {
            index++;
            data = questions.get(index);
            txtque.setText(data.getfque());
            txtans.setText(data.getfans());
            txtmaxmark.setText("Maximum marks to give := "+data.getfmark());
            qid = data.getfqid();
            isexist = dbo.isassessDes(sid,schid,qid);

            if(isexist){
                String[] get = dbo.ifassessDes(sid,schid,qid);
                edtobtmark.setText(get[1]);
                edtremark.setText(get[0]);
            }
        }


        if(index==questions.size()-1){
            btnext.setEnabled(false);
            btnext.setAlpha(.5f);
        }

        if(index==0){
            btnprev.setEnabled(false);
            btnprev.setAlpha(.5f);
        }
    }
}