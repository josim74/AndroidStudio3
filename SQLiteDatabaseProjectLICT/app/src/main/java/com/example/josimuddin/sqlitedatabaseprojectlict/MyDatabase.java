package com.example.josimuddin.sqlitedatabaseprojectlict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by JosimUddin on 30/10/2017.
 */

public class MyDatabase {
    public static final String DB_NAME = "empDb";
    MyHelper myHelper;
    Context myContext;
    SQLiteDatabase sqLiteDatabase;

    // create a constructor...
    public MyDatabase(Context Context) {
        this.myContext = Context;

        myHelper = new MyHelper(myContext, DB_NAME, null, 1);
    }

    public void open() {
        sqLiteDatabase = myHelper.getWritableDatabase();
    }

    public void empInsert(ContentValues contentValues) {
        sqLiteDatabase.insert("employee", null, contentValues);
    }

    public Cursor getEmp() {
        sqLiteDatabase = myHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query("employee", null, null, null, null, null, null);
        return cursor;
    }
}
