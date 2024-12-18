package com.example.splitit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.splitit.R;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseDescription, etExpenseAmount;
    private Spinner spinnerPayer;
    private Button btnSaveExpense;
    private AppDatabaseHelper dbHelper;
    private long groupId;

    // We'll keep track of member IDs and names.
    private List<Long> memberIds = new ArrayList<>();
    private List<String> memberNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etExpenseDescription = findViewById(R.id.etExpenseDescription);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        spinnerPayer = findViewById(R.id.spinnerPayer);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        dbHelper = new AppDatabaseHelper(this);
        groupId = getIntent().getLongExtra("group_id", -1);

        loadMembers();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, memberNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayer.setAdapter(adapter);

        btnSaveExpense.setOnClickListener(v -> {
            String desc = etExpenseDescription.getText().toString().trim();
            String amountStr = etExpenseAmount.getText().toString().trim();

            if(!amountStr.isEmpty() && spinnerPayer.getSelectedItemPosition() != -1){
                double amount = Double.parseDouble(amountStr);
                long payerId = memberIds.get(spinnerPayer.getSelectedItemPosition());
                addExpense(desc, amount, payerId);
            } else {
                Toast.makeText(this, "Enter valid amount and select payer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMembers() {
        memberIds.clear();
        memberNames.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.MemberEntry.TABLE_NAME,
                new String[]{DatabaseContract.MemberEntry._ID, DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME},
                DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);
        while(cursor.moveToNext()){
            long mId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.MemberEntry._ID));
            String mName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME));
            memberIds.add(mId);
            memberNames.add(mName);
        }
        cursor.close();
    }

    private void addExpense(String desc, double amount, long payerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExpenseEntry.COLUMN_NAME_DESCRIPTION, desc);
        values.put(DatabaseContract.ExpenseEntry.COLUMN_NAME_AMOUNT, amount);
        values.put(DatabaseContract.ExpenseEntry.COLUMN_NAME_PAYER_ID, payerId);
        values.put(DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID, groupId);

        long newRowId = db.insert(DatabaseContract.ExpenseEntry.TABLE_NAME, null, values);
        if(newRowId != -1) {
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding expense", Toast.LENGTH_SHORT).show();
        }
    }
}


