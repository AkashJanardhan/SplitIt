package com.example.splitit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.example.splitit.R;
import com.example.splitit.database.AppDatabaseHelper;
import com.example.splitit.database.DatabaseContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettleUpActivity extends AppCompatActivity {

    private TextView tvBalances;
    private AppDatabaseHelper dbHelper;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);

        tvBalances = findViewById(R.id.tvBalances);
        dbHelper = new AppDatabaseHelper(this);
        groupId = getIntent().getLongExtra("group_id", -1);

        showMinimalTransactions();
    }

    private void showMinimalTransactions() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Get members
        List<Long> memberIds = new ArrayList<>();
        Map<Long, String> memberNames = new HashMap<>();
        Cursor cMembers = db.query(DatabaseContract.MemberEntry.TABLE_NAME,
                null,
                DatabaseContract.MemberEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);

        while(cMembers.moveToNext()){
            long mid = cMembers.getLong(cMembers.getColumnIndexOrThrow(DatabaseContract.MemberEntry._ID));
            String mName = cMembers.getString(cMembers.getColumnIndexOrThrow(DatabaseContract.MemberEntry.COLUMN_NAME_MEMBER_NAME));
            memberIds.add(mid);
            memberNames.put(mid, mName);
        }
        cMembers.close();

        // If no members, just show a message
        if (memberIds.isEmpty()) {
            tvBalances.setText("No members in this group, nothing to settle.");
            return;
        }

        Map<Long, Double> amountsPaid = new HashMap<>();
        for (long mId : memberIds) {
            amountsPaid.put(mId, 0.0);
        }

        double total = 0.0;
        Cursor cExpenses = db.query(DatabaseContract.ExpenseEntry.TABLE_NAME,
                null,
                DatabaseContract.ExpenseEntry.COLUMN_NAME_GROUP_ID + "=?",
                new String[]{String.valueOf(groupId)},
                null,null,null);
        while(cExpenses.moveToNext()){
            double amt = cExpenses.getDouble(cExpenses.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
            long payerId = cExpenses.getLong(cExpenses.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_NAME_PAYER_ID));

            // Check if payer is actually in the group
            if (!amountsPaid.containsKey(payerId)) {
                // This means the expense references someone not in this group
                // Decide how to handle:
                // Option 1: Skip this expense
                continue;
                // Option 2: Log an error or show a message to user
            }

            total += amt;
            Double currentPaid = amountsPaid.get(payerId);
            if (currentPaid == null) currentPaid = 0.0; // Default to 0.0 if no entry exists
            amountsPaid.put(payerId, currentPaid + amt);
        }
        cExpenses.close();

        int memberCount = memberIds.size();
        double share = memberCount > 0 ? total / memberCount : 0;

        Map<Long, Double> balances = new HashMap<>();
        List<Long> debtors = new ArrayList<>();
        List<Long> creditors = new ArrayList<>();

        for (long mId : memberIds) {
            Double paid = amountsPaid.get(mId);
            if (paid == null) paid = 0.0;
            double balance = paid - share;
            balances.put(mId, balance);

            if (balance < 0) {
                debtors.add(mId);
            } else if (balance > 0) {
                creditors.add(mId);
            }
        }

        // If no debts or credits, everyone is settled
        if (debtors.isEmpty() && creditors.isEmpty()) {
            StringBuilder settledMsg = new StringBuilder();
            settledMsg.append("Total: $").append(String.format("%.2f", total)).append("\n");
            settledMsg.append("Each share: $").append(String.format("%.2f", share)).append("\n\n");
            settledMsg.append("All settled! No one owes anything.");
            tvBalances.setText(settledMsg.toString());
            return;
        }

        List<String> transactions = new ArrayList<>();
        int dIndex = 0;
        int cIndex = 0;

        while (dIndex < debtors.size() && cIndex < creditors.size()) {
            long debtor = debtors.get(dIndex);
            long creditor = creditors.get(cIndex);

            Double debtorBalance = balances.get(debtor);
            Double creditorBalance = balances.get(creditor);

            // Safety checks
            if (debtorBalance == null) debtorBalance = 0.0;
            if (creditorBalance == null) creditorBalance = 0.0;

            double oweAmount = -debtorBalance;  // how much debtor owes
            double owedAmount = creditorBalance; // how much creditor is owed

            double minAmount = Math.min(oweAmount, owedAmount);

            transactions.add(memberNames.get(debtor) + " owes " + memberNames.get(creditor) + ": $" + String.format("%.2f", minAmount));

            // Update balances
            balances.put(debtor, debtorBalance + minAmount);
            balances.put(creditor, creditorBalance - minAmount);

            // If debtor is now settled
            if (Math.abs(balances.get(debtor)) < 0.000001) {
                dIndex++;
            }

            // If creditor is now settled
            if (Math.abs(balances.get(creditor)) < 0.000001) {
                cIndex++;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total: $").append(String.format("%.2f", total)).append("\n");
        sb.append("Each person's share: $").append(String.format("%.2f", share)).append("\n\n");

        if (transactions.isEmpty()) {
            sb.append("All settled! No one owes anything.");
        } else {
            for (String t : transactions) {
                sb.append(t).append("\n");
            }
        }

        tvBalances.setText(sb.toString());
    }

}


