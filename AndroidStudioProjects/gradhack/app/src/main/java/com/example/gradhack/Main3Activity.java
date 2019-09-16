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

public class Main3Activity extends AppCompatActivity {
    public Button run;
    public Button next_page;
    public Button previous_page;
    public TextView message;
    public Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        run = (Button) findViewById(R.id.button1);
        run.setOnClickListener(new View.OnClickListener(){
                                   @Override
                                   public void onClick(View view) {
                                       Main3Activity.ExtractTransaction extractTransaction = new Main3Activity.ExtractTransaction();
                                       extractTransaction.execute("");
                                   }
                               }
        );

        previous_page = (Button) findViewById(R.id.button3);
        previous_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        next_page = (Button) findViewById(R.id.button2);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main4Activity.class);
                startActivity(intent);
            }
        });
    }

    public class ExtractTransaction extends AsyncTask<String, String, String> {
        String z = "";
        String account_no_1 = "";
        String transaction_time_1 = "";
        String merchant_1= "";
        String amount_1 = "";
        List<String> account_no = new ArrayList<>();
        List<String> transaction_time = new ArrayList<>();
        List<String> merchant = new ArrayList<>();
        List<String> amount = new ArrayList<>();
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(Main3Activity.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess) {
                message = (TextView) findViewById(R.id.textView1);
                //String mesg = account_type_1 + "  " + account_no_1 + "  " + account_balance_1;
                String mesg = account_no.get(0) + "\n" + transaction_time.get(0) + "\n" + merchant.get(0) + "\n" + amount.get(0);
                mesg += "\n\n\n" + account_no.get(1) + "\n" + transaction_time.get(1) + "\n" + merchant.get(1) + "\n" + amount.get(1);
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
                    String query = "select * from transaction_detail";
                    Statement stm = con.createStatement();
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        account_no_1 = rs.getString("account_no");
                        transaction_time_1 = rs.getString("transaction_time");
                        merchant_1 = rs.getString("merchant");
                        amount_1 = rs.getString("amount");
                        account_no.add(account_no_1);
                        transaction_time.add(transaction_time_1);
                        merchant.add(merchant_1);
                        amount.add(amount_1);
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





