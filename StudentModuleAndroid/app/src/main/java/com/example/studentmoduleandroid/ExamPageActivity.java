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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ExamPageActivity extends AppCompatActivity {

    Button btnext,btnprev,btnsub;
    TextView txtcdown,txtque;
    RadioButton opt1,opt2,opt3,opt4,optsel;
    RadioGroup rgp;
    SharedPreferences obj;
    ArrayList<DataModel1> questions = new ArrayList<>();
    DBOperations dbo;
    DataModel1 data;
    int index;
    String schid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page);

        txtque = findViewById(R.id.txtQue);
        txtcdown = findViewById(R.id.countdown);
        btnext = findViewById(R.id.next);
        btnext.setOnClickListener(this::onNext);

        btnprev = findViewById(R.id.prev);
        btnprev.setOnClickListener(this::onPrev);
        btnprev.setEnabled(false);
        btnprev.setAlpha(.5f);

        btnsub = findViewById(R.id.submit);
        btnsub.setOnClickListener(this::onSubmit);

        rgp = findViewById(R.id.QueOpt);
        opt1= findViewById(R.id.opt1);
        opt2= findViewById(R.id.opt2);
        opt3= findViewById(R.id.opt3);
        opt4= findViewById(R.id.opt4);

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
        questions = dbo.getExam(schid);

        if(!questions.isEmpty()){
            Collections.shuffle(questions);
            index = 0;
            data = questions.get(index);
            txtque.setText(data.getquestion());
            opt1.setText(data.getoption1());
            opt2.setText(data.getoption2());
            opt3.setText(data.getoption3());
            opt4.setText(data.getoption4());
        }
    }

    private void onNext(View view){
        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ans;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexist(email,pass,schid,qid);

        btnprev.setEnabled(true);
        btnprev.setAlpha(1f);


        if(rgp.getCheckedRadioButtonId()!=-1){
            int sid = rgp.getCheckedRadioButtonId();
            optsel = rgp.findViewById(sid);
            ans = optsel.getText().toString();
        }
        else
        {
            ans = "Not Selected";
        }

        if(!isexist){
            dbo.insans(email,pass,schid,qid,ans);
        }
        else{
            dbo.updans(email,pass,schid,qid,ans);
        }

        rgp.clearCheck();

        if(index<(questions.size()-1)){
            index++;
            data = questions.get(index);
            txtque.setText(data.getquestion());
            opt1.setText(data.getoption1());
            opt2.setText(data.getoption2());
            opt3.setText(data.getoption3());
            opt4.setText(data.getoption4());
            qid = data.getqid();
            isexist = dbo.isexist(email,pass,schid,qid);
            if(isexist) {
                ans = dbo.ifexist(email, pass, schid, qid);
                if (ans.equals(opt1.getText().toString())) {
                    rgp.check(R.id.opt1);
                }
                if (ans.equals(opt2.getText().toString())) {
                    rgp.check(R.id.opt2);
                }
                if (ans.equals(opt3.getText().toString())) {
                    rgp.check(R.id.opt3);
                }
                if (ans.equals(opt4.getText().toString())) {
                    rgp.check(R.id.opt4);
                }
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

    private void onPrev(View view){
        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ans;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexist(email,pass,schid,qid);

        btnext.setEnabled(true);
        btnext.setAlpha(1f);

        if(rgp.getCheckedRadioButtonId()!=-1){
            int sid = rgp.getCheckedRadioButtonId();
            optsel = rgp.findViewById(sid);
            ans = optsel.getText().toString();
        }
        else
        {
            ans = "Not Selected";
        }

        if(!isexist){
            dbo.insans(email,pass,schid,qid,ans);
        }
        else{
            dbo.updans(email,pass,schid,qid,ans);
        }

        rgp.clearCheck();

        if(index>0){
            index--;
            data = questions.get(index);
            txtque.setText(data.getquestion());
            opt1.setText(data.getoption1());
            opt2.setText(data.getoption2());
            opt3.setText(data.getoption3());
            opt4.setText(data.getoption4());
            qid = data.getqid();
            isexist = dbo.isexist(email,pass,schid,qid);
            if(isexist){
                ans = dbo.ifexist(email,pass,schid,qid);
                if(ans.equals(opt1.getText().toString())){
                    rgp.check(R.id.opt1);
                }
                if(ans.equals(opt2.getText().toString())){
                    rgp.check(R.id.opt2);
                }
                if(ans.equals(opt3.getText().toString())){
                    rgp.check(R.id.opt3);
                }
                if(ans.equals(opt4.getText().toString())){
                    rgp.check(R.id.opt4);
                }
            }
        }

        if(index==0){
            btnprev.setEnabled(false);
            btnprev.setAlpha(.5f);
        }

    }

    private void onSubmit(View view){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    data = questions.get(index);
                    obj = getSharedPreferences("Myfile",MODE_PRIVATE);
                    String email = obj.getString("email","def");
                    String pass = obj.getString("password","def");
                    String qid = data.getqid();
                    String ans;
                    dbo = new DBOperations();
                    Boolean isexist = dbo.isexist(email,pass,schid,qid);

                    if(rgp.getCheckedRadioButtonId()!=-1){
                        int sid = rgp.getCheckedRadioButtonId();
                        optsel = rgp.findViewById(sid);
                        ans = optsel.getText().toString();
                    }
                    else
                    {
                        ans = "Not Selected";
                    }

                    if(!isexist){
                        dbo.insans(email,pass,schid,qid,ans);
                    }
                    else{
                        dbo.updans(email,pass,schid,qid,ans);
                    }

                    dbo.SetResult(email,pass,schid);

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

    public void onTimeOut(){

        data = questions.get(index);
        obj = getSharedPreferences("Myfile",MODE_PRIVATE);
        String email = obj.getString("email","def");
        String pass = obj.getString("password","def");
        String qid = data.getqid();
        String ans;
        dbo = new DBOperations();
        Boolean isexist = dbo.isexist(email,pass,schid,qid);

        if(rgp.getCheckedRadioButtonId()!=-1){
            int sid = rgp.getCheckedRadioButtonId();
            optsel = rgp.findViewById(sid);
            ans = optsel.getText().toString();
        }
        else
        {
            ans = "Not Selected";
        }

        if(!isexist){
            dbo.insans(email,pass,schid,qid,ans);
        }
        else{
            dbo.updans(email,pass,schid,qid,ans);
        }

        dbo.SetResult(email,pass,schid);


        Intent myIntent = new Intent(this, HomePageActivity.class);
        this.startActivity(myIntent);

    }

    public void onBackPressed() {

        btnsub.performClick();

    }

}