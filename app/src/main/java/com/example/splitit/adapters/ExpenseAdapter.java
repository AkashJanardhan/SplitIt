package com.example.splitit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Expense;

import java.text.BreakIterator;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvDescription.setText(expense.getDescription());
        holder.tvAmount.setText(String.format("$%.2f", expense.getAmount()));
        holder.tvPayerName.setText("Paid by: " + (expense.getPayerName() != null ? expense.getPayerName() : "Unknown"));
    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPayerName;
        TextView tvDescription, tvAmount;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvExpenseDescription);
            tvAmount = itemView.findViewById(R.id.tvExpenseAmount);
            tvPayerName = itemView.findViewById(R.id.tvPayerName);

        }
    }
}

