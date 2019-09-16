package com.example.gradhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    public Button run;
    public Button next_page;
    public TextView message;
    public Connection con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run = (Button) findViewById(R.id.button1);
        run.setOnClickListener(new View.OnClickListener(){
                                   @Override
                                   public void onClick(View view) {
                                       Checklogin checklogin = new Checklogin();
                                       checklogin.execute("");
                                   }
                               }
        );
        next_page = (Button) findViewById(R.id.button2);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private boolean haveInternet(){
        boolean have_wifi = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_wifi = true;
        }
        Log.d("have_wifi: ",Boolean.toString(have_wifi));
        return have_wifi;
    }

    public class Checklogin extends AsyncTask<String, String, String>{
        String z = "";
        String user_first_name = "";
        String user_last_name = "";
        String phone_no = "";
        Boolean isSuccess = false;
        String password = "";

        @Override
        protected void onPostExecute(String r){
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();
            if(isSuccess){
                message = (TextView)findViewById(R.id.textView1);
                String mesg = user_first_name + " " + user_last_name + "\n" + phone_no;
                message.setText(mesg);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                ConnectionHelper conStr=new ConnectionHelper();
                con =conStr.connectionclasss();        // Connect to database
                if(con == null){
                    z = "check your internet access!";
                }
                else{
                    String query = "select * from customer_login";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    if(rs.next()){
                        user_first_name = rs.getString("first_name");
                        user_last_name = rs.getString("last_name");
                        phone_no = rs.getString("phone_no");
                        z = "query success!";
                        isSuccess = true;
                        con.close();
                    }
                }

            }
            catch (Exception e){
                isSuccess = false;
                Log.d("sql error: ","error");
            }
            return z;
        }

    }
    }

