package com.skyracle.QuickParcels;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**

 */
public class ConnectionHelper extends SQLiteOpenHelper {
    Context context;
    public ConnectionHelper(Context context){
        super(context,"Register",null,1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table userinfo(number text,userpwd text,userid text)";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        super.close();

    }
}
