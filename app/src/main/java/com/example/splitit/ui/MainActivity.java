package com.example.splitit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.example.splitit.R;
import com.example.splitit.adapters.GroupAdapter;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;
import com.example.splitit.model.Group;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGroups;
    private Button btnAddGroup;
    private AppDatabaseHelper dbHelper;
    private GroupAdapter adapter;
    private List<Group> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvGroups = findViewById(R.id.rvGroups);
        btnAddGroup = findViewById(R.id.btnAddGroup);
        dbHelper = new AppDatabaseHelper(this);

        loadGroups();

        adapter = new GroupAdapter(groupList, new GroupAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(Group group) {
                Intent intent = new Intent(MainActivity.this, GroupDetailActivity.class);
                intent.putExtra("group_id", group.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteGroupClick(Group group) {
                deleteGroup(group.getId());
            }
        });

        rvGroups.setLayoutManager(new LinearLayoutManager(this));
        rvGroups.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvGroups.setAdapter(adapter);


        btnAddGroup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddGroupActivity.class));
        });
    }

    private void loadGroups() {
        groupList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.GroupEntry.TABLE_NAME,
                null,null,null,null,null,null);
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.GroupEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GroupEntry.COLUMN_NAME_GROUP_NAME));
            groupList.add(new Group(id, name));
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroups();
        adapter.notifyDataSetChanged();
    }

    private void deleteGroup(long groupId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Delete expenses first
        db.delete(DatabaseContract.ExpenseEntry.TABLE_NAME,
                DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)});
        // Delete members
        db.delete(DatabaseContract.MemberEntry.TABLE_NAME,
                DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)});
        // Delete group
        db.delete(DatabaseContract.GroupEntry.TABLE_NAME,
                DatabaseContract.GroupEntry._ID + "=?",
                new String[]{String.valueOf(groupId)});

        loadGroups();
        adapter.notifyDataSetChanged();
    }
}
