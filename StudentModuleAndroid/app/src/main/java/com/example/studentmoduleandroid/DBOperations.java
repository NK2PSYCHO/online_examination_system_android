package com.example.studentmoduleandroid;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DBOperations {
    Connection con;
    DBHandler db;
    String Conres="passed";

    public void connect(){
        db=new DBHandler();
        con = db.conClass();
    }

    public int[] RandomizeArray(int[] array){
        Random rgen = new Random();

        for (int i=0; i<array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }

        return array;
    }

    public String getsid(String email,String pass){
        String sid="";
        try{
            connect();
            if(con !=null){
                String query = "select Stud_ID from TBL_Student where Email_ID ='"+ email +"'" +
                        " and Passsword='"+ pass +"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                sid = rs.getString(1);
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  sid;
    }

    public Boolean LogIn(String email,String pass){
        boolean is_sus=false;
        try{
            connect();
            if(con !=null){
                String query = "select * from TBL_Student where Email_ID ='"+ email +"'" +
                        "and Passsword='"+ pass +"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    is_sus = true;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  is_sus;
    }

    public String[] getStud(String email,String pass){
        String[] alpha =new String[6];
        try{
            connect();
            if(con !=null){
                String query = "select s.Stud_Name,s.Email_ID,c.Class_Name,s.Enrollment, " +
                        " s.Seat_No from TBL_Student s INNER JOIN TBL_Class c ON " +
                        " s.Class_ID=c.Class_ID and " +
                        " s.Email_ID='"+ email +"' and Passsword='"+ pass +"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    for(int i=1;i<rs.getMetaData().getColumnCount()+1;i++)
                        alpha[i] = rs.getString(i) +"\n";
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return alpha;
    }

    public ArrayList<DataModel>  schCHK(String email, String pass){
        ArrayList<DataModel> dataModels = new ArrayList<>();
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        try{
            connect();
            if(con !=null){
                String query = "select sch.Date,sch.Exam_Type,sch.Start_Time" +
                        ",sch.End_Time,sub.Subject_Name,sch.Schedule_ID,sch.Start_Limit" +
                        " from TBL_Schedule sch INNER JOIN" +
                        " TBL_Subject sub on sch.Subject_ID = sub.Subject_ID" +
                        " Left join TBL_Student s on s.Class_ID = sch.Class_ID" +
                        " and s.Email_ID='"+ email +"' and s.Passsword='"+ pass +"'" +
                        " where sch.Date='"+ todayString +"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    do{
                        dataModels.add(new DataModel(rs.getString(1)
                                ,rs.getString(3),rs.getString(4)
                                ,rs.getString(5),rs.getString(2)
                                ,rs.getString(6),rs.getString(7)));
                    }while(rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return dataModels;
    }

    public Boolean isdone(String email,String pass,String schid){
        boolean togo = true;
        try{
            connect();
            if(con !=null){
                String query = "select * from TBL_MCQAnswer where Schedule_ID ='"+ schid +"' " +
                        " and Stud_ID = ( select Stud_ID from TBL_Student where Email_ID =" +
                        " '"+ email +"' and Passsword = '"+ pass +"' ) ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    togo = false;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  togo;
    }

    public ArrayList<DataModel1> getExam(String schid){
        ArrayList<DataModel1> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = "select Question_ID , Question , Option_A , Option_B , Option_C " +
                        " , Option_D from TBL_MCQNA where Schedule_ID = '"+ schid +"' ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    do{
                        int[] arr = {3,4,5,6};
                        arr=RandomizeArray(arr);
                        datamodels.add(new DataModel1(rs.getString(2)
                                ,rs.getString(arr[0]),rs.getString(arr[1])
                                ,rs.getString(arr[2]),rs.getString(arr[3])
                                ,rs.getString(1)));
                    }while(rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public Boolean isexist(String email,String pass,String schid,String qid){
        boolean isexist = false;
        try{
            connect();
            if(con !=null){

                String query = "select * from TBL_MCQAnswer where Schedule_ID ='"+ schid +"' " +
                        " and Question_ID = '" + qid + "' and Stud_ID = " +
                        "( select Stud_ID from TBL_Student where Email_ID =" +
                        " '"+ email +"' and Passsword = '"+ pass +"' ) ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);

                if(rs.next()) {
                    isexist = true;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  isexist;
    }

    public void insans(String email,String pass,String schid,String qid,String ans){
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String ins = "insert into TBL_MCQAnswer (Schedule_ID,Stud_ID,Question_ID,Answer)" +
                        " values (?, ?, ?, ?) ";
                PreparedStatement insstat = con.prepareStatement(ins);
                insstat.setString(1, schid);
                insstat.setString(2, studid);
                insstat.setString(3, qid);
                insstat.setString(4, ans);
                insstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updans(String email,String pass,String schid,String qid,String ans){
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String upd = "update TBL_MCQAnswer set Answer = ? where Schedule_Id = ? " +
                        " and Stud_Id = ? and Question_ID = ?";
                PreparedStatement updstat = con.prepareStatement(upd);
                updstat.setString(1, ans);
                updstat.setString(2, schid);
                updstat.setString(3, studid);
                updstat.setString(4, qid);
                updstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public String ifexist(String email,String pass,String schid,String qid){
        String ans ="";
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String get = "select Answer from TBL_MCQAnswer where Schedule_Id = ? " +
                        " and Question_ID = ? and Stud_ID = ?";
                PreparedStatement getstat = con.prepareStatement(get);
                getstat.setString(1, schid);
                getstat.setString(2, qid);
                getstat.setString(3, studid);
                ResultSet rs = getstat.executeQuery();
                if(rs.next()){
                    ans=rs.getString(1);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return ans;
    }

    public void SetResult(String email,String pass,String schid){
        float obmark=0;
        String obmarks;
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String get = "select ma.Answer,mq.Correct_Option from TBL_MCQNA mq " +
                        " INNER JOIN TBL_MCQAnswer ma on ma.Schedule_ID = mq.Schedule_ID and " +
                        " ma.Question_ID = mq.Question_ID where ma.Schedule_Id = ? and " +
                        " ma.Stud_ID = ?";
                PreparedStatement getstat = con.prepareStatement(get);
                getstat.setString(1, schid);
                getstat.setString(2, studid);
                ResultSet rs = getstat.executeQuery();
                if(rs.next()){
                    do {
                        if (rs.getString(1).equals(rs.getString(2))) {
                            obmark += 2;
                        } else if(!rs.getString(1).equals("Not Selected")){
                            obmark -= 1;
                        }
                    }while(rs.next());
                }
                obmarks = String.valueOf(obmark);
                String set = "insert into TBL_Result (Stud_ID,Sch_ID,Marks_Obtained) " +
                        " values (?, ?, ?) ";
                PreparedStatement setstat = con.prepareStatement(set);
                setstat.setString(1, studid);
                setstat.setString(2, schid);
                setstat.setString(3, obmarks);
                setstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public ArrayList<DataModel> getResult(String email,String pass){
        ArrayList<DataModel> datamodels = new ArrayList<>();
        String studid = getsid(email,pass);
        try{
            connect();
            if(con !=null){
                String query = "select sch.Date,sch.Exam_Type,sch.Start_Time," +
                        "sch.End_Time,sub.Subject_Name,sch.Schedule_ID," +
                        "sch.Start_Limit from TBL_Schedule sch INNER JOIN TBL_Subject " +
                        " sub on sch.Subject_ID = sub.Subject_ID INNER JOIN TBL_Result res on " +
                        " res.Sch_ID = sch.Schedule_ID and res.Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,studid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel(rs.getString(1)
                                ,rs.getString(3),rs.getString(4)
                                ,rs.getString(5),rs.getString(2)
                                ,rs.getString(6),rs.getString(7)));
                    }while(rs.next());
                }
                else{
                    datamodels.add(new DataModel("No results are","",""
                                    ,"Declared yet","","",""));
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public ArrayList<DataModel2> dispResult(String email,String pass,String schid){
        ArrayList<DataModel2> datamodels = new ArrayList<>();
        String studid = getsid(email,pass);
        try{
            connect();
            if(con !=null){
                String query = "select distinct mcq.Question , mcq.Correct_Option , " +
                        " mcqa.Answer from TBL_MCQNA mcq INNER JOIN TBL_MCQAnswer " +
                        " mcqa on mcq.Schedule_ID = mcqa.Schedule_ID and " +
                        " mcq.Question_ID = mcqa.Question_ID\n" +
                        " where mcqa.Schedule_ID = ? and mcqa.Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,studid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel2(rs.getString(1)
                                ,rs.getString(2),rs.getString(3)));
                    }while (rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public String gettotmarks(String email,String pass,String schid){
        String totMarks ="";
        String studid = getsid(email,pass);
        try{
            connect();
            if(con !=null){
                String query = "select Marks_Obtained from TBL_Result " +
                        "  where Sch_ID = ? and Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,studid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    totMarks=rs.getString(1);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return totMarks;
    }

    public String chktype(String schid){
        String type = "";
        try{
            connect();
            if(con !=null){
                String query = "select Exam_Type from TBL_Schedule " +
                        " where Schedule_ID = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    type=rs.getString(1);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return type;
    }

    public ArrayList<DataModel1> getDesExam(String schid){
        ArrayList<DataModel1> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = "select Question_ID , Question from TBL_DESQNA where " +
                        " Schedule_ID = '"+ schid +"' ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel1(rs.getString(2)
                                ,"none","none"
                                ,"none","none"
                                ,rs.getString(1)));
                    }while(rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public Boolean isexistDes(String email,String pass,String schid,String qid){
        boolean isexist = false;
        try{
            connect();
            if(con !=null){

                String query = "select * from TBL_DESQAnswer where Schedule_ID ='"+ schid +"' " +
                        " and Question_ID = '" + qid + "' and Stud_ID = " +
                        "( select Stud_ID from TBL_Student where Email_ID =" +
                        " '"+ email +"' and Passsword = '"+ pass +"' ) ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);

                if(rs.next()) {
                    isexist = true;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  isexist;
    }

    public void insansDes(String email,String pass,String schid,String qid,String ans){
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String ins = "insert into TBL_DESQAnswer (Schedule_ID,Stud_ID,Question_ID,Answer)" +
                        " values (?, ?, ?, ?) ";
                PreparedStatement insstat = con.prepareStatement(ins);
                insstat.setString(1, schid);
                insstat.setString(2, studid);
                insstat.setString(3, qid);
                insstat.setString(4, ans);
                insstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updansDes(String email,String pass,String schid,String qid,String ans){
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String upd = "update TBL_DESQAnswer set Answer = ? where Schedule_Id = ? " +
                        " and Stud_Id = ? and Question_ID = ?";
                PreparedStatement updstat = con.prepareStatement(upd);
                updstat.setString(1, ans);
                updstat.setString(2, schid);
                updstat.setString(3, studid);
                updstat.setString(4, qid);
                updstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public String ifexistDes(String email,String pass,String schid,String qid){
        String ans ="";
        try{
            connect();
            if(con !=null){
                String studid = getsid(email,pass);
                String get = "select Answer from TBL_DESQAnswer where Schedule_Id = ? " +
                        " and Question_ID = ? and Stud_ID = ?";
                PreparedStatement getstat = con.prepareStatement(get);
                getstat.setString(1, schid);
                getstat.setString(2, qid);
                getstat.setString(3, studid);
                ResultSet rs = getstat.executeQuery();
                if(rs.next()){
                    ans=rs.getString(1);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return ans;
    }

    public Boolean isdoneDes(String email,String pass,String schid){
        boolean togo = true;
        try{
            connect();
            if(con !=null){
                String query = "select * from TBL_DESQAnswer where Schedule_ID ='"+ schid +"' " +
                        " and Stud_ID = ( select Stud_ID from TBL_Student where Email_ID =" +
                        " '"+ email +"' and Passsword = '"+ pass +"' ) ";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    togo = false;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  togo;
    }

    public ArrayList<DataModel3> dispResultDes(String email,String pass,String schid){
        ArrayList<DataModel3> datamodels = new ArrayList<>();
        String studid = getsid(email,pass);
        try{
            connect();
            if(con !=null){
                String query = "select distinct mcq.Question ," +
                        " mcqa.Answer,mcqa.Remarks" +
                        " , mcqa.marking from TBL_DESQNA mcq INNER JOIN TBL_DESQAnswer " +
                        " mcqa on mcq.Schedule_ID = mcqa.Schedule_ID and " +
                        " mcq.Question_ID = mcqa.Question_ID\n" +
                        " where mcqa.Schedule_ID = ? and mcqa.Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,studid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel3(rs.getString(1)
                                ,rs.getString(2),rs.getString(3)
                                ,rs.getString(4)));
                    }while (rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public Boolean FLogIn(String email,String pass){
        boolean is_sus=false;
        try{
            connect();
            if(con !=null){
                String query = "select * from TBL_Faculty where Email_ID ='"+ email +"'" +
                        "and Password='"+ pass +"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    is_sus = true;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  is_sus;
    }

    public ArrayList<DataModel4> getALLmcq(String email,String pass){
        ArrayList<DataModel4> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = " select res.Stud_ID,res.Sch_ID,res.Marks_Obtained,sch.Date," +
                        " sbj.Subject_Name,Class_Name,stu.Seat_No,stu.Stud_Name from TBL_Result res" +
                        " INNER JOIN TBL_Schedule sch on sch.Schedule_ID=res.Sch_ID" +
                        " INNER JOIN TBL_Faculty fac on sch.Class_ID = fac.Class_ID" +
                        " and sch.Subject_ID = fac.Subject_ID" +
                        " INNER JOIN TBL_Student stu on fac.Class_ID=stu.Class_ID" +
                        " and res.Stud_ID = stu.Stud_ID" +
                        " INNER JOIN TBL_Subject sbj on sch.Subject_ID = sbj.Subject_ID" +
                        " INNER JOIN TBL_Class cl on cl.Class_ID = sch.Class_ID" +
                        " where sch.Exam_Type like 'MCQ' and" +
                        " fac.Email_ID = '" + email + "'" +
                        " and fac.Password = '" + pass + "'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    do{
                        datamodels.add(new DataModel4(rs.getString(1),
                                rs.getString(2),rs.getString(3),
                                rs.getString(4),rs.getString(5),
                                rs.getString(6),rs.getString(7),
                                rs.getString(8)));
                    }while(rs.next());
                }
                else{
                    datamodels.add(new DataModel4("",
                            "","", "Not Declared/Available Yet","", "",
                            "",""));
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public ArrayList<DataModel2> fdispResult(String sid,String schid){
        ArrayList<DataModel2> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = "select distinct mcq.Question , mcq.Correct_Option , " +
                        " mcqa.Answer from TBL_MCQNA mcq INNER JOIN TBL_MCQAnswer " +
                        " mcqa on mcq.Schedule_ID = mcqa.Schedule_ID and " +
                        " mcq.Question_ID = mcqa.Question_ID\n" +
                        " where mcqa.Schedule_ID = ? and mcqa.Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,sid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel2(rs.getString(1)
                                ,rs.getString(2),rs.getString(3)));
                    }while (rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public String fgettotmarks(String studid,String schid){
        String totMarks ="";
        try{
            connect();
            if(con !=null){
                String query = "select Marks_Obtained from TBL_Result " +
                        "  where Sch_ID = ? and Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,studid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    totMarks=rs.getString(1);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return totMarks;
    }

    public ArrayList<DataModel4> getUnchecked(String email,String pass){
        ArrayList<DataModel4> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query =  "select distinct de.Stud_ID,de.Schedule_ID,sch.Date," +
                        " sbj.Subject_Name,cl.Class_Name,stu.Seat_No," +
                        " stu.Stud_Name from TBL_DESQAnswer de" +
                        " INNER JOIN TBL_Schedule sch on de.Schedule_ID=sch.Schedule_ID" +
                        " INNER JOIN TBL_Subject sbj on sch.Subject_ID=sbj.Subject_ID" +
                        " INNER JOIN TBL_Class cl on cl.Class_ID = sch.Class_ID" +
                        " INNER JOIN TBL_Faculty fac on sch.Class_ID = fac.Class_ID" +
                        " and sch.Subject_ID = fac.Subject_ID" +
                        " INNER JOIN TBL_Student stu on fac.Class_ID=stu.Class_ID and" +
                        " stu.Stud_Id = de.Stud_Id" +
                        " LEFT JOIN TBL_Result res on res.Stud_ID=de.Stud_ID" +
                        " and res.Sch_ID=de.Schedule_ID" +
                        " where res.Stud_ID is NULL and" +
                        " fac.Email_ID='"+email+"' and fac.Password='"+pass+"'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    do{
                        datamodels.add(new DataModel4(rs.getString(1),
                                rs.getString(2),"",
                                rs.getString(3),rs.getString(4),
                                rs.getString(5), rs.getString(6),
                                rs.getString(7)));
                    }while(rs.next());
                }
                else{
                    datamodels.add(new DataModel4("",
                            "","", "Not Declared/Available Yet","", ""
                    ,"",""));
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public ArrayList<DataModel4> getALLdes(String email,String pass){
        ArrayList<DataModel4> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = " select res.Stud_ID,res.Sch_ID,res.Marks_Obtained,sch.Date," +
                        " sbj.Subject_Name,Class_Name,stu.Seat_No,stu.Stud_Name from TBL_Result res" +
                        " INNER JOIN TBL_Schedule sch on sch.Schedule_ID=res.Sch_ID" +
                        " INNER JOIN TBL_Faculty fac on sch.Class_ID = fac.Class_ID" +
                        " and sch.Subject_ID = fac.Subject_ID" +
                        " INNER JOIN TBL_Student stu on fac.Class_ID=stu.Class_ID" +
                        " and res.Stud_ID = stu.Stud_ID" +
                        " INNER JOIN TBL_Subject sbj on sch.Subject_ID = sbj.Subject_ID" +
                        " INNER JOIN TBL_Class cl on cl.Class_ID = sch.Class_ID" +
                        " where sch.Exam_Type like 'Descriptive' and" +
                        " fac.Email_ID = '" + email + "'" +
                        " and fac.Password = '" + pass + "'";
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    do{
                        datamodels.add(new DataModel4(rs.getString(1),
                                rs.getString(2),rs.getString(3),
                                rs.getString(4),rs.getString(5),
                                rs.getString(6),rs.getString(7)
                        ,rs.getString(8)));
                    }while(rs.next());
                }
                else{
                    datamodels.add(new DataModel4("",
                            "","", "Not Declared/Available Yet","", ""
                    ,"",""));
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public ArrayList<DataModel3> fdispResultDes(String sid,String schid){
        ArrayList<DataModel3> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = "select distinct mcq.Question ," +
                        " mcqa.Answer,mcqa.Remarks" +
                        " , mcqa.marking from TBL_DESQNA mcq INNER JOIN TBL_DESQAnswer " +
                        " mcqa on mcq.Schedule_ID = mcqa.Schedule_ID and " +
                        " mcq.Question_ID = mcqa.Question_ID\n" +
                        " where mcqa.Schedule_ID = ? and mcqa.Stud_ID= ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,schid);
                statement.setString(2,sid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel3(rs.getString(1)
                                ,rs.getString(2),rs.getString(3)
                                ,rs.getString(4)));
                    }while (rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public ArrayList<DataModel5> assessDes(String sid,String schid){
        ArrayList<DataModel5> datamodels = new ArrayList<>();
        try{
            connect();
            if(con !=null){
                String query = "select distinct da.Question_ID,da.Schedule_ID,de.Stud_ID," +
                        " da.Question,da.Marking,de.Answer from TBL_DESQNA da" +
                        " INNER JOIN TBL_DESQAnswer de on de.Question_ID=da.Question_ID" +
                        " and de.Schedule_ID=da.Schedule_ID" +
                        " and de.Stud_ID = ? and de.Schedule_ID = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,sid);
                statement.setString(2,schid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    do{
                        datamodels.add(new DataModel5(rs.getString(1)
                                ,rs.getString(2),rs.getString(3)
                                ,rs.getString(4),rs.getString(5)
                                ,rs.getString(6)));
                    }while (rs.next());
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  datamodels;
    }

    public Boolean isassessDes(String sid,String schid,String qid){
        boolean exist=false;
        try{
            connect();
            if(con !=null){
                String query = "select distinct Remarks,Marking from TBL_DESQAnswer where" +
                        " Stud_ID=? and Schedule_ID=? and Question_ID=?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,sid);
                statement.setString(2,schid);
                statement.setString(3,qid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    exist = true;
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  exist;
    }

    public void updassessDes(String remark,String mark,String sid,String schid,String qid){
        try{
            connect();
            if(con !=null){
                String query = "update TBL_DESQAnswer set  Remarks = ? , Marking = ? " +
                        " where Stud_ID = ? and Schedule_ID = ? and Question_ID = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,remark);
                statement.setString(2,mark);
                statement.setString(3,sid);
                statement.setString(4,schid);
                statement.setString(5,qid);
                statement.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public String[] ifassessDes(String sid,String schid,String qid){
        String[] get = new String[2];
        try{
            connect();
            if(con !=null){
                String query = "select distinct Remarks,Marking from TBL_DESQAnswer where" +
                        " Stud_ID=? and Schedule_ID=? and Question_ID=?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1,sid);
                statement.setString(2,schid);
                statement.setString(3,qid);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    get[0]=rs.getString(1);
                    get[1]=rs.getString(2);
                }
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  get;
    }

    public void fSetResult(String sid,String schid){
        float obmark=0;
        String obmarks;
        try{
            connect();
            if(con !=null){
                String get = "select ma.Marking from TBL_DESQAnswer ma where" +
                        " ma.Schedule_Id = ? and " +
                        " ma.Stud_ID = ?";
                PreparedStatement getstat = con.prepareStatement(get);
                getstat.setString(1, schid);
                getstat.setString(2, sid);
                ResultSet rs = getstat.executeQuery();
                if(rs.next()){
                    do {
                        obmark+=rs.getFloat(1);
                    }while(rs.next());
                }
                obmarks = String.valueOf(obmark);
                String set = "insert into TBL_Result (Stud_ID,Sch_ID,Marks_Obtained) " +
                        " values (?, ?, ?) ";
                PreparedStatement setstat = con.prepareStatement(set);
                setstat.setString(1, sid);
                setstat.setString(2, schid);
                setstat.setString(3, obmarks);
                setstat.executeUpdate();
            }
            else
            {
                Conres="failed";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}