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
import android.widget.TextView;

import com.example.splitit.R;
import com.example.splitit.adapters.ExpenseAdapter;
import com.example.splitit.adapters.MemberAdapter;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;
import com.example.splitit.model.Expense;
import com.example.splitit.model.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView tvGroupTitle;
    private Button btnAddMember, btnAddExpense, btnSettleUp;
    private RecyclerView rvMembers, rvExpenses;
    private long groupId;
    private AppDatabaseHelper dbHelper;
    private MemberAdapter memberAdapter;
    private ExpenseAdapter expenseAdapter;
    private List<Member> memberList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        tvGroupTitle = findViewById(R.id.tvGroupTitle);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnSettleUp = findViewById(R.id.btnSettleUp);
        rvMembers = findViewById(R.id.rvMembers);
        rvExpenses = findViewById(R.id.rvExpenses);

        dbHelper = new AppDatabaseHelper(this);

        groupId = getIntent().getLongExtra("group_id", -1);
        loadGroupName();
        loadMembers();
        loadExpenses();

        memberAdapter = new MemberAdapter(memberList);
        expenseAdapter = new ExpenseAdapter(expenseList);

        rvMembers.setLayoutManager(new LinearLayoutManager(this));
        rvMembers.setAdapter(memberAdapter);

        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        rvExpenses.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvExpenses.setAdapter(expenseAdapter);

        btnAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailActivity.this, AddMemberActivity.class);
            intent.putExtra("group_id", groupId);
            startActivity(intent);
        });

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailActivity.this, AddExpenseActivity.class);
            intent.putExtra("group_id", groupId);
            startActivity(intent);
        });

        btnSettleUp.setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailActivity.this, SettleUpActivity.class);
            intent.putExtra("group_id", groupId);
            startActivity(intent);
        });
    }

    private void loadGroupName() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.GroupEntry.TABLE_NAME,
                null,
                DatabaseContract.GroupEntry._ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GroupEntry.COLUMN_NAME_GROUP_NAME));
            tvGroupTitle.setText(name);
        }
        cursor.close();
    }

    private void loadMembers() {
        memberList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.MemberEntry.TABLE_NAME,
                null,
                DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.MemberEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME));
            String imgUri = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_IMAGE));
            memberList.add(new Member(id, name, groupId, imgUri));
        }
        cursor.close();
    }

    private void loadExpenses() {
        expenseList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<Long, String> memberNameMap = new HashMap<>();
        for(Member m : memberList) {
            memberNameMap.put(m.getId(), m.getName());
        }

        Cursor cursor = db.query(DatabaseContract.ExpenseEntry.TABLE_NAME, null,
                DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);

        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry._ID));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_NAME_DESCRIPTION));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
            long payerId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_NAME_PAYER_ID));
            String payerName = memberNameMap.get(payerId);
            expenseList.add(new Expense(id, desc, amount, payerId, groupId, payerName));
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
        loadExpenses();
        memberAdapter.notifyDataSetChanged();
        expenseAdapter.notifyDataSetChanged();
    }
}

