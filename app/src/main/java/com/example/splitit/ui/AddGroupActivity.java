package com.example.splitit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splitit.R;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;

public class AddGroupActivity extends AppCompatActivity {

    private EditText etGroupName;
    private Button btnSaveGroup;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        etGroupName = findViewById(R.id.etGroupName);
        btnSaveGroup = findViewById(R.id.btnSaveGroup);
        dbHelper = new AppDatabaseHelper(this);

        btnSaveGroup.setOnClickListener(v -> {
            String groupName = etGroupName.getText().toString().trim();
            if(!groupName.isEmpty()){
                saveGroup(groupName);
            } else {
                Toast.makeText(this, "Enter a group name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveGroup(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.GroupEntry.COLUMN_NAME_GROUP_NAME, name);
        long newRowId = db.insert(DatabaseContract.GroupEntry.TABLE_NAME, null, values);
        if(newRowId != -1) {
            Toast.makeText(this, "Group saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving group", Toast.LENGTH_SHORT).show();
        }
    }
}

