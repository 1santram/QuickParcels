package com.skyracle.QuickParcels;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    ConnectionHelper con = new ConnectionHelper(SplashScreen.this);
                    SQLiteDatabase db = con.getWritableDatabase();
                    String query = "select * from userinfo";
                    Cursor c = db.rawQuery(query, null);
                    if (c.moveToFirst()) {

                        Intent i = new Intent(SplashScreen.this,Hom.class);
                        startActivity(i);
                        con.close();
                    }
                    else {

                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);


                    }

                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

}
