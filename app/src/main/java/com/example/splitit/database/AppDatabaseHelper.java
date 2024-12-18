package com.example.splitit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "billsplitter.db";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_GROUPS =
            "CREATE TABLE " + DatabaseContract.GroupEntry.TABLE_NAME + " (" +
                    DatabaseContract.GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.GroupEntry.COLUMN_NAME_GROUP_NAME + " TEXT NOT NULL" +
                    ");";

    private static final String SQL_CREATE_MEMBERS =
            "CREATE TABLE " + DatabaseContract.MemberEntry.TABLE_NAME + " (" +
                    DatabaseContract.MemberEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME + " TEXT NOT NULL, " +
                    DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + " INTEGER NOT NULL, " +
                    DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_IMAGE + " TEXT," +
                    "FOREIGN KEY(" + DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + ") REFERENCES " +
                    DatabaseContract.GroupEntry.TABLE_NAME + "(" + DatabaseContract.GroupEntry._ID + ")" +
                    ");";


    private static final String SQL_CREATE_EXPENSES =
            "CREATE TABLE " + DatabaseContract.ExpenseEntry.TABLE_NAME + " (" +
                    DatabaseContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.ExpenseEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    DatabaseContract.ExpenseEntry.COLUMN_NAME_AMOUNT + " REAL NOT NULL, " +
                    DatabaseContract.ExpenseEntry.COLUMN_NAME_PAYER_ID + " INTEGER NOT NULL, " +
                    DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + DatabaseContract.ExpenseEntry.COLUMN_NAME_PAYER_ID + ") REFERENCES " +
                    DatabaseContract.MemberEntry.TABLE_NAME + "(" + DatabaseContract.MemberEntry._ID + "), " +
                    "FOREIGN KEY(" + DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID + ") REFERENCES " +
                    DatabaseContract.GroupEntry.TABLE_NAME + "(" + DatabaseContract.GroupEntry._ID + ")" +
                    ");";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GROUPS);
        db.execSQL(SQL_CREATE_MEMBERS);
        db.execSQL(SQL_CREATE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ExpenseEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MemberEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.GroupEntry.TABLE_NAME);
        onCreate(db);
    }

}

