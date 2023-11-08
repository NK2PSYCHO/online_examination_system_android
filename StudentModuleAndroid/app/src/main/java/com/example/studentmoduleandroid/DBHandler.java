package com.example.studentmoduleandroid;

import android.os.StrictMode;
import android.util.Log;

import java.sql.*;

public class DBHandler {
    String ip,port,db,user,pass,conurl;

    public Connection conClass()
    {
        ip="192.168.2.4";
        port="1433";
        db="ExamModule";
        user="GURU";
        pass="Poke@123";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn=null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conurl="jdbc:jtds:sqlserver://"+ip+":"+port+";"+"databaseName="+db+";user="+user
                    +";password="+pass+";";
            conn = DriverManager.getConnection(conurl,user,pass);
        }
        catch (Exception e){
            Log.e("Error :",e.getMessage());
        }
        return conn;
    }

}