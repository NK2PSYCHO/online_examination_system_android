package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class DescriptiveExamActivity extends AppCompatActivity {

    Button btnext,btnprev,btnsub;
    TextView txtcdown,txtque;
    EditText ans;
    SharedPreferences obj;
    ArrayList<DataModel1> questions = new ArrayList<>();
    DBOperations dbo;
    DataModel1 data;
    int index;
    String schid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptive_exam);
        txtque = findViewById(R.id.txtQue);
        txtcdown = findViewById(R.id.countdown);

        ans = findViewById(R.id.txtAns);

        ans.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onCreateActionMode(ActionMode actionMode,
                                              Menu menu) {//from w ww .  j  a  v  a 2s .  c  om
                return false;
            }

            public boolean onPrepareActionMode(ActionMode actionMode,
                                               Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode actionMode,
                                               MenuItem item) {
                return false;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });

        ans.setLongClickable(false);
        ans.setTextIsSelectable(false);

        btnext = findViewById(R.id.next);
        btnext.setOnClickListener(this::onNext);

        btnprev = findViewById(R.id.prev);
        btnprev.setOnClickListener(this::onPrev);
        btnprev.setEnabled(false);
        btnprev.setAlpha(.5f);

        btnsub = findViewById(R.id.submit);
        btnsub.setOnClickListener(this::onSubmit);

        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        int millis = obj.getInt("Timer",0);
        schid = obj.getString("sch","def");

        new CountDownTimer(millis, 1000) {

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                txtcdown.setText(""+String.format("%02d : %02d : %02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes((TimeUnit.MILLISECONDS.
                                        toHours(millisUntilFinished))),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

                onTimeOut();

            }
        }.start();

        dbo = new DBOperations();
        questions = dbo.getDesExam(schid);

        if(!questions.isEmpty()){
            Collections.shuffle(questions);
            index = 0;
            data = questions.get(index);
            txtque.setText(data.getquestion());
        }
    }

    private void onSubmit(View view) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    data = questions.get(index);
                    obj = getSharedPreferences("Myfile",MODE_PRIVATE);
                    String email = obj.getString("email","def");
                    String pass = obj.getString("password","def");
                    String qid = data.getqid();
                    String ansif;
                    dbo = new DBOperations();
                    Boolean isexist = dbo.isexistDes(email,pass,schid,qid);

                    if(!ans.getText().toString().matches("")){
                        ansif = ans.getText().toString();
                    }
                    else
                    {
                        ansif = "Not Attempted";
                    }

                    if(!isexist){
                        dbo.insansDes(email,pass,schid,qid,ansif);
                    }
                    else{
                        dbo.updansDes(email,pass,schid,qid,ansif);
                    }

                    Intent myIntent = new Intent(this, HomePageActivity.class);
                    this.startActivity(myIntent);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to submit ? \n" +
                "Notice : Check everything before submitting as \n \t\t\t\t" +
                " it cannot be changed later ")
                .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                        dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                        dialogClickListener).show();


    }

    private void onPrev(View view) {
        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ansif;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexistDes(email,pass,schid,qid);

        btnext.setEnabled(true);
        btnext.setAlpha(1f);

        if(!ans.getText().toString().matches("")){
            ansif = ans.getText().toString();
        }
        else
        {
            ansif = "Not Attempted";
        }

        if(!isexist){
            dbo.insansDes(email,pass,schid,qid,ansif);
        }
        else{
            dbo.updansDes(email,pass,schid,qid,ansif);
        }

        ans.getText().clear();

        if(index>0){
            index--;
            data = questions.get(index);
            txtque.setText(data.getquestion());
            qid = data.getqid();
            isexist = dbo.isexistDes(email,pass,schid,qid);
            if(isexist) {
                ansif = dbo.ifexistDes(email, pass, schid, qid);
                ans.setText(ansif);
            }
        }

        if(index==0){
            btnprev.setEnabled(false);
            btnprev.setAlpha(.5f);
        }

    }

    private void onNext(View view) {
        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ansif;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexistDes(email,pass,schid,qid);

        btnprev.setEnabled(true);
        btnprev.setAlpha(1f);

        if(!ans.getText().toString().matches("")){
            ansif = ans.getText().toString();
        }
        else
        {
            ansif = "Not Attempted";
        }

        if(!isexist){
            dbo.insansDes(email,pass,schid,qid,ansif);
        }
        else{
            dbo.updansDes(email,pass,schid,qid,ansif);
        }

        ans.getText().clear();

        if(index<(questions.size()-1)){
            index++;
            data = questions.get(index);
            txtque.setText(data.getquestion());
            qid = data.getqid();
            isexist = dbo.isexistDes(email,pass,schid,qid);
            if(isexist) {
                ansif = dbo.ifexistDes(email, pass, schid, qid);
                ans.setText(ansif);
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

    public void onTimeOut(){

        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ansif;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexistDes(email,pass,schid,qid);

        if(!ans.getText().toString().matches("")){
            ansif = ans.getText().toString();
        }
        else
        {
            ansif = "Not Attempted";
        }

        if(!isexist){
            dbo.insansDes(email,pass,schid,qid,ansif);
        }
        else{
            dbo.updansDes(email,pass,schid,qid,ansif);
        }

        Intent myIntent = new Intent(this, HomePageActivity.class);
        this.startActivity(myIntent);

    }

    public void onBackPressed() {

        btnsub.performClick();

    }
}