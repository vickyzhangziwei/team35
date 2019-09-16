package com.example.gradhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class Main2Activity extends AppCompatActivity {
    public Button run;
    public Button next_page;
    public Button previous_page;
    public TextView message;
    public Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        run = (Button) findViewById(R.id.button1);
        run.setOnClickListener(new View.OnClickListener(){
                                   @Override
                                   public void onClick(View view) {
                                       Main2Activity.ExtractAccount extractAccount = new Main2Activity.ExtractAccount();
                                       extractAccount.execute("");
                                   }
                               }
        );

        previous_page = (Button) findViewById(R.id.button3);
        previous_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        next_page = (Button) findViewById(R.id.button2);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(intent);
            }
        });
    }
    public class ExtractAccount extends AsyncTask<String, String, String> {
        String z = "";
        String account_no_1 = "";
        String account_type_1= "";
        String account_balance_1 = "";
        List<String> account_no = new ArrayList<>();
        List<String> account_type = new ArrayList<>();
        List<String> account_balance = new ArrayList<>();
        Boolean isSuccess = false;
        String password = "";

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(Main2Activity.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess) {
                message = (TextView) findViewById(R.id.textView1);
                //String mesg = account_type_1 + "  " + account_no_1 + "  " + account_balance_1;
                String mesg = account_type.get(0) + "\n" + account_no.get(0) + "\n" + account_balance.get(0);
                mesg += "\n\n\n" + account_type.get(1) + "\n" + account_no.get(1) + "\n" + account_balance.get(1);
                message.setText(mesg);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                con = conStr.connectionclasss();        // Connect to database
                if (con == null) {
                    z = "check your internet access!";
                } else {
                    String query = "select * from account_detail";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        account_type_1 = rs.getString("account_type");
                        account_no_1 = rs.getString("account_no");
                        account_balance_1 = rs.getString("account_balance");
                        account_type.add(account_type_1);
                        account_no.add(account_no_1);
                        account_balance.add(account_balance_1);
                        z = "query success!";
                        isSuccess = true;
                    }
                    con.close();
                }

            } catch (Exception e) {
                isSuccess = false;
                Log.d("sql error: ", "error");
            }
            return z;
        }
    }
}
