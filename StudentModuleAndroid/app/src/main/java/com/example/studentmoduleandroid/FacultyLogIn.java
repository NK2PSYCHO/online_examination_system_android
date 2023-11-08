package com.example.studentmoduleandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FacultyLogIn extends AppCompatActivity {

    EditText edtPassword,edtEmail;
    Button btnLogin;
    Boolean log=false;
    DBOperations dbo;
    SharedPreferences obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_log_in);
        edtEmail = findViewById(R.id.EmailEdt);
        edtPassword=findViewById(R.id.PassEdt);
        btnLogin = findViewById(R.id.LogInButton);
        obj = getSharedPreferences("fMyfile",MODE_PRIVATE);
        boolean status = obj.getBoolean("status",false);
        if(status){
            Intent myIntent = new Intent(this, HomePageFacultyActivity.class);
            this.startActivity(myIntent);
        }
        if(obj.contains("email")&&obj.contains("password")){
            String email = obj.getString("email","def");
            String pass = obj.getString("password","def");
            edtEmail.setText(email);
            edtPassword.setText(pass);
        }

    }

    public void FLogIn(View view){
        dbo = new DBOperations();
        SharedPreferences.Editor editor = obj.edit();
        final Intent[] myIntent = new Intent[1];
        if (edtEmail.getText().toString().matches("") ||
                edtPassword.getText().toString().matches(""))
        {
            Toast.makeText(FacultyLogIn.this,"One or more fields empty"
                    ,Toast.LENGTH_LONG).show();
        }
        else{
            log=dbo.FLogIn(edtEmail.getText().toString(),
                    edtPassword.getText().toString());
            if(log){
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            editor.putBoolean("status",true);
                            editor.putBoolean("Mstatus",true);
                            editor.apply();
                            myIntent[0] = new Intent(this, HomePageFacultyActivity.class);
                            this.startActivity(myIntent[0]);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            editor.putBoolean("status",false);
                            editor.apply();
                            myIntent[0] = new Intent(this, HomePageFacultyActivity.class);
                            this.startActivity(myIntent[0]);
                            break;
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Keep Logged In ?")
                        .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                                dialogClickListener)
                        .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                                dialogClickListener).show();
                editor.putString("email",edtEmail.getText().toString());
                editor.putString("password",edtPassword.getText().toString());
                editor.apply();
            }
            else {
                Toast.makeText(FacultyLogIn.this,"Invalid Username or Password"
                        ,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void FShowPass(View view){
        if(edtPassword.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else{
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        edtPassword.setSelection(edtPassword.length());
    }

    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    moveTaskToBack(true);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    if(!obj.contains("email") || !obj.getBoolean("status",false)) {
                        Intent myIntent = new Intent(this, FacultyLogIn.class);
                        this.startActivity(myIntent);
                    }
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit ?")
                .setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"),
                        dialogClickListener)
                .setNegativeButton(Html.fromHtml("<font color='#FF0000'>No</font>"),
                        dialogClickListener).show();
    }
}